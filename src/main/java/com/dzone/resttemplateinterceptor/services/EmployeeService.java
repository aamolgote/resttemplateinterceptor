package com.dzone.resttemplateinterceptor.services;

import com.dzone.resttemplateinterceptor.model.employee.EmployeeRequest;
import com.dzone.resttemplateinterceptor.model.employee.EmployeeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    public EmployeeResponse  addEmployee(EmployeeRequest employeeRequest){
        EmployeeResponse employeeResponse = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            String requestPayLoad = mapper.writeValueAsString(employeeRequest);
            logger.info("Request Received :" + requestPayLoad);
            employeeResponse = new EmployeeResponse();
            employeeResponse.setMessage("Employee Added");
            employeeResponse.setResult(true);
        } catch (Exception ex){
            logger.error("An error occurred detail error : ", ex);
        }
        return employeeResponse;
    }
}
