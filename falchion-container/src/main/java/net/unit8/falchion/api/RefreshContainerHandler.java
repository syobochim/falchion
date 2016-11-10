package net.unit8.falchion.api;

import com.sun.net.httpserver.HttpExchange;
import net.unit8.falchion.Container;
import net.unit8.falchion.JvmPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author kawasima
 */
public class RefreshContainerHandler extends AbstractApi {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshContainerHandler.class);

    public RefreshContainerHandler(Container container) {
        super(container);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Container container = getContainer();
        JvmPool pool = container.getPool();
        // TODO : JSONが来なかったときの処理を考える
        if (Objects.nonNull(container.getBasedir())
                && exchange.getRequestHeaders().get("Content-Type").get(0).equals("application/json")) {
            try (InputStream requestBody = exchange.getRequestBody();
                 JsonReader bodyJsonReader = Json.createReader(new InputStreamReader(requestBody))) {
                String version = bodyJsonReader.readObject().getString("version");
                String classpath = container.createClasspath(container.getBasedir(), version);

                if (!Objects.equals(classpath, container.getBasedir())) {
                    pool.setClasspath(classpath);
                    LOG.info("The version of the application has been changed. New version is '{}'", version);
                }
            }
        }
        pool.refresh();
        sendNoContent(exchange);
    }
}
