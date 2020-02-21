package api;

import java.io.IOException;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;

public final class Main {

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
}
