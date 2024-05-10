import org.junit.Test;
import static org.junit.Assert.*;
import main.PayrollManagement;
import exception.EmployeeNotFoundException;
import exception.PayrollGenerationException;
import exception.TaxCalculationException;
import exception.FinancialRecordException;
import exception.DatabaseConnectionException;

public class UnitTest {

	@Test
	public void testCalculateGrossSalaryForEmployee() {
	    int basicSalary = 5000;
	    int overtimePay = 500;
	    double expectedGrossSalary = 5500.0;
	    assertEquals(expectedGrossSalary, PayrollManagement.calculateGrossSalaryForEmployee(basicSalary, overtimePay), 0.01);
	    assertEquals(expectedGrossSalary, PayrollManagement.calculateGrossSalaryForEmployee(basicSalary, overtimePay), 0.01);
	}

	@Test
	public void testCalculateNetSalaryAfterDeductions() 
	{
	    double grossSalary = 5000;
	    double deductions = 1000;
	    
	    double expectedNetSalary = 4000;
	    double actualNetSalary = PayrollManagement.calculateNetSalaryAfterDeductions(grossSalary, deductions);
	    assertEquals(expectedNetSalary, actualNetSalary, 0.01);
	}


    @Test
    public void testProcessPayrollForMultipleEmployees() throws DatabaseConnectionException, PayrollGenerationException, EmployeeNotFoundException, TaxCalculationException, FinancialRecordException {
		PayrollManagement.processPayrollForMultipleEmployees();
    }

    @Test
    public void testVerifyErrorHandlingForInvalidEmployeeData() throws FinancialRecordException {
		PayrollManagement.verifyErrorHandlingForInvalidEmployeeData();
    }
   
    @Test
    public void testCalculateTaxForHighIncomeEmployee() {
        int lowIncome = 40000;
        double expectedZeroTax = 0.0;
        assertEquals(expectedZeroTax, PayrollManagement.calculateTaxForHighIncomeEmployee(lowIncome), 0.000001d);
    }
    
         
     

     
     
}
