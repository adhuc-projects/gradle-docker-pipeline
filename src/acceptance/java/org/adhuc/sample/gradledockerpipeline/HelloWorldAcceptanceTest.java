package org.adhuc.sample.gradledockerpipeline;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class HelloWorldAcceptanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldAcceptanceTest.class);

    static final String SERVICE_NAME = "gradle-docker-pipeline";
    static final String EXPOSED_PORT = "8080";
    static final String PORT_PROPERTY_NAME = SERVICE_NAME + ".tcp." + EXPOSED_PORT;

    @Test
    public void callHelloWorld() {
        String port = System.getProperty(PORT_PROPERTY_NAME);
        LOGGER.info("Call hello world with port {}", port);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello World!", response.getBody());
    }

}
