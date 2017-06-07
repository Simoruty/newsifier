package com.newsifier.controller;

import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.newsifier.dao.impl.CloudantCategoriesDAO;
import com.newsifier.dao.impl.CloudantFeedDAO;
import com.newsifier.dao.impl.CloudantNewsDAO;
import com.newsifier.dao.interfaces.CategoriesDAO;
import com.newsifier.dao.interfaces.FeedDAO;
import com.newsifier.dao.interfaces.NewsDAO;
import com.newsifier.rss.bean.Feed;
import com.newsifier.rss.bean.News;
import com.newsifier.watson.reader.Extractor;
import com.newsifier.watson.bean.NewsNLU;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

        System.out.println(" ++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++  CREATION FEED DOCUMENTS  ++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++  \n");

        // Adding feeds document
        FeedDAO cloudantFeedDAO = new CloudantFeedDAO();
        cloudantFeedDAO.insertFeeds(feedsList);


        System.out.println(" ++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++  CREATION NEWS DOCUMENTS  ++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++  \n");

        // Adding news documents by feed
        NewsDAO cloudantNewsDAO = new CloudantNewsDAO();
        cloudantNewsDAO.insertNews(feedsList);


        System.out.println(" ++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" ++++++  EXTRACTOR NLU  +++++++++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++  \n\n");



        System.out.println("FEED: " + f1.getName() + "\n\n");
        List<News> newsList = cloudantNewsDAO.getNews(f1);

        //extract keywords from news
        Extractor e = new Extractor(20);
        ArrayList<NewsNLU> newsNLUArrayList = new ArrayList<>();

        for (News news : newsList) {

            System.out.println(new Timestamp(System.currentTimeMillis()) + " Start extraction info for news : " + news.getUri());
            try {
                newsNLUArrayList.add(e.extractInfo(news.getUri()));
            } catch (ServiceResponseException exp){
                if (exp.getStatusCode() > 400)
                {
                    System.err.println(" SERVICE ERROR " + exp.getStatusCode());
                    break;
                }
            }
            System.out.println(new Timestamp(System.currentTimeMillis()) + " End extraction info for news : " + news.getUri());
        }


        System.out.println(" ++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++  CREATION CAT DOCUMENTS  +++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++  \n\n");


        CategoriesDAO categoriesDAO = new CloudantCategoriesDAO();
        categoriesDAO.insertCategories(newsNLUArrayList);

        System.out.println(" ++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++++++++++  END  ++++++++++++++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++  \n\n");


    }

}
