//package com.yebisu.medusa.controller;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Flux;
//import reactor.test.StepVerifier;
//
//@RunWith(SpringRunner.class)
//@WebFluxTest
//public class RosMessageControllerTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Test
//    public void givenSubscriberWhenGetNamesThenReturnNamesUppercase() {
//
//        Flux<String> namesFlux = webTestClient.get()
//                .uri("/test")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .returnResult(String.class)
//                .getResponseBody();
//
//        StepVerifier.create(namesFlux)
//                .expectSubscription()
//                .expectNext("PASCALFRANCISCOBAYONNE")
//                .verifyComplete();
//    }
//
//
//}
