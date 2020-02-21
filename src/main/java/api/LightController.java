package api;

import java.util.function.Supplier;

import io.helidon.config.Config;
import io.helidon.webserver.NotFoundException;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

public class LightController {
    private static final String SUCCESS_MSG = "success";
    private static final String ERROR_MSG = "error";
    private static final Supplier<NotFoundException> LIGHT_NOT_FOUND = () -> new NotFoundException("Light not found");

    private Config config;

    public LightController(Config config) {
        this.config = config;
    }

    public void turnOn(ServerRequest req, ServerResponse res) {
        SmartLight light = getLight(req);

        boolean success = light.on();
        res.send(success ? SUCCESS_MSG : ERROR_MSG);
    }

    public void turnOff(ServerRequest req, ServerResponse res) {
        SmartLight light = getLight(req);

        boolean success = light.off();
        res.send(success ? SUCCESS_MSG : ERROR_MSG);
    }

    private SmartLight getLight(ServerRequest req) {
        // maps the user friendly alias from the path to the generated light id from
        // configuration:
        String alias = req.path().param("alias");
        String id = config.get("lights." + alias + ".id").asString().orElseThrow(LIGHT_NOT_FOUND);

        return new SmartLight(id, alias, config);
    }
}
