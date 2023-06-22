package com.loan.hero;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testDatabaseConnection() {
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource("jdbc:postgresql://localhost:5432/hero_man");
        try {
            Connection connection = dataSource.getConnection("postgres", "password");
            System.out.println(connection);
            assertThat(connection).isNotNull();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
