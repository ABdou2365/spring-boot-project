package com.abde;

import com.abde.customer.Customer;
import com.abde.customer.CustomerRepository;
import com.abde.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class);



        int beanDefinitionNames = applicationContext.getBeanDefinitionCount();

//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }
        System.out.println(beanDefinitionNames);



    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {

            Faker faker = new Faker();
            Customer customer1 = new Customer(faker.name().fullName()
                    , faker.internet().safeEmailAddress()
                    , faker.number().numberBetween(1,100), Gender.MALE);
            //customerRepository.save(customer1);

        };
    }

}
