package com.bizz.backend.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bizz.backend.exception.ResourceNotFoundException;
import com.bizz.backend.model.Employee;
import com.bizz.backend.repository.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	// Get all employees in REST API
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {

		return employeeRepository.findAll();

	}

	// Create employee REST API
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate));

		/*
		 * long l=System.currentTimeMillis(); Timestamp updatetime=new Timestamp(l); Employee empDataObj=new Employee();
		 * empDataObj.setDob(localDate);
		 */

		return employeeRepository.save(employee);
	}

	// Get employee by id REST API

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

		Employee employee = employeeRepository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id:" + id));

		return ResponseEntity.ok(employee);
	}

	// Update employee REST API
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {

		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id:" + id));

		employee.setEmpId(employeeDetails.getEmpId());
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmailId(employeeDetails.getEmailId());
		employee.setDob(employeeDetails.getDob());

		Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	// Delete employee REST API
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {

		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id:" + id));

		employeeRepository.delete(employee);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);

	}

}
