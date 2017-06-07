package com.newsifier.dao.interfaces;


import com.newsifier.watson.bean.NewsNLU;
import com.newsifier.watson.bean.NewsNLUByCat;

import java.util.List;

public interface CategoriesDAO {
    void insertCategories(List<NewsNLU> news);
    List<NewsNLUByCat> getNewsbyCat(String cat);
}
