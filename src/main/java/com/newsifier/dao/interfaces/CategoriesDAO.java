package com.newsifier.dao.interfaces;


import com.newsifier.watson.bean.NewsNLU;
import com.newsifier.watson.bean.NewsNLUByCat;

import java.util.List;

public interface CategoriesDAO {
    void insertCategories(List<NewsNLU> news);

    void insertCategories(NewsNLU news);

    List<NewsNLUByCat> getNewsbyCat(String cat);

    String newsToCSV(String cat);

    List<String> allCategories();
}
