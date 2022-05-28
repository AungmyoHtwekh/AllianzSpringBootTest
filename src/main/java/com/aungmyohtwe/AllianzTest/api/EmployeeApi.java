package com.aungmyohtwe.AllianzTest.api;

import com.aungmyohtwe.AllianzTest.constant.Constant;
import com.aungmyohtwe.AllianzTest.model.Employee;
import com.aungmyohtwe.AllianzTest.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.aungmyohtwe.AllianzTest.payload.ResponseStatus;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/employee")
@Api(value = "Employee Information", description = "Employee Information")
public class EmployeeApi {

    private final static Logger log = LoggerFactory.getLogger(com.aungmyohtwe.AllianzTest.api.EmployeeApi.class);
    @Autowired
    EmployeeService employeeService;
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ApiOperation(value = "Add new employee ")
    private ResponseEntity<?> saveEmployee(@RequestBody Employee employee, HttpServletRequest request, HttpServletResponse response){

        try {
            employeeService.saveEmployee(initObject(employee));
            ResponseStatus responseStatus = setResponse(Constant.SUCCESS,Constant.DESCRIPTION_SUCCESS,Constant.SUCCESS_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete employee with id ")
    private ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){

        try {
            Employee employee = employeeService.findById(id);
            if (employee != null){
                employeeService.deleteEmployee(employee);
                ResponseStatus responseStatus = setResponse(Constant.SUCCESS,Constant.DESCRIPTION_DELETE,Constant.SUCCESS_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.OK);
            }else {
                ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.NOT_FOUND,Constant.NOT_FOUND_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "Update employee with id ")
    private ResponseEntity<?> updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){

        try {
            Employee findEmployee = employeeService.findById(id);
            if (findEmployee != null){
                findEmployee.setName(employee.getName());
                findEmployee.setAddress(employee.getAddress());
                findEmployee.setEmail(employee.getEmail());
                findEmployee.setPhoneNo(employee.getPhoneNo());
                employeeService.saveEmployee(findEmployee);
                ResponseStatus responseStatus = setResponse(Constant.SUCCESS,Constant.UPDATED,Constant.SUCCESS_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.CREATED);
            }else {
                ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.NOT_FOUND,Constant.NOT_FOUND_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/get-employee/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve employee with id ", response = Employee.class)
    private ResponseEntity<?> findEmployeeById(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){

        try {
            Employee employee = employeeService.findById(id);
            if (employee != null){
                return new ResponseEntity<>(employee, HttpStatus.OK);
            }else {
                ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.NOT_FOUND,Constant.NOT_FOUND_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve employees ", response = Employee.class)
    private ResponseEntity<?> findAllEmployees(HttpServletRequest request, HttpServletResponse response){

        try {
            List<Employee> employeeList = employeeService.findAll();
            if (employeeList != null){
                return new ResponseEntity<>(employeeList, HttpStatus.OK);
            }else {
                ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.NOT_FOUND,Constant.NOT_FOUND_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    private Employee initObject(Employee employee){
        Employee employeeObj = new Employee();
        if (employee.getId() != null){
            employeeObj.setId(employee.getId());
        }
        employeeObj.setName(employee.getName());
        employeeObj.setEmail(employee.getEmail());
        employeeObj.setAddress(employee.getAddress());
        employeeObj.setPhoneNo(employee.getPhoneNo());
        return employeeObj;
    }

    private ResponseStatus setResponse(String status, String description, String code){
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus(status);
        responseStatus.setDescription(description);
        responseStatus.setCode(code);
        return responseStatus;
    }
}
