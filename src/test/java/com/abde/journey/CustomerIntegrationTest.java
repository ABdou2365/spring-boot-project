package com.abde.journey;

import com.abde.customer.Customer;
import com.abde.customer.CustomerRegestrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    private static final String customerURI = "api/v1/customers";

    @Autowired
    private WebTestClient webClient;

    private Random random = new Random();

    @Test
    void canRegisterACustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-"+ UUID.randomUUID() +"@abde.com";
        Integer age = random.nextInt(1,100);
        CustomerRegestrationRequest customerRegistrationRequest =
                new CustomerRegestrationRequest(name, email, age);


        webClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegestrationRequest.class)
                .exchange().expectStatus().isOk();

        List<Customer> responseBody = webClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();

        Customer expectedCustomer = new Customer(name, email, age);

        assertThat(responseBody)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);



        var id = responseBody.stream().filter(c->c.getEmail().equals(email))
                        .map(Customer::getId).findFirst().orElseThrow();

        expectedCustomer.setId(id);

        webClient.get()
                .uri(customerURI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).isEqualTo(expectedCustomer);
    }
}
