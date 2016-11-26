# Fake Http Server

Fake Http Server is actually a real http server controlled by a JavaFX GUI. Its main purpose is to test and mock
REST/WS calls and inspect them in real time. It also can be used to quickly setup a server to deliver static files.

![Main Screen](./docs/server-tumb.png?raw=true)

[Screenshot Main Screen](./docs/server.png?raw=true)

### Features
* Define "Filters" on relative paths to intercept the desired calls
* Return desired status codes
* Return custom defined content (XML, HTML, ...)
* Setup Folder to server the files contained in the folder
* Forward (Proxy) request to the desired endpoint

![Example Filter](./docs/filter-tumb.png?raw=true)

[Screenshot Example Filter](./docs/filter.png?raw=true)

## Technologies used

* [Grizzly framework](https://grizzly.java.net/)
* [Afterburner.fx](https://github.com/AdamBien/afterburner.fx)
* JavaFX / FXML

## Prerequisites

* Maven

Additionally recommended:

* [Netbeans](https://netbeans.org)

## Build and run

Just run mvn install. Start the generated FakeHttpServer-app.jar (dependencies are bundled)
