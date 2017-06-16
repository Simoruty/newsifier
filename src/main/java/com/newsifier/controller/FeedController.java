package com.newsifier.controller;

import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.newsifier.Credentials;
import com.newsifier.Logger;
import com.newsifier.Settings;
import com.newsifier.dao.impl.*;
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

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@WebServlet(urlPatterns = "/feed", asyncSupported = true)
public class FeedController extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final AsyncContext asyncContext = request.startAsync(request, response);

        new Thread() {

            @Override
            public void run() {
                ServletResponse response = asyncContext.getResponse();
                response.setContentType("text/plain");
                eraseAll();
                asyncContext.complete();
            }
        }.start();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newsLimit = request.getParameter("newslimit");
        String kwlimit = request.getParameter("kwlimit");
        String catthreshold = request.getParameter("catthreshold");
        String kwthreshold = request.getParameter("kwthreshold");
        String trainingtestpercentage = request.getParameter("trainingtestpercentage");

        final Settings settings = new Settings(newsLimit, kwlimit, catthreshold, kwthreshold, trainingtestpercentage);

        final ArrayList<Feed> feedsList = new ArrayList<>();

        String[] feeds = request.getParameter("feeds").split("\n");

        for (String feedStr : feeds) {
            if (!feedStr.isEmpty()) {
                Feed feed = new Feed(feedStr, new URL(feedStr));
                feedsList.add(feed);
            }
        }

        /*
        Feed f1 = new Feed("Ansa_Cronaca", new URL("http://www.ansa.it/sito/notizie/cronaca/cronaca_rss.xml"));
        Feed f2 = new Feed("Ansa_Politica", new URL("http://www.ansa.it/sito/notizie/politica/politica_rss.xml"));
        Feed f3 = new Feed("Ansa_Calcio", new URL("http://www.ansa.it/sito/notizie/sport/calcio/calcio_rss.xml"));
        //RssManager.printNews(f2);

        feedsList.add(f1);
        feedsList.add(f2);
        feedsList.add(f3);

        */

        final AsyncContext asyncContext = request.startAsync(request, response);

        new Thread() {

            @Override
            public void run() {
                ServletResponse response = asyncContext.getResponse();
                response.setContentType("text/plain");
                execution(settings, feedsList);
                asyncContext.complete();
            }
        }.start();

    }

    private static NewsDAO creationCloudantFromRss(Settings settings, ArrayList<Feed> feedsList) {
        Logger.log(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.log(" +++++++++++++++++++ FEED DOCUMENTS CREATION ++++++++++++++++  ");
        Logger.log(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        Logger.log(" Feed added : " + feedsList.size());

        Logger.webLog(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.webLog(" +++++++++++++++++++ FEED DOCUMENTS CREATION ++++++++++++++++  ");
        Logger.webLog(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        Logger.webLog(" Feed added : " + feedsList.size());


        // Adding feeds document
        FeedDAO cloudantFeedDAO = new CloudantFeedDAO();

        cloudantFeedDAO.insertFeeds(feedsList);


        Logger.log(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.log(" +++++++++++++++++++ NEWS DOCUMENTS CREATION ++++++++++++++++  ");
        Logger.log(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        Logger.webLog(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.webLog(" +++++++++++++++++++ NEWS DOCUMENTS CREATION ++++++++++++++++  ");
        Logger.webLog(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");


        // Adding news documents by feed
        // Set limit news for feed
        NewsDAO cloudantNewsDAO = new CloudantNewsDAO();

        for (Feed feed : feedsList) {
            cloudantNewsDAO.insertNews(RssManager.readerNews(feed, settings.getLimitNews()), feed);
        }

        return cloudantNewsDAO;
    }


    private static String retrieveInfoFromNLU(Settings settings, ArrayList<Feed> feedsList, NewsDAO cloudantNewsDAO) {
        Logger.log(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.log(" +++++++++++++++++++ NLU EXTRACTION  ++++++++++++++++++++++++  ");
        Logger.log(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        Logger.webLog(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.webLog(" +++++++++++++++++++ NLU EXTRACTION  ++++++++++++++++++++++++  ");
        Logger.webLog(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");


        StringBuilder stringCSV = new StringBuilder();

        for (Feed feed : feedsList) {
            Logger.log("\n\n -----------------------------------------------------------  ");
            Logger.log(" ---  FEED: " + feed.getName());
            Logger.log(" ----------------------------------------------------------- \n\n");

            Logger.webLog("\n\n -----------------------------------------------------------  ");
            Logger.webLog(" ---  FEED: " + feed.getName());
            Logger.webLog(" ----------------------------------------------------------- \n\n");

            List<News> newsList = cloudantNewsDAO.getNews(feed);

            //extract keywords from news
            Extractor e = new Extractor(settings.getLimitKeywordsNews());
            NewsNLU newsNLU;

            CategoriesDAO categoriesDAO = new CloudantCategoriesDAO();

            Logger.webLog("Start extraction info for news ");

            for (News news : newsList) {

                Logger.log(new Timestamp(System.currentTimeMillis()) + " Start extraction info for news : " + news.getUri());

                try {

                    // second parameter "score" is the threshold to retrieve the categories
                    // third parameter "relevance" is the threshold to retrieve the keywords

                    newsNLU = e.extractInfo(news.getUri(), settings.getScore(), settings.getRelevance());
                    categoriesDAO.insertCategories(newsNLU);

                } catch (ServiceResponseException exp) {
                    if (exp.getStatusCode() > 400) {
                        Logger.logErr(" SERVICE ERROR " + exp.getStatusCode());
                        break;
                    }
                }
                Logger.log(new Timestamp(System.currentTimeMillis()) + " End extraction info for news : " + news.getUri());
            }


            for (String category : categoriesDAO.allCategories()) {
                stringCSV.append(categoriesDAO.newsToCSV(category));
                //Logger.log(categoriesDAO.newsToCSV(category));
            }
        }

        return stringCSV.toString();
    }


    private static void saveDatasetObjectStorage(DatasetDAO datasetDAO, String datasetCSV) {
        Logger.log(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.log(" +++++++++++++++++++++ OBJECT STORAGE +++++++++++++++++++++++  ");
        Logger.log(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n  ");


        Logger.webLog(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.webLog(" +++++++++++++++++++++ OBJECT STORAGE +++++++++++++++++++++++  ");
        Logger.webLog(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n  ");

        datasetDAO.saveDataset(datasetCSV, Credentials.getContainernameObj(), Credentials.getDatasetnameObj());

    }


    private static void creationExecutionNLC(Settings settings, DatasetDAO datasetDAO) {
        Logger.log(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.log(" +++++++++++++++++++++++++ NLC ++++++++++++++++++++++++++++++  ");
        Logger.log(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n ");

        Logger.webLog(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.webLog(" +++++++++++++++++++++++++ NLC ++++++++++++++++++++++++++++++  ");
        Logger.webLog(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n ");


        File datasetFile = datasetDAO.getDatasetFile(Credentials.getContainernameObj(), Credentials.getDatasetnameObj());
        ClassifierNLC classifierNLC = new ClassifierNLC();

        Dataset dataset = classifierNLC.splitDataset(datasetFile, settings.getTrainingDimension());

        Logger.log(" \n---------------- Training Set Creation ------------------  ");
        Logger.webLog(" \n---------------- Training Set Creation ------------------  ");

        datasetDAO.saveDataset(dataset.getTrainingSet(), Credentials.getContainernameObj(), Credentials.getTrainingsetnameObj());

        Logger.log(" \n---------------- Test Set Creation ----------------------  ");
        Logger.webLog(" \n---------------- Test Set Creation ----------------------  ");

        datasetDAO.saveDataset(dataset.getTestSet(), Credentials.getContainernameObj(), Credentials.getTestsetnameObj());

        Logger.log(" \n---------- Classifier Creation and Training --------------  ");
        Logger.webLog(" \n---------- Classifier Creation and Training --------------  ");

        File trainingFile = datasetDAO.getDatasetFile(Credentials.getContainernameObj(), Credentials.getTrainingsetnameObj());
        classifierNLC.createClassifier(trainingFile, Credentials.getClassifierName());


        Logger.log(" \n------------------ Classifier Testing --------------------  ");
        Logger.webLog(" \n------------------ Classifier Testing --------------------  ");

        File testFile = datasetDAO.getDatasetFile(Credentials.getContainernameObj(), Credentials.getTestsetnameObj());
        List<SampleTestSetEntry> testSetEntries = classifierNLC.testClassifier(testFile, Credentials.getClassifierName());

        if (testSetEntries != null) {
            Logger.log(" \n------------------- Performance --------------------------  ");
            Logger.webLog(" \n------------------- Performance --------------------------  ");

            double precision = classifierNLC.precisionClassifier(datasetFile, testSetEntries);
            Logger.log(" Precision: " + precision + "\n");
            Logger.webLog(" Precision: " + precision + "\n");
        }
        else {
            Logger.log("Testing is not available");
            Logger.webLog("Testing is not available");
        }
    }


    private static void execution(Settings settings, ArrayList<Feed> feedsList) {

        NewsDAO cloudantNewsDAO = creationCloudantFromRss(settings, feedsList);

        String datasetCSV = retrieveInfoFromNLU(settings, feedsList, cloudantNewsDAO);

        DatasetDAO datasetDAO = new ObjectStorageDatasetDAO();

        saveDatasetObjectStorage(datasetDAO, datasetCSV);

        creationExecutionNLC(settings, datasetDAO);

        Logger.log(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.log(" +++++++++++++++++++++++  END  ++++++++++++++++++++++++++++++++  ");
        Logger.log(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        Logger.webLog(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.webLog(" +++++++++++++++++++++++  END  +++++++++++++++++++++++++++++++  ");
        Logger.webLog(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

    }


    private static void eraseAll() {
        eraseCloudant();
        eraseObjectStorage();
        eraseNLC();

        Logger.log(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.log(" +++++++++++++++++++++++  END  ++++++++++++++++++++++++++++++++  ");
        Logger.log(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

        Logger.webLog(" \n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  ");
        Logger.webLog(" +++++++++++++++++++++++  END  +++++++++++++++++++++++++++++++  ");
        Logger.webLog(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  \n\n");

    }

    private static void eraseCloudant() {
        CloudantUtilsDAO.eraseDatabase();
    }

    private static void eraseObjectStorage() {
        DatasetDAO datasetDAO = new ObjectStorageDatasetDAO();
        datasetDAO.eraseDataset(Credentials.getContainernameObj(), Credentials.getTrainingsetnameObj());
        datasetDAO.eraseDataset(Credentials.getContainernameObj(), Credentials.getTestsetnameObj());
        datasetDAO.eraseDataset(Credentials.getContainernameObj(), Credentials.getDatasetnameObj());
        datasetDAO.eraseDataset(Credentials.getContainernameObj());
    }

    private static void eraseNLC() {
        ClassifierNLC classifierNLC = new ClassifierNLC();
        classifierNLC.eraseClassifier(Credentials.getClassifierName());
    }

}
