package com.dzone.resttemplateinterceptor.controller;

import com.dzone.resttemplateinterceptor.model.employee.EmployeeRequest;
import com.dzone.resttemplateinterceptor.model.employee.EmployeeResponse;
import com.dzone.resttemplateinterceptor.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/api/v1/employee")
    EmployeeResponse addEmployee(@RequestBody EmployeeRequest newEmployee) {
        return employeeService.addEmployee(newEmployee);
    }
}
