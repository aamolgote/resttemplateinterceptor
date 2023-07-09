package com.dzone.resttemplateinterceptor.services;

import com.dzone.resttemplateinterceptor.model.employee.EmployeeRequest;
import com.dzone.resttemplateinterceptor.model.employee.EmployeeResponse;
import com.dzone.resttemplateinterceptor.services.ext.ExtSaveEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProvisionEmployeeService {
    @Autowired
    ExtSaveEmployeeService extSaveEmployeeService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeResponse provisionEmployee(EmployeeRequest employeeRequest){
        EmployeeResponse employeeResponse = null;
        try{
            //DO additional things for provisioning employee

            //Ideally access token would be fetched from the header.
            String accessToken = UUID.randomUUID().toString();

            //Now add employee by call external API
            employeeResponse = extSaveEmployeeService.addEmployee(employeeRequest, accessToken);
        } catch (Exception ex){
            logger.error("An error occurred detail error : ", ex);
        }
        return employeeResponse;
    }
}
