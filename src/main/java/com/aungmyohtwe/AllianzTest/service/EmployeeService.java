package com.aungmyohtwe.AllianzTest.service;

import com.aungmyohtwe.AllianzTest.model.Employee;

import java.util.List;

public interface EmployeeService {
    void saveEmployee(Employee employee);
    void deleteEmployee(Employee employee);
    Employee findById(Long id);
    List<Employee> findAll();
}
