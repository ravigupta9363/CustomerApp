package com.example.ravi_gupta.slider.Models;

import com.example.ravi_gupta.slider.Repository.SplashRepository;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.remoting.VirtualObject;

/**
 * Created by robins on 26/9/15.
 */
public class SplashContainer extends VirtualObject {

    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    /**
     * Upload a new file
     * @param file Content of the file.
     * @param callback The callback to be executed when finished.
     */
    public void upload(java.io.File file, ObjectCallback<SplashFile> callback) {
        getFileRepository().upload(file, callback);
    }

    /**
     * Upload a new file
     * @param fileName The file name, must be unique within the container.
     * @param content Content of the file.
     * @param contentType Content type (optional).
     * @param callback The callback to be executed when finished.
     */
    public void upload(String fileName, byte[] content, String contentType,
                       ObjectCallback<SplashFile> callback) {
        getFileRepository().upload(fileName, content, contentType, callback);
    }

    /**
     * Create a new File object associated with this container.
     * @param name The name of the file.
     * @return the object created
     */
    public SplashFile createFileObject(String name) {
        return getFileRepository().createObject(ImmutableMap.of("name", name));
    }

    /**
     * Get data of a File object.
     * @param fileName The name of the file.
     * @param callback The callback to be executed when finished.
     */
    public void getFile(String fileName, ObjectCallback<SplashFile> callback) {
        getFileRepository().get(fileName, callback);
    }

    /**
     * List all files in the container.
     * @param callback The callback to be executed when finished.
     */
    public void getAllFiles(ListCallback<SplashFile> callback) {
        getFileRepository().getAll(callback);
    }

    public SplashRepository getFileRepository() {
        RestAdapter adapter = ((RestAdapter)getRepository().getAdapter());
        SplashRepository repo = adapter.createRepository(SplashRepository.class);
        repo.setContainer(this);
        return repo;
    }
}
