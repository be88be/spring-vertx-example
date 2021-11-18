package com.be88be.example.vertx;






import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class VertxWebClient {
	@Value("${http.client.ssl}") 
	private Boolean sslYn;
	@Value("${http.client.timeout}")
	private int timeout;
	
	private WebClient webClient;
	
    public WebClient createWebClient(Vertx vertx, int port, String host) {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setSsl(sslYn);
        webClientOptions.setDefaultHost(host);
        webClientOptions.setDefaultPort(port);
        webClientOptions.setConnectTimeout(timeout);
        
        webClient = WebClient.create(vertx, webClientOptions);
        return webClient;
      }
    
    @PreDestroy
    private void close() throws Exception {
    	if (webClient != null) {
    		webClient.close();
    		log.info("Shutdown webClient completed.");
    	}
    }
}
