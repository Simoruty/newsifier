package com.newsifier.dao.interfaces;


import java.io.File;

public interface DatasetDAO {

    String getDataset(String containerName, String fileName);
    File getDatasetFile(String containerName, String fileName);
    void saveDataset(String datasetStr, String containerName, String fileName);

    void eraseDataset(String containerName);
    void eraseDataset(String containerName, String fileName);
}
