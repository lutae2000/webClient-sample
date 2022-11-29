package com.example.springwebclient.service;

import com.example.springwebclient.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.example.springwebclient.constants.EmployeeConstants.*;

@Slf4j
public class EmployeeRestClient {


    private WebClient webClient;

    public EmployeeRestClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public List<Employee> retrieveAllEmployees(){
        //http://localhost:8081/employeeservice/v1/allEmployees
        return webClient.get()
                .uri(GET_ALL_EMPLOYEES_V1)
                .retrieve()
                .bodyToFlux(Employee.class)
                .collectList()
                .block();
    }
    
    //http://localhost:8081/employeeservice/v1/employee/1
    //PathVariable 사용할 때
    public Employee retrieveEmployeeById(int employeeId){

        try{
            return webClient.get()
                    .uri(EMPLOYEE_BY_ID_V1,employeeId)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        } catch (WebClientResponseException ex){
            log.error("Error response code is {}  and the response body is {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeById ", ex);
            throw ex;
        } catch (Exception ex){
            log.error("Exception in retrieveEmployeeById ", ex);
            throw ex;
        }

    }
    
    //http://localhost:8081/employeeservice/v1/employeeName?employee_name=chris
    //Param 사용할 때
    public List<Employee> retrieveEmployeeByName(String employeeName){

        String uri = UriComponentsBuilder.fromUriString(EMPLOYEE_BY_NAME_v1)
                                        .queryParam("employee_name", employeeName)
                                        .build()
                                        .toUriString();

        try{
            return webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToFlux(Employee.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException ex){
            log.error("Error response code is {}  and the response body is {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeById ", ex);
            if(ex.getStatusCode().is4xxClientError()){
                log.error("employee is not found");
            }
            throw ex;
        } catch (Exception ex){
            log.error("Exception in retrieveEmployeeById ", ex);
            throw ex;
        }
    }

    public Employee addNewEmployee(Employee employee){
        try{
            return webClient.post()
                    .uri(ADD_NEW_EMPLOYEE_v1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(employee)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        } catch (WebClientResponseException ex){
            log.error("Error response code is {}  and the response body is {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeById ", ex);
            throw ex;
        } catch (Exception ex){
            log.error("Exception in retrieveEmployeeById ", ex);
            throw ex;
        }
    }

    public Employee updateEmployee(int employeeId, Employee employee){
        try{
            return webClient.put().uri(EMPLOYEE_BY_ID_V1,employeeId)
                    .bodyValue(employee)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        } catch (WebClientResponseException ex){
            log.error("Error response code is {}  and the response body is {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeById ", ex);
            throw ex;
        } catch (Exception ex){
            log.error("Exception in retrieveEmployeeById ", ex);
            throw ex;
        }
    }

    public String deleteEmployee(int employeeId){
        try{
            return webClient.delete()
                    .uri(EMPLOYEE_BY_ID_V1, employeeId)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.is4xxClientError()
                                        || httpStatus.is5xxServerError()
                            , clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new RuntimeException(body))
                    )
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException ex){
            log.error("Error response code is {}  and the response body is {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeById ", ex);
            throw ex;
        } catch (Exception ex){
            log.error("Exception in retrieveEmployeeById ", ex);
            throw ex;
        }
    }
}
