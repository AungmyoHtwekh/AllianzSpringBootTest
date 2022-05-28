package com.aungmyohtwe.AllianzTest.service.impl;

import com.aungmyohtwe.AllianzTest.model.Employee;
import com.aungmyohtwe.AllianzTest.repository.EmployeeRepository;
import com.aungmyohtwe.AllianzTest.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final static Logger log = LoggerFactory.getLogger(com.aungmyohtwe.AllianzTest.service.impl.EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.saveAndFlush(employee);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public Employee findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.get();
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList;
    }
}
