package com.newsifier.controller;

import com.newsifier.dao.impl.CloudantFeedDAO;
import com.newsifier.dao.impl.CloudantNewsDAO;
import com.newsifier.dao.interfaces.FeedDAO;
import com.newsifier.dao.interfaces.NewsDAO;
import com.newsifier.rss.bean.Feed;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

@WebServlet("/feed")
public class FeedController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().print("Hello newsifier!");

        ArrayList<Feed> feedsList = new ArrayList<>();
        Feed f1 = new Feed("Ansa Cronaca", new URL("http://www.ansa.it/sito/notizie/cronaca/cronaca_rss.xml"));
        Feed f2 = new Feed("Ansa Politica", new URL("http://www.ansa.it/sito/notizie/politica/politica_rss.xml"));
        Feed f3 = new Feed("Ansa Calcio", new URL("http://www.ansa.it/sito/notizie/sport/calcio/calcio_rss.xml"));
        //RssManager.printNews(f2);

        feedsList.add(f1);
        feedsList.add(f2);
        feedsList.add(f3);

        FeedDAO cloudantFeedDAO = new CloudantFeedDAO();
        cloudantFeedDAO.insertFeeds(feedsList);

        NewsDAO cloudantNewsDAO = new CloudantNewsDAO();
        cloudantNewsDAO.insertNews(feedsList);

    }

}
