package com.example.company.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.company.entities.Department;

public interface DepartmentRepo extends JpaRepository<Department, Integer> {

	Department findByDeptId(int deptId);

}
