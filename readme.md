#### Start mongo as a Docker container and import data

* To get the latest mongo image:

`$ docker pull mongo`
 
* To start mongo on port 27777 

`$ docker run -p 27777:27017 mongo`

* To connect mongo client to the mongo database running on Docker container and run quries on collections etc

`$ mongo --host 127.0.0.1 --port 27777`

* To import data from sample data folder to the mongo database running on the Docker container (tl-test is the db name):
`cd ` to the project root that contains `sample-data` folder and:

`$ mongoimport -d tl-test -c users --port 27777 --jsonArray < sample-data/users.json`

`$ mongoimport -d tl-test -c events --port 27777 --jsonArray < sample-data/events.json`

#### Updating gradle version

Update  `gradle/wrapper/gradle-wrapper.propertes` for the desired gradle version.
For e.g., `distributionUrl=https\://services.gradle.org/distributions/gradle-4.9-bin.zip`

#### REST  API and running the app using gradle and commandline

Change the default port in `application.properties`
`port=${APPLICATION_PORT}`

Currently it is set to `6868` for th app and `27777` to connect to  monogdb.

* To run the tests:

`./gradlew check`

* To run the app via `gradle`:

`./gradlew bootRun`

* To run the fat jar:
`cd build/libs`

`java -jar tl-test-0.0.1-SNAPSHOT.jar --APPLICATION_PORT=6868 --MONGO_HOST=localhost --MONGO_PORT=27777`

#### Running app in container

* To docker image, run `./gradlew buildImage`, which will build image `achalise/tl-test:1.0`. 
The built image can be displayed by executing `docker images`.
This is just a very basic execution of `docker` build command from `gradle` for demo only, and needs to be done 
properly to remove hardcoded names to make it suitable for CI and automation.


* To start the app in Docker container:

`docker run --name tl-test-app -p 6868:6868 achalise/tl-test:1.0`

Since the app needs to communicate to mongo running in different container, we need a bridge network
between the two containers and need to be instantiated using `Docker Compose`. I did not have time to
set up Docker Compose, but since I had played with `kubernetes` in my other project, I was quickly able to 
create `kubernetes` deployment descriptors to run the app and the mongo database in separate pods in kubernetes cluster,
which I thought of including with the project. 

* Running the app in Kubernetes

After `kubectl`, `minikube` has been installed. Run the following:

   `cd ` to project root dir  
   `$ minikube start`  
   `$ eval $(minikube docker-env)` to use local images by reusing `Docker` daemon from `minikube`   
   `$ ./gradlew buildImage` to create the app image and install locally for `Docker` daemon in `minikube`  
   `$ mkdir /data/db/tl-service` to be used as host volume mount for mongodb container to persist data  
   `$ kubectl create -f k8s/mongo-service.yaml` to create mongo service  
   `$ kubectl create -f k8s/app-service.yaml` to create the app service  
   `$ kubectl get pods` to check if all pods are running  
   `$ minikube service tl-service` to launch the browser and point to the url of our app running in the minikube cluster. 

