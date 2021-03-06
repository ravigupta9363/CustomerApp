package com.example.ravi_gupta.slider.Repository;

/**
 * Created by robins on 26/9/15.
 */
import com.example.ravi_gupta.slider.Models.SplashContainer;
import com.example.ravi_gupta.slider.Models.SplashFile;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.RestRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.JsonObjectParser;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;
import com.strongloop.android.remoting.adapters.StreamParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SplashRepository extends RestRepository<SplashFile> {
    private final static String TAG = "SplashRepository";

    private SplashContainer container;

    public SplashContainer getContainer() {
        return container;
    }
    public String getContainerName() {
        return getContainer().getName();
    }

    public void setContainer(SplashContainer value) {
        container = value;
    }

    public SplashRepository() {
        super("file", SplashFile.class);
    }

    /**
     * Creates a {@link RestContract} representing the user type's custom
     * routes. Used to extend an {@link Adapter} to support user. Calls
     * super {@link ModelRepository} createContract first.
     *
     * @return A {@link RestContract} for this model type.
     */

    public RestContract createContract() {
        RestContract contract = super.createContract();

        String basePath = "/containers/:container";
        String className = getClassName();

        contract.addItem(new RestContractItem(basePath + "/files/:name", "GET"),
                className + ".get");

        contract.addItem(new RestContractItem(basePath + "/files", "GET"),
                className + ".getAll");

        contract.addItem(
                RestContractItem.createMultipart(basePath + "/upload", "POST"),
                className + ".upload");

        contract.addItem(new RestContractItem(basePath +  "/download/:name", "GET"),
                className + ".prototype.download");

        contract.addItem(new RestContractItem(basePath + "/files/:name", "DELETE"),
                className + ".prototype.delete");

        return contract;
    }

    @Override
    public SplashFile createObject(Map<String, ? extends Object> parameters) {
        SplashFile file = super.createObject(parameters);
        file.setContainerRef(container);
        return file;
    }

    /**
     * Upload a new file
     * @param name The file name, must be unique within the container.
     * @param content Content of the file.
     * @param contentType Content type (optional).
     * @param callback The callback to be executed when finished.
     */
    public void upload(String name, byte[] content, String contentType,
                       ObjectCallback<SplashFile> callback) {
        upload(name, new ByteArrayInputStream(content), contentType, callback);
    }

    /**
     * Upload a new file
     * @param name The file name, must be unique within the container.
     * @param content Content of the file.
     * @param contentType Content type (optional).
     * @param callback The callback to be executed when finished.
     */
    public void upload(String name, InputStream content, String contentType,
                       final ObjectCallback<SplashFile> callback) {

        StreamParam param = new StreamParam(content, name, contentType);
        invokeStaticMethod("upload",
                ImmutableMap.of("container", getContainerName(), "file", param),
                new UploadResponseParser(this, callback));
    }

    /**
     * Upload a new file
     * @param localFile The local file to upload.
     * @param callback The callback to be executed when finished.
     */
    public void upload(java.io.File localFile, final ObjectCallback<SplashFile> callback) {
        invokeStaticMethod("upload",
                ImmutableMap.of("container", getContainerName(), "file", localFile),
                new UploadResponseParser(this, callback));
    }

    /**
     * Get file by name
     * @param name The name of the file to get.
     * @param callback The callback to be executed when finished.
     */
    public void get(String name, final ObjectCallback<SplashFile> callback) {
        final HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("container", getContainerName());
        params.put("name", name);
        invokeStaticMethod("get", params,
                new JsonObjectParser<SplashFile>(this, callback));
    }

    /**
     * List all files in the container.
     * @param callback The callback to be executed when finished.
     */
    public void getAll(ListCallback<SplashFile> callback) {
        invokeStaticMethod("getAll",
                ImmutableMap.of("container", getContainerName()),
                new JsonArrayParser<SplashFile>(this, callback));
    }

    private class UploadResponseParser extends Adapter.JsonObjectCallback {
        private final SplashRepository repository;
        private final ObjectCallback<SplashFile> callback;

        private UploadResponseParser(SplashRepository repository, ObjectCallback<SplashFile> callback) {
            this.repository = repository;
            this.callback = callback;
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                JSONObject data = response.getJSONObject("result")
                        .getJSONObject("files")
                        .getJSONArray("file")
                        .getJSONObject(0);
                callback.onSuccess(
                        repository.createObject(JsonUtil.fromJson(data)));
            } catch (JSONException e) {
                callback.onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            callback.onError(t);
        }
    }
}
