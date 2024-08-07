package com.abde.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("jdbc")
public class CustomerJdbcDataAccessService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJdbcDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }


    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id,name,email,password,age,gender FROM customer;
                """;

        return jdbcTemplate.query(sql,customerRowMapper );
    }



    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT id,name,email,password,age,gender FROM customer WHERE id=?;
                """;
        return jdbcTemplate.query(sql,customerRowMapper,id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name,email,password,age,gender) VALUES (?,?,?,?,?);
                """;
        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(),customer.getPassword(), customer.getAge(),customer.getGender().name());
        System.out.println("Customer inserted into database = " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT COUNT(id) FROM customer WHERE email=?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);
        return count != null && count > 0;

    }

    @Override
    public boolean existsPersonWithId(Long id) {
        var sql = """
                SELECT COUNT(id) FROM customer WHERE id=?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE FROM customer WHERE id=?;
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null){
            var sql = """
                     UPDATE customer SET name=? WHERE id=?;
                """;
            jdbcTemplate.update(sql, customer.getName(), customer.getId());
        }

        if(customer.getEmail() != null){
            var sql = """
                    UPDATE customer SET email=? WHERE id=?;
                    """;
            jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
        }
        if(customer.getAge() != null){
            var sql = """
                    UPDATE customer SET age=? WHERE id=?;
                    """;
            jdbcTemplate.update(sql, customer.getAge(), customer.getId());
        }
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        var sql = """
                SELECT id,name,email,password,age,gender FROM customer WHERE email=?;
                """;
        return jdbcTemplate.query(sql, customerRowMapper, email).stream().findFirst();
    }
}
