package dao;
import entity.Employee;
import entity.Tax;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxServiceImpl implements ITaxService {
    private Map<Integer, List<Tax>> taxesByEmployee;
    private Map<Integer, Tax> taxesById;

    public TaxServiceImpl() {
        this.taxesByEmployee = new HashMap<>();
        this.taxesById = new HashMap<>();
    }

    @Override
    public void calculateTax(int employeeId, int taxYear) {
        System.out.println("Placeholder implementation for calculating tax for Employee ID: " + employeeId +
                " for the tax year: " + taxYear);
    }

    @Override
    public Tax getTaxById(int taxId) {
        return taxesById.get(taxId);
    }

    @Override
    public List<Tax> getTaxesForEmployee(int employeeId) {
        return taxesByEmployee.getOrDefault(employeeId, new ArrayList<>());
    }

    @Override
    public List<Tax> getTaxesForYear(int taxYear) {
        List<Tax> taxesForYear = new ArrayList<>();
        for (List<Tax> taxes : taxesByEmployee.values()) {
            for (Tax tax : taxes) {
                if (tax.getTaxYear() == taxYear) {
                    taxesForYear.add(tax);
                }
            }
        }
        return taxesForYear;
    }

	@Override
	public Employee getEmployeeById(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEmployee(Employee employeeData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEmployee(Employee employeeData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEmployee(int employeeId) {
		// TODO Auto-generated method stub
		
	}
}