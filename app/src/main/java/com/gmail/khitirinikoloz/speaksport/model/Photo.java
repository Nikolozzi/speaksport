package com.gmail.khitirinikoloz.speaksport.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Photo implements Serializable {
    private long id;
    private String name;
    private String path;
    private String contentType;
    private String extension;
    private String size;
    private String lastUpdateTime;
    private String lastUpdatedBy;
    private String fileObject;

    public Photo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getFileObject() {
        return fileObject;
    }

    public void setFileObject(String fileObject) {
        this.fileObject = fileObject;
    }

    @NonNull
    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", contentType='" + contentType + '\'' +
                ", extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", fileObject='" + fileObject + '\'' +
                '}';
    }
}
