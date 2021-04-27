package com.example.company.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.company.dao.DepartmentRepo;
import com.example.company.dao.EmployeeRepo;
import com.example.company.entities.Department;
import com.example.company.entities.Employee;

@Controller
public class HomeController {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private DepartmentRepo departmentRepo;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Company");
		return "home";
	}

	@GetMapping("/employeelist")
	public String showEmployees(Model m) {
		m.addAttribute("title", "View Employee List");
		List<Employee> employees = this.employeeRepo.findAll();
		m.addAttribute("employees", employees);
		return "show_employee_list";
	}

	@GetMapping("/add-employee")
	public String openAddEmployeeForm(Model model) {

		model.addAttribute("title", "Add Employee");

		List<Department> departmentList = this.departmentRepo.findAll();
		model.addAttribute("departmentList", departmentList);

		model.addAttribute("employee", new Employee());
		return "add_employee_form";
	}

	@RequestMapping(value = "/add-employee", method = RequestMethod.POST)
	public String addUser(@Valid @ModelAttribute("employee") Employee employee, BindingResult result1, Model model,
			HttpSession session) {
		try {
			if (result1.hasErrors()) {
				List<Department> departmentList = this.departmentRepo.findAll();
				model.addAttribute("departmentList", departmentList);

				model.addAttribute("employee", employee);
				return "add_employee_form";
			}

			Department department = this.departmentRepo.findByDeptId(employee.getDeptId());
			if (department != null) {
				employee.setDepartment(department);
				this.employeeRepo.save(employee);
				return "redirect:/employeelist";
			}
			return "add_employee_form";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("employee", employee);
			return "add_employee_form";
		}
	}
	
	@GetMapping("/show-employee/{empId}")
	public String showEmployee(@PathVariable("empId") Integer empId, Model model) {
		model.addAttribute("title", "View Employee");
		Employee employee = this.employeeRepo.findByEmpId(empId);
		employee.setDeptId(employee.getDepartment().getDeptId());
		model.addAttribute("employee", employee);
		return "view_employee";
	}

	@PostMapping("/update-employee/{empId}")
	public String openUpdateEmployeeForm(@PathVariable("empId") Integer empId, Model model) {
		model.addAttribute("title", "Update Employee");

		List<Department> departmentList = this.departmentRepo.findAll();
		model.addAttribute("departmentList", departmentList);

		Employee employee = this.employeeRepo.findByEmpId(empId);
		employee.setDeptId(employee.getDepartment().getDeptId());
		model.addAttribute("employee", employee);

		return "update_employee_form";
	}

	@RequestMapping(value = "/update-employee", method = RequestMethod.POST)
	public String updateEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result1,
			Model model, HttpSession session) {
		try {
			Department department = this.departmentRepo.findByDeptId(employee.getDeptId());
			if (department != null) {
				employee.setDepartment(department);
			}

			if (result1.hasErrors()) {
				List<Department> departmentList = this.departmentRepo.findAll();
				model.addAttribute("departmentList", departmentList);

				model.addAttribute("employee", employee);
				return "update_employee_form";
			}

			Employee updateEmployee = this.employeeRepo.findByEmpId(employee.getEmpId());
			updateEmployee.setEmpName(employee.getEmpName());
			updateEmployee.setEmpAge(employee.getEmpAge());
			updateEmployee.setEmpSalary(employee.getEmpSalary());
			updateEmployee.setMobileNo(employee.getMobileNo());
			if (department != null) {
				updateEmployee.setDepartment(department);
				this.employeeRepo.save(updateEmployee);
				return "redirect:/employeelist";
			}
			return "update_employee_form";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("employee", employee);
			return "update_employee_form";
		}
	}

	@GetMapping("/delete-employee/{empId}")
	@Transactional
	public String deleteEmployee(@PathVariable("empId") Integer empId, Model model, HttpSession session,
			Principal principal) {
		Employee employee = this.employeeRepo.findByEmpId(empId);
		this.employeeRepo.delete(employee);
		return "redirect:/employeelist";
	}

	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {
		List<Employee> employees = this.employeeRepo.findByEmpNameContaining(query);
		return ResponseEntity.ok(employees);
	}

}
