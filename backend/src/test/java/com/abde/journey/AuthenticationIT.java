package com.abde.journey;

import com.abde.Auth.AuthenticationRequest;
import com.abde.Auth.AuthenticationResponse;
import com.abde.customer.CustomerDTO;
import com.abde.customer.CustomerRegestrationRequest;
import com.abde.customer.Gender;
import com.abde.jwt.JWTUtil;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIT {
    private static final String customerURI = "api/v1/customers";
    private static final String authenticationURI = "api/v1/auth";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private JWTUtil jwtUtil;

    private Random random = new Random();

    @Test
    void canLogin() {

        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-"+ UUID.randomUUID() +"@hmida.com";
        Integer age = random.nextInt(1,100);
        String password = "password";
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                email,
                password
        );


        CustomerRegestrationRequest customerRegistrationRequest =
                new CustomerRegestrationRequest(name, email,password, age,gender);


        webClient.post()
                .uri(authenticationURI+"/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange().expectStatus().isUnauthorized();


        //send a post request
        webClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegestrationRequest.class)
                .exchange().expectStatus().isOk();

        EntityExchangeResult<AuthenticationResponse> result = webClient.post()
                .uri(authenticationURI + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String jwtToken = result.getResponseHeaders().get(AUTHORIZATION).get(0);

        AuthenticationResponse responseBody = result.getResponseBody();

        CustomerDTO customerDTO = responseBody.customerDTO();

        assertThat(jwtUtil.isTokenValid(jwtToken, customerDTO.username())).isTrue();


        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.username()).isEqualTo(email);
        assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.gender()).isEqualTo(gender);
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));

    }
}
