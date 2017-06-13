package com.newsifier.dao.interfaces;


public interface DatasetDAO {

    String getDataset(String containerName, String fileName);
    void saveDataset(String datasetStr, String containerName, String fileName);
}
