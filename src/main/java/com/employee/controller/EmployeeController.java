package com.employee.controller;

import com.employee.entity.Employee;
import com.employee.exception.ResourceNotFoundException;
import com.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping
    public List<Employee> getEmployees() {
        return employeeRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Employee>> getEmployee(@PathVariable Long id) {

        Optional<Employee> employee = employeeRepo.findById(id);

        if(employee.isEmpty()) {
           throw new ResourceNotFoundException("Employee not found for given ID " + id);
        }

        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public Map<String, String> createEmployee(@RequestBody Employee employee)  {

        Map<String, String> response = new HashMap<>();
        response.put("status", "fail");

        try {
            if( employee.getFirstName().isBlank() || employee.getLastName().isBlank() || employee.getEmail().isBlank() ) {
                response.put("message", "All fields are mandatory");
                return response;
            }

            employeeRepo.save(employee);
            response.put("status", "success");
            response.put("message", employee.getFirstName() + " has been created.");
        } catch(Exception e) {
            response.put("message", "Unable to create employee");
            return response;
        }

        return response;
    }

    @PutMapping
    public Map<String, String> updateEmployee(@RequestBody Employee employee)  {

        Map<String, String> response = new HashMap<>();
        response.put("status", "fail");

        try {
            if( employee.getId().toString().isBlank() || employee.getFirstName().isBlank() || employee.getLastName().isBlank() || employee.getEmail().isBlank() ) {
                response.put("message", "All fields are mandatory");
                return response;
            }

            Optional<Employee> existingEmployee = employeeRepo.findById(employee.getId());

            if(existingEmployee.isEmpty()) {
                response.put("message", "Invalid Employee");
                return response;
            }

            employeeRepo.save(employee);
            response.put("status", "success");
            response.put("message", "Employee Updated");
        } catch (Exception e) {
            response.put("message", "Unable to save");
            return response;
        }

        return response;
    }

    @DeleteMapping
    public Map<String, String> deleteEmployee(@RequestBody Employee employee)  {
        Map<String, String> response = new HashMap<>();
        response.put("status", "fail");

        try {
            if( employee.getId().toString().isBlank() ) {
                response.put("message", "All fields are mandatory");
                return response;
            }

            Optional<Employee> existingEmployee = employeeRepo.findById(employee.getId());

            if(existingEmployee.isEmpty()) {
                response.put("message", "Invalid Employee");
                return response;
            }

            employeeRepo.delete(employee);
            response.put("status", "success");
            response.put("message", "Employee Deleted");
        } catch (Exception e) {
            response.put("message", "Unable to delete");
            return response;
        }

        return response;
    }

}
