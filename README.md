# Newsifier

Application that uses IBM Watson services to create, train and test a NLC Classifier of news.

## Bluemix Services used

* [Cloudant NoSQL DB](https://console.bluemix.net/catalog/services/cloudant-nosql-db)
* [Object Storage](https://console.bluemix.net/catalog/services/object-storage)
* [Watson Natural Language Understanding (NLU)](https://console.bluemix.net/catalog/services/natural-language-understanding)
* [Watson Natural Language Classifier (NLC)](https://console.bluemix.net/catalog/services/natural-language-classifier)
    
## How it works

1. Sources: generic news RSS feeds provided by the user
2. Extraction of keywords and categories for each news using Watson NLU
3. Dataset persisted on Object Storage
4. Creation of a NLC using the generated dataset
5. Classifier training
6. Testing the classifier using _Leave-p-out cross-validation_ 


## Prerequisites

* Download and install Eclipse IDE for Java EE Developers from [here](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/neon3) 


* Install Liberty server in Eclipse from [here](https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse/)


* Create a new Liberty server in your workspace


* Provision the following services on Bluemix
    * [Cloudant NoSQL DB](https://console.bluemix.net/catalog/services/cloudant-nosql-db)
    * [Object Storage](https://console.bluemix.net/catalog/services/object-storage)
    * [Watson Natural Language Understanding](https://console.bluemix.net/catalog/services/natural-language-understanding)
    * [Watson Natural Language Classifier](https://console.bluemix.net/catalog/services/natural-language-classifier)

	

## Deployment

#### Local machine

Add the credentials for your Bluemix services in _com.newsifier.utils.Credentials_

#### On Bluemix

* Export the application .war file

* Add your services to the manifest.yml

* Run the following command from the manifest directory

    
```
cf push -p <exported_application.war>
```

Or see [here](https://console.bluemix.net/docs/runtimes/liberty/optionsForPushing.html#options_for_pushing) other options for pushing Liberty applications on Bluemix.

## Authors

* **Simone Rutigliano** - *Software Engineer* - [LinkedIn](https://www.linkedin.com/in/simonerutigliano/)
* **Umberto Manganiello** - *Software Engineer* - [LinkedIn](www.linkedin.com/in/umanganiello)