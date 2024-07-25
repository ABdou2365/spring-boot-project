package com.abde;

import com.abde.customer.Customer;
import com.abde.customer.CustomerRepository;
import com.abde.customer.Gender;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;


@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class);



//        int beanDefinitionNames = applicationContext.getBeanDefinitionCount();
//
//        System.out.println(beanDefinitionNames);



    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository,PasswordEncoder passwordEncoder){
        return args -> {

            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            int age = faker.number().numberBetween(1, 100);
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

            Customer customer1 = new Customer(
                    firstName + " " + lastName
                    , firstName.toLowerCase()+"."+lastName.toLowerCase()+random.nextInt(0,100)+"@abde.com"
                    , passwordEncoder.encode(UUID.randomUUID().toString()), age, gender);
            customerRepository.save(customer1);

        };
    }

}
