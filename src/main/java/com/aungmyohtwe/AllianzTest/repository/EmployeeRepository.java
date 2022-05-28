package com.aungmyohtwe.AllianzTest.repository;

import com.aungmyohtwe.AllianzTest.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
