package com.example.springwebclient.service;

import com.example.springwebclient.dto.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void addNewEmployee(){
        Employee employee = new Employee(null, 20, "lee", "ui tae", "male", "developer");
        Employee employee1 = employeeRestClient.addNewEmployee(employee);
        assertNotNull(employee1.getId());
    }

    @Test
    void addNewEmployee_BadRequest(){
        Employee employee = new Employee(null, 20, "null", "ui", "male", "developer");
        Assertions.assertThrows(WebClientResponseException.class, ()->employeeRestClient.addNewEmployee(employee));
    }

    @Test
    void updateEmployee(){
        Employee employee = new Employee(null, 30, "lee", "ui tae", "male", "developer");
        Employee updatedEmployee = employeeRestClient.updateEmployee(5,employee);
        assertEquals(30, updatedEmployee.getAge());
    }

    @Test
    void deleteEmployee(){

        String response = employeeRestClient.deleteEmployee(7);
        String expectedMessage = "Employee deleted successfully.";
        assertEquals(expectedMessage, response);
    }
}
