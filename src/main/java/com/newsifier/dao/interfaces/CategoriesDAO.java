package com.newsifier.dao.interfaces;


import com.newsifier.watson.bean.NewsNLU;
import com.newsifier.watson.bean.NewsWithKeywords;

import java.util.List;

public interface CategoriesDAO {
    /**
     * Adds the keywords/categories to the input List of News.
     * If a news already exists appends the keywords/categories
     */
    void insertCategories(List<NewsNLU> news);

    /**
     * Adds the input keywords/categories to the input News.
     * If the news already exists appends the keywords/categories.
     */
    void insertCategories(NewsNLU news);

    /**
     * Retrieves the news having the input category
     */
    List<NewsWithKeywords> getNewsbyCat(String cat);

    /**
     * Retrieves the document related to the input category.
     * Returns a string representing the CSV entries for the input category.
     */
    String newsToCSV(String cat);

    List<String> allCategories();
}
