package com.duoc.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ApiDocsProxyController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping(path = "/docs-proxy/{service}/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> proxyApiDocs(@PathVariable String service) {
        List<ServiceInstance> instances = discoveryClient.getInstances(service);
        if (instances == null || instances.isEmpty()) {
            return Mono.just(ResponseEntity.status(503).body("{}"));
        }
        String target = instances.get(0).getUri().toString() + "/v3/api-docs";
        return webClientBuilder.build()
                .get()
                .uri(target)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> body.replaceAll("/api/", "/"))
                .map(body -> ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(body))
                .onErrorReturn(ResponseEntity.status(502).body("{}"));
    }
}
