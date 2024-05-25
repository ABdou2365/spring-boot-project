package com.abde;

import com.abde.customer.Customer;
import com.abde.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class);



//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }


    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
//            Customer abde = new Customer("Ad", "abdee@gmail.com", 21);
//
//
//
//            Customer nabil = new Customer( "Nabil", "nabil@gmail.com", 99);
//
//            List<Customer> customers = List.of(abde,nabil);
//
//            customerRepository.saveAll(customers);

        };
    }



}
