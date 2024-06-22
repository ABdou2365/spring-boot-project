package com.abde.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("abde");
        when(resultSet.getString("email")).thenReturn("abde@gmail.com");
        when(resultSet.getInt("age")).thenReturn(22);

        System.out.println("ID: " + resultSet.getLong("id")); // Should print 1
        System.out.println("Name: " + resultSet.getString("name")); // Should print abde
        System.out.println("Email: " + resultSet.getString("email")); // Should print abde@gmail.com
        System.out.println("Age: " + resultSet.getInt("age"));

        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        Customer expected = new Customer(
                1L, "abde", "abde@gmail.com", 22,
                Gender.MALE);

        assertThat(actual).isEqualTo(expected);
    }
}