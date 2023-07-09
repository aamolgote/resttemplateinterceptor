package com.dzone.resttemplateinterceptor.controller;

import com.dzone.resttemplateinterceptor.model.employee.EmployeeRequest;
import com.dzone.resttemplateinterceptor.model.employee.EmployeeResponse;
import com.dzone.resttemplateinterceptor.services.ProvisionEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ExampleController {

    @Autowired
    ProvisionEmployeeService provisionEmployeeService;

    @PostMapping("/api/v1/example/provision/employee")
    EmployeeResponse addEmployee(@RequestBody EmployeeRequest newEmployee) {
        return provisionEmployeeService.provisionEmployee(newEmployee);
    }
}
