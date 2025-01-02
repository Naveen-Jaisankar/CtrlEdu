package com.ctrledu.CommonService.utilities;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class ServiceCommunicator {

    private final WebClient webClient;

    public ServiceCommunicator(@LoadBalanced WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public <T> Mono<T> sendPostRequest(String baseUrl, String uri, Object requestBody, Class<T> responseType) {
        System.out.println("Sending POST request to: " + baseUrl + uri);
        System.out.println("Payload: " + requestBody);
        return webClient.post()
                .uri(baseUrl + uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> sendGetRequest(String baseUrl, String uri, Class<T> responseType) {
        return webClient.get()
                .uri(baseUrl + uri)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> sendPostRequestWithRetry(String baseUrl, String uri, Object requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(baseUrl + uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .timeout(Duration.ofSeconds(10));
    }

}


