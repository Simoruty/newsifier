package com.newsifier.dao.interfaces;

import java.io.InputStream;

public interface DatasetDAO {

    Object getDataset(String containerName, String fileName);
    void saveDataset(InputStream in, String containerName, String fileName);
}
