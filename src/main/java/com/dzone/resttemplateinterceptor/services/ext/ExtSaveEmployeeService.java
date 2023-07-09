package com.dzone.resttemplateinterceptor.services.ext;

import com.dzone.resttemplateinterceptor.model.employee.EmployeeRequest;
import com.dzone.resttemplateinterceptor.model.employee.EmployeeResponse;
import com.dzone.resttemplateinterceptor.services.EmployeeService;
import com.dzone.resttemplateinterceptor.utililty.ExternalApiRestTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;

@Service
public class ExtSaveEmployeeService {

    private String baseUrl = "http://localhost:8080/api/v1";
    private static final Logger logger = LoggerFactory.getLogger(ExtSaveEmployeeService.class);
    @Autowired
    ExternalApiRestTemplateFactory externalApiRestTemplateFactory;

    public EmployeeResponse addEmployee(EmployeeRequest request, String accessToken) {
        EmployeeResponse result = null;
        try {
            Date dt = new Date();
            RestTemplate restTemplate = this.externalApiRestTemplateFactory.getRestTemplate(accessToken, "ADD_EMPLOYEE", "ADD_EMPLOYEE");
            HttpHeaders headers = this.getHttpHeaders(accessToken);
            HttpEntity<EmployeeRequest> entity = new HttpEntity<>(request, headers);
            long startTime = System.currentTimeMillis();
            String endPoint = this.baseUrl + "/employee";
            ResponseEntity<EmployeeResponse> responseEntity = restTemplate.exchange(endPoint,
                    HttpMethod.POST, entity, EmployeeResponse.class);
            result = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
            long timeDifference = endTime - startTime;
            logger.info("Time taken for GDS API call SOFT_PULL/performSoftPull call 1: " + timeDifference);
        } catch (HttpClientErrorException exception) {
            logger.error("HttpClientErrorException occurred while calling ext API, response string: " + exception.getResponseBodyAsString());
            throw exception;
        } catch (HttpStatusCodeException exception) {
            logger.error("HttpStatusCodeException occurred while calling EXT API, response string: " + exception.getResponseBodyAsString());
            throw exception;
        } catch (Exception ex) {
            logger.error("An Error occurred, Detail error:", ex);
            throw ex;
        }
        return result;
    }

    private HttpHeaders getHttpHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
