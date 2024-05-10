 package dao;

import entity.Employee;

public class ValidationService {
    public static boolean validateEmployeeData(Employee employee) {
        if (employee.getFirstName() == null || employee.getFirstName().isEmpty()) {
            return false;
        }
        if (employee.getLastName() == null || employee.getLastName().isEmpty()) {
            return false;
        }
        if (employee.getDateOfBirth() == null) {
            return false;
        }
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            return false;
        }
        return true;
    }
 
}