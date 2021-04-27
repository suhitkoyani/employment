package com.example.company.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.company.entities.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

	Employee findByEmpId(int empId);

	List<Employee> findByEmpNameContaining(String query);

}
