# Smart lights REST API

This project is an exercise application written for learning how to use [Helidon SE](https://helidon.io/).

> "Helidon is a collection of Java libraries for writing microservices that run on a fast web core powered by Netty."
>
> https://helidon.io/ 

The application provides a REST API for controlling Philips Hue lights and is based on the [Helidon Quickstart SE Example](https://github.com/oracle/helidon/tree/master/examples/quickstarts/helidon-quickstart-se).

## Configuration

All configuration, including the Hue Bridge host and token as well as mappings between light aliases and ids is done in the application.yaml file.

Rename the [application.yaml.example](src/main/resources/application.yaml.example) file to [application.yaml](src/main/resources/application.yaml) to get started. 

The default `application.yaml` file contains the following parts:

```yaml
# Server details:
server:
  port: 8080
  host: 0.0.0.0

# Details of the Hue bridge in your local network:
hue.bridge:
  host: 192.168.100.100
  token: HUE_BRIDGE_API_TOKEN_HERE

# Mappings of custom light aliases to Hue bridge ids:
lights:
  office.id: 1
  livingroom.id: 2
  kitchen.id: 3
```

You will need to fill in your Hue bridge IP and access token (username) in the properties. Instructions on generating the access token can be found at https://developers.meethue.com/develop/get-started-2/.


## Running the application

Execute the `Main.java` on your favorite IDE or build according to [Helidon Quickstart instructions](https://github.com/oracle/helidon/tree/master/examples/quickstarts/helidon-quickstart-se).

`Main.java` consists of a main method, which sets up the server and creates two URL mappings for controlling the lights:

```java
public static void main(final String[] args) throws IOException {
    Config config = Config.create();
    ServerConfiguration serverConfig = ServerConfiguration.create(config.get("server"));

    LightController lights = new LightController(config);

    Routing routes = Routing.builder() //
            .get("/lights/{alias}/on", lights::turnOn) //
            .get("/lights/{alias}/off", lights::turnOff) //
            .build();

    WebServer.create(serverConfig, routes).start();
}
```

## Usage

After light aliases are mapped to Hue bridge ids and the application is started, you can use it for switching the lights on and off. Calls to the URLs can be made using the browser, a custom application or, for example, curl:  

```
$ curl -s -X GET http://localhost:8080/lights/kitchen/on
success

$ curl -s -X GET http://localhost:8080/lights/kitchen/off
success

$ curl -s -X GET http://localhost:8080/lights/office/on
success

$ curl -s -X GET http://localhost:8080/lights/office/off
success
```
