package com.newsifier.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.newsifier.Credentials;
import com.newsifier.Settings;
import com.newsifier.dao.impl.CloudantCategoriesDAO;
import com.newsifier.dao.impl.CloudantFeedDAO;
import com.newsifier.dao.impl.CloudantNewsDAO;
import com.newsifier.dao.impl.ObjectStorageDatasetDAO;
import com.newsifier.dao.interfaces.CategoriesDAO;
import com.newsifier.dao.interfaces.DatasetDAO;
import com.newsifier.dao.interfaces.FeedDAO;
import com.newsifier.dao.interfaces.NewsDAO;
import com.newsifier.rss.bean.Feed;
import com.newsifier.rss.bean.News;
import com.newsifier.rss.reader.RssManager;
import com.newsifier.watson.bean.Dataset;
import com.newsifier.watson.bean.NewsNLU;
import com.newsifier.watson.bean.SampleTestSetEntry;
import com.newsifier.watson.reader.ClassifierNLC;
import com.newsifier.watson.reader.Extractor;


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

        int newsLimit = Integer.parseInt(request.getParameter("newslimit"));
        int kwlimit = Integer.parseInt(request.getParameter("kwlimit"));
        double catthreshold = Double.parseDouble(request.getParameter("catthreshold"));
        double kwthreshold = Double.parseDouble(request.getParameter("kwthreshold"));
        double trainingtestpercentage = Double.parseDouble(request.getParameter("trainingtestpercentage"));

        Settings settings = new Settings(newsLimit, kwlimit,catthreshold, kwthreshold, trainingtestpercentage);

        ArrayList<Feed> feedsList = new ArrayList<>();
        Feed f1 = new Feed("Ansa_Cronaca", new URL("http://www.ansa.it/sito/notizie/cronaca/cronaca_rss.xml"));
        Feed f2 = new Feed("Ansa_Politica", new URL("http://www.ansa.it/sito/notizie/politica/politica_rss.xml"));
        Feed f3 = new Feed("Ansa_Calcio", new URL("http://www.ansa.it/sito/notizie/sport/calcio/calcio_rss.xml"));
        //RssManager.printNews(f2);

        feedsList.add(f1);
        feedsList.add(f2);
        feedsList.add(f3);

        System.out.println(" \n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++++++++++++++++++  CREATION FEED DOCUMENTS  ++++++++++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        // Adding feeds document
        FeedDAO cloudantFeedDAO = new CloudantFeedDAO();

        cloudantFeedDAO.insertFeeds(feedsList);


        System.out.println(" \n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++++++++++++++++++  CREATION NEWS DOCUMENTS  ++++++++++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        // Adding news documents by feed
        // Set limit news for feed
        NewsDAO cloudantNewsDAO = new CloudantNewsDAO();

        for (Feed feed : feedsList) {
            cloudantNewsDAO.insertNews(RssManager.readerNews(feed, settings.getLimitNews()), feed);
        }

        System.out.println(" \n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++++++++++++++++++  EXTRACTOR NLU  ++++++++++++++++++++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        StringBuilder stringCSV = new StringBuilder();

        for (Feed feed : feedsList) {
            System.out.println("\n\n -----------------------------------------------------------  ");
            System.out.println(" ------  FEED: " + feed.getName());
            System.out.println(" ----------------------------------------------------------- \n\n");
            List<News> newsList = cloudantNewsDAO.getNews(feed);

            //extract keywords from news
            Extractor e = new Extractor(settings.getLimitKeywordsNews());
            NewsNLU newsNLU;

            CategoriesDAO categoriesDAO = new CloudantCategoriesDAO();

            for (News news : newsList) {

                System.out.println(new Timestamp(System.currentTimeMillis()) + " Start extraction info for news : " + news.getUri());
                try {

                    // second parameter "score" is the threshold to retrieve the categories
                    // third parameter "relevance" is the threshold to retrieve the keywords

                    newsNLU = e.extractInfo(news.getUri(), settings.getScore(), settings.getRelevance());
                    categoriesDAO.insertCategories(newsNLU);

                } catch (ServiceResponseException exp) {
                    if (exp.getStatusCode() > 400) {
                        System.err.println(" SERVICE ERROR " + exp.getStatusCode());
                        break;
                    }
                }
                System.out.println(new Timestamp(System.currentTimeMillis()) + " End extraction info for news : " + news.getUri());
            }


            System.out.println(" \n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
            System.out.println(" +++++++++++++++++++++ OBJECT STORAGE +++++++++++++++++++++++  ");
            System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n  ");


            for (String category : categoriesDAO.allCategories()) {
                stringCSV.append(categoriesDAO.newsToCSV(category));
                //System.out.println(categoriesDAO.newsToCSV(category));
            }
        }

        DatasetDAO o = new ObjectStorageDatasetDAO();
        o.saveDataset(stringCSV.toString(), Credentials.getContainernameObj(), Credentials.getDatasetnameObj());

        System.out.println(" \n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++++++++++++++++++ NLC ++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n ");

        File datasetFile = o.getDatasetFile(Credentials.getContainernameObj(), Credentials.getDatasetnameObj());
        ClassifierNLC classifierNLC = new ClassifierNLC();

        Dataset dataset = classifierNLC.splitDataset(datasetFile, settings.getTrainingDimension());

        System.out.println(" ---------------- Creation Training Set ------------------  ");

        o.saveDataset(dataset.getTrainingSet(), Credentials.getContainernameObj(), Credentials.getTrainingsetnameObj());

        System.out.println(" ---------------- Creation Test Set ----------------------  ");

        o.saveDataset(dataset.getTestSet(), Credentials.getContainernameObj(), Credentials.getTestsetnameObj());

        System.out.println(" ---------- Creation and Training Classifier --------------  ");

        File trainingFile = o.getDatasetFile(Credentials.getContainernameObj(), Credentials.getTrainingsetnameObj());
        classifierNLC.createClassifier(trainingFile, Credentials.getClassifiername());


        System.out.println(" ------------------ Testing Classifier --------------------  ");

        File testFile = o.getDatasetFile(Credentials.getContainernameObj(), Credentials.getTestsetnameObj());
        List<SampleTestSetEntry> testSetEntries = classifierNLC.testClassifier(testFile);

        System.out.println(" ------------------- Performance --------------------------  ");

        double precision = classifierNLC.precisionClassifier(datasetFile, testSetEntries);
        System.out.println(" Precision: " + precision + "\n");

        System.out.println(" \n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        System.out.println(" +++++++++++++++++++++++  END  ++++++++++++++++++++++++++++++  ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");


    }

}
