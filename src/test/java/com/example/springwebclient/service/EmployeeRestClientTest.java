package com.example.springwebclient.service;

import com.example.springwebclient.dto.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeRestClientTest {
    private static final String baseUrl = "http://localhost:8081/employeeservice";
    private WebClient webClient = WebClient.create(baseUrl);

    EmployeeRestClient employeeRestClient = new EmployeeRestClient(webClient);

    @Test
    void retrieveAllEmployees(){
        List<Employee> employeeList = employeeRestClient.retrieveAllEmployees();
        assertTrue(employeeList.size()>0);
    }

    @Test
    void retrieveEmployee(){
        int employeeId = 1;
        Employee employee = employeeRestClient.retrieveEmployeeById(employeeId);
        assertEquals("Chris", employee.getFirstName());
    }

    @Test
    void retrieveEmployee_notFound(){
        int employeeId = 10;
        Assertions.assertThrows(WebClientResponseException.class, ()->employeeRestClient.retrieveEmployeeById(employeeId));
    }

    @Test
    void retrieveEmployeeByName(){
        String name = "Chris";
        List<Employee> employees = employeeRestClient.retrieveEmployeeByName(name);
        assertTrue(employees.size()>0);
    }

    @Test
    void retrieveEmployeeByName_notFound(){
        String name = "aaaa";
        Assertions.assertThrows(WebClientResponseException.class, ()->employeeRestClient.retrieveEmployeeByName(name));
    }
}
