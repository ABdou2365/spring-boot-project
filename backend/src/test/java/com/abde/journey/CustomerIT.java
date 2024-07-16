package com.abde.journey;

import com.abde.customer.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {
    private static final String customerURI = "api/v1/customers";

    @Autowired
    private WebTestClient webClient;

    private Random random = new Random();

    @Test
    void canRegisterACustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-"+ UUID.randomUUID() +"@hmida.com";
        Integer age = random.nextInt(1,100);
        String password = "password";
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;


        CustomerRegestrationRequest customerRegistrationRequest =
                new CustomerRegestrationRequest(name, email,password, age,gender);


        //send a post request
        String jwtToken = webClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegestrationRequest.class)
                .exchange().expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);


        //get all customers
        List<CustomerDTO> allCustomers = webClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                }).returnResult().getResponseBody();




        var id = allCustomers.stream().filter(c->c.email().equals(email))
                        .map(c->c.id()).findFirst().orElseThrow();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email
        );

        assertThat(allCustomers)
                .contains(expectedCustomer);

        webClient.get()
                .uri(customerURI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                }).isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteACustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-"+ UUID.randomUUID() +"@abde.com";
        String password = "password";
        Integer age = random.nextInt(1,100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegestrationRequest customerRegistrationRequest =
                new CustomerRegestrationRequest(name, email,password, age,gender);

        CustomerRegestrationRequest customerRegistrationRequest2 =
                new CustomerRegestrationRequest(name, email+".uk",password , age,gender);

        //Create customer1
        webClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegestrationRequest.class)
                .exchange().expectStatus().isOk();

        //Create customer2
        String jwtToken = webClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest2), CustomerRegestrationRequest.class)
                .exchange().expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        List<CustomerDTO> responseBody = webClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                }).returnResult().getResponseBody();

        var id = responseBody.stream().filter(c->c.email().equals(email))
                .map(c->c.id()).findFirst().orElseThrow();

        webClient.delete()
                .uri(customerURI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isOk();


        webClient.get()
                .uri(customerURI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void canUpdateACustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-"+ UUID.randomUUID() +"@abde.com";
        String password = "password";
        Integer age = random.nextInt(1,100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegestrationRequest customerRegistrationRequest =
                new CustomerRegestrationRequest(name, email,password, age,gender);


        String jwtToken = webClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegestrationRequest.class)
                .exchange().expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        List<CustomerDTO> responseBody = webClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                }).returnResult().getResponseBody();

        var id = responseBody.stream().filter(c->c.email().equals(email))
                .map(CustomerDTO::id).findFirst().orElseThrow();

        String newName = "name";
        CustomerUpdateRequest updatedRequest =
                new CustomerUpdateRequest(newName, null, null,null,null);

        webClient.put()
                .uri(customerURI + "/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedRequest), CustomerUpdateRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isOk();

        CustomerDTO updated = webClient.get()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION,String.format("Bearer %s ",jwtToken))
                .exchange().expectStatus().isOk()
                .expectBody(CustomerDTO.class).returnResult().getResponseBody();

        CustomerDTO expected = new CustomerDTO(id,newName, email,gender, age, List.of("ROLE_USER") ,email);

        assertThat(updated).isEqualTo(expected);

    }
}
