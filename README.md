# Newsifier

Application that uses IBM Watson services to create, train and test a Classifier of news.

## Bluemix Services used

* [Cloudant NoSQL DB](https://console.bluemix.net/catalog/services/cloudant-nosql-db)
* [Object Storage](https://console.bluemix.net/catalog/services/object-storage)
* [Watson Natural Language Understanding (NLU)](https://console.bluemix.net/catalog/services/natural-language-understanding)
* [Watson Natural Language Classifier (NLC)](https://console.bluemix.net/catalog/services/natural-language-classifier)
    
## How it works

1. Sources: generic news RSS feeds provided by the user
* Extraction of keywords and categories for each news using Watson NLU
* Dataset persisted on Object Storage
* Creation of a NLC using the generated dataset
* Training of the classifier
* Testing the classifier using _Leave-p-out cross-validation_ 


## Prerequisites

* Download and install Eclipse IDE for Java EE Developers from [here](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/neon3) 


* Install Liberty server in Eclipse from [here](https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse/)


* Create a new Liberty server in your workspace


* Provision the following services on Bluemix
    * [Cloudant NoSQL DB](https://console.bluemix.net/catalog/services/cloudant-nosql-db)
    * [Object Storage](https://console.bluemix.net/catalog/services/object-storage)
    * [Watson Natural Language Understanding](https://console.bluemix.net/catalog/services/natural-language-understanding)
    * [Watson Natural Language Classifier](https://console.bluemix.net/catalog/services/natural-language-classifier)

	

### Installing

```
git clone
```

#### Deployment

# Local machine

Add the credentials for your Bluemix services in _com.newsifier.utils.Credentials_

# On Bluemix

* Export the application .war file

* Add your services to the manifest.yml

* Run the following command from the manifest directory

    
```
cf push -p <path_to_exported_app.war>
```

## Authors

* **Simone Rutigliano** - *Software Engineer* - [LinkedIn](https://www.linkedin.com/in/simonerutigliano/)
* **Umberto Manganiello** - *Software Engineer* - [LinkedIn](www.linkedin.com/in/umanganiello)