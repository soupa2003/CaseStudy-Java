 package dao;
import entity.Employee;
import entity.Tax;
import java.util.List;

public interface ITaxService {
    void calculateTax(int employeeId, int taxYear);
    Tax getTaxById(int taxId);
    List<Tax> getTaxesForEmployee(int employeeId);
    List<Tax> getTaxesForYear(int taxYear);
	Employee getEmployeeById(int employeeId);
	List<Employee> getAllEmployees();
	void addEmployee(Employee employeeData);
	void updateEmployee(Employee employeeData);
	void removeEmployee(int employeeId);
}
