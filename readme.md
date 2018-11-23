### Start mongo as a Docker container

To get the latest mongo image:
 * `$ docker pull mongo`
 
To start mongo on port 27777 
* `$ docker run -p 27777:27017 mongo`

To connect mongo client to the mongo database running on Docker container and run quries on collections etc

* `$ mongo --host 127.0.0.1 --port 27777`

To import data from sample data folder to the mongo database running on the Docker container (tl-test is the db name):
* `$ mongoimport -d tl-test -c users --port 27777 --jsonArray < sample-data/users.json`
* `$ mongoimport -d tl-test -c events --port 27777 --jsonArray < sample-data/events.json`

### Updating gradle version

Update  `gradle/wrapper/gradle-wrapper.propertes` for the desired gradle version.
For e.g., `distributionUrl=https\://services.gradle.org/distributions/gradle-4.9-bin.zip`

### REST  API

Change the default port in `application.properties`
`port=${APPLICATION_PORT}`