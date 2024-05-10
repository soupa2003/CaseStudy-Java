package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entity.FinancialRecord;
import entity.Payroll;
import entity.Tax;

public class PayrollManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        try {
            String connectionString = "jdbc:mysql://localhost:3306/payrollmanagementsystem";
            String username = "root";
            String password = "Ajay182003@$";
            connection = DriverManager.getConnection(connectionString, username, password);

            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Add Employee");
                System.out.println("2. GetEmployeeById");
                System.out.println("3. GetAllEmployees");
                System.out.println("4. UpdateEmployee");
                System.out.println("5. RemoveEmployee");
                System.out.println("6. Generate Payroll");
                System.out.println("7. GetPayrollById");
                System.out.println("8. GetPayrollsForEmployee");
                System.out.println("9. GetPayrollsForPeriod");
                System.out.println("10. Calculate Tax"); 
                System.out.println("11. GetTaxById");
                System.out.println("12. GetTaxesForEmployee");
                System.out.println("13. GetTaxesForYear");
                System.out.println("14. Add Financial Record");
                System.out.println("15. GetFinancialRecordById");
                System.out.println("16. GetFinancialRecordsForEmployee");
                System.out.println("17. GetFinancialRecordsForDate");
                System.out.println("18. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        addEmployee(connection, scanner);
                        break;
                    case 2:
                    	getEmployeeById(connection,scanner);
                    	break;
                    case 3:
                    	getAllEmployees(connection);
                    	break;
                    case 4:
                    	updateEmployee(connection,scanner);
                    	break;
                    case 5:
                    	removeEmployee(connection,scanner);
                    	break;
                    case 6:
                        generatePayroll(connection);
                        break;
                    case 7:
                    	getPayrollById(connection,scanner);
                    	break;
                    case 8:
                    	getPayrollsForEmployee(connection,scanner);
                    	break;
                    case 9:
                    	getPayrollsForPeriod(connection,scanner);
                    	break;
                    case 10:
                        calculateTax(connection,scanner);
                        break;
                    case 11:
                    	getTaxById(connection,scanner);
                    	break;
                    case 12:
                    	getTaxesForEmployee(connection,scanner);
                    	break;
                    case 13:
                    	getTaxesForYear(connection,scanner);
                    	break;
                    case 14:
                        addFinancialRecord(connection);
                        break;
                    case 15:
                    	getFinancialRecordById(connection,scanner);
                    	break;
                    case 16:
                    	getFinancialRecordsForEmployee(connection,scanner);
                    	break;
                    case 17:
                    	getFinancialRecordsForDate(connection,scanner);
                    	break;
                    case 18:
                        System.out.println("Exiting application...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    
    private static void getPayrollsForPeriod(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            String startDateStr = scanner.next();
            LocalDate startDate = LocalDate.parse(startDateStr);
            System.out.print("Enter End Date (YYYY-MM-DD): ");
            String endDateStr = scanner.next();
            LocalDate endDate = LocalDate.parse(endDateStr);
            String sql = "SELECT * FROM payroll WHERE payperiodstartdate >= ? AND payperiodenddate <= ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(startDate));
                statement.setDate(2, java.sql.Date.valueOf(endDate));
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Payroll> periodPayrolls = new ArrayList<>();
                    while (resultSet.next()) {
                        Payroll payroll = mapResultSetToPayroll(resultSet);
                        periodPayrolls.add(payroll);
                    }
                    if (!periodPayrolls.isEmpty()) {
                        System.out.println("Payrolls for Period from " + startDate + " to " + endDate);
                        for (Payroll payroll : periodPayrolls) {
                            System.out.println(payroll);
                        }
                    } else {
                        System.out.println("No payrolls found for the specified period.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving payrolls for the period: " + e.getMessage());
        }
    }



	private static void getFinancialRecordsForDate(Connection connection, Scanner scanner) {
    	 System.out.print("Enter record date (yyyy-MM-dd): ");
         String recordDateStr = scanner.next();

         PreparedStatement preparedStatement = null;
         ResultSet resultSet = null;

         try {
             String sql = "SELECT * FROM financialrecord WHERE recordDate = ?";
             preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setString(1, recordDateStr);
             resultSet = preparedStatement.executeQuery();
             while (resultSet.next()) {
                 FinancialRecord financialRecord = new FinancialRecord();
                 financialRecord.setRecordID(resultSet.getInt("recordId"));
                 financialRecord.setEmployeeID(resultSet.getInt("employeeId"));
                 financialRecord.setDescription(resultSet.getString("description"));
                 financialRecord.setAmount(resultSet.getDouble("amount"));
                 financialRecord.setRecordType(resultSet.getString("recordType"));
                 System.out.println("Financial Record:");
                 System.out.println("Record ID: " + financialRecord.getRecordID());
                 System.out.println("Employee ID: " + financialRecord.getEmployeeID());
                 System.out.println("Description: " + financialRecord.getDescription());
                 System.out.println("Amount: " + financialRecord.getAmount());
                 System.out.println("Record Type: " + financialRecord.getRecordType());
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }      
         }
		

	private static void getFinancialRecordsForEmployee(Connection connection, Scanner scanner) {
		System.out.print("Enter employee ID: ");
        int employeeId = scanner.nextInt();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM financialrecord WHERE employeeId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                FinancialRecord financialRecord = new FinancialRecord();
                financialRecord.setRecordID(resultSet.getInt("recordId"));
                financialRecord.setEmployeeID(resultSet.getInt("employeeId"));
                financialRecord.setDescription(resultSet.getString("description"));
                financialRecord.setAmount(resultSet.getDouble("amount"));
                financialRecord.setRecordType(resultSet.getString("recordType"));
                System.out.println("Financial Record:");
                System.out.println("Record ID: " + financialRecord.getRecordID());
                System.out.println("Employee ID: " + financialRecord.getEmployeeID());
                System.out.println("Description: " + financialRecord.getDescription());
                System.out.println("Amount: " + financialRecord.getAmount());
                System.out.println("Record Type: " + financialRecord.getRecordType());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  
    }
		
	



	private static void getFinancialRecordById(Connection connection, Scanner scanner) {
		System.out.print("Enter financial record ID: ");
        int recordId = scanner.nextInt();
        FinancialRecord financialRecord = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM financialrecord WHERE recordId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, recordId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                financialRecord = new FinancialRecord();
                financialRecord.setRecordID(resultSet.getInt("recordId"));
                financialRecord.setEmployeeID(resultSet.getInt("employeeId"));
                financialRecord.setDescription(resultSet.getString("description"));
                financialRecord.setAmount(resultSet.getDouble("amount"));
                financialRecord.setRecordType(resultSet.getString("recordType"));
                System.out.println("Financial Record Found:");
                System.out.println("Record ID: " + financialRecord.getRecordID());
                System.out.println("Employee ID: " + financialRecord.getEmployeeID());
                System.out.println("Description: " + financialRecord.getDescription());
                System.out.println("Amount: " + financialRecord.getAmount());
                System.out.println("Record Type: " + financialRecord.getRecordType());
            } else {
                System.out.println("Financial record with ID " + recordId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  
    }
	
	
	public static void calculateTax(Connection connection, Scanner scanner) throws SQLException {
		System.out.print("Enter employee ID: ");
        int employeeId = scanner.nextInt();
        System.out.print("Enter tax year: ");
        int taxYear = scanner.nextInt();
        double calculatedTax = 0.0;
        System.out.println("Tax calculated for employee ID " + employeeId + " for tax year " + taxYear + ": $" + calculatedTax);
        
    }
  
		


	private static void getTaxesForYear(Connection connection, Scanner scanner) throws SQLException {
		System.out.print("Enter tax year: ");
        int taxYear = scanner.nextInt();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM tax WHERE taxYear = ?")) {
            statement.setInt(1, taxYear);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tax tax = new Tax();
                tax.setTaxID(resultSet.getInt("taxId"));
                tax.setEmployeeID(resultSet.getInt("employeeId"));
                tax.setTaxYear(resultSet.getInt("taxYear"));
                tax.setTaxAmount(resultSet.getDouble("taxamount"));
                System.out.println("Tax ID: " + tax.getTaxID());
                System.out.println("Employee ID: " + tax.getEmployeeID());
                System.out.println("Tax Year: " + tax.getTaxYear());
                System.out.println("Amount: $" + tax.getTaxAmount());
                System.out.println(); 
            }
        }
	
	}



	private static void getTaxesForEmployee(Connection connection, Scanner scanner) throws SQLException {
		System.out.print("Enter employee ID: ");
        int employeeId = scanner.nextInt();
 
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM tax WHERE employeeId = ?")) {
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();
 
            while (resultSet.next()) {
                Tax tax = new Tax();
                tax.setTaxID(resultSet.getInt("taxId"));
                tax.setEmployeeID(resultSet.getInt("employeeId"));
                tax.setTaxYear(resultSet.getInt("taxYear"));
                tax.setTaxAmount(resultSet.getDouble("taxamount"));
 
                System.out.println("Tax ID: " + tax.getTaxID());
                System.out.println("Employee ID: " + tax.getEmployeeID());
                System.out.println("Tax Year: " + tax.getTaxYear());
                System.out.println("Amount: $" + tax.getTaxAmount());
                System.out.println(); 
            }
        }
		
	}



	private static void getTaxById(Connection connection, Scanner scanner) throws SQLException {
		// TODO Auto-generated method stub
		System.out.print("Enter tax ID: ");
        int taxId = scanner.nextInt();
 
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM tax WHERE taxId = ?")) {
            statement.setInt(1, taxId);
            ResultSet resultSet = statement.executeQuery();
 
            if (resultSet.next()) {
                Tax tax = new Tax();
                tax.setTaxID(resultSet.getInt("taxId"));
                tax.setEmployeeID(resultSet.getInt("employeeId"));
                tax.setTaxYear(resultSet.getInt("taxYear"));
                tax.setTaxAmount(resultSet.getDouble("taxamount"));
                System.out.println("Tax ID: " + tax.getTaxID());
                System.out.println("Employee ID: " + tax.getEmployeeID());
                System.out.println("Tax Year: " + tax.getTaxYear());
                System.out.println("Amount: $" + tax.getTaxAmount());
            } else {
                System.out.println("Tax with ID " + taxId + " not found.");
            }
        }
		
	}



	           
 
           
	
		


	private static void getPayrollsForEmployee(Connection connection, Scanner scanner) {
    	    try {
    	        System.out.print("Enter Employee ID: ");
    	        int employeeId = scanner.nextInt();
    	        String sql = "SELECT * FROM payroll WHERE employeeid = ?";
    	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
    	            statement.setInt(1, employeeId);
    	            try (ResultSet resultSet = statement.executeQuery()) {
    	                List<Payroll> employeePayrolls = new ArrayList<>();
    	                while (resultSet.next()) {
    	                    Payroll payroll = mapResultSetToPayroll(resultSet);
    	                    employeePayrolls.add(payroll);
    	                }
    	                if (!employeePayrolls.isEmpty()) {
    	                    System.out.println("Payrolls for Employee ID: " + employeeId);
    	                    for (Payroll payroll : employeePayrolls) {
    	                        System.out.println(payroll);
    	                    }
    	                } else {
    	                    System.out.println("No payrolls found for Employee ID: " + employeeId);
    	                }
    	            }
    	        }
    	    } catch (SQLException e) {
    	        System.out.println("Error retrieving payrolls for employee: " + e.getMessage());
    	    }
    	}

		
	
  	private static void getPayrollById(Connection connection, Scanner scanner) {
          try {
              System.out.print("Enter Payroll ID: ");
              int payrollId = scanner.nextInt();
              String sql = "SELECT * FROM payroll WHERE payrollid = ?";
              try (PreparedStatement statement = connection.prepareStatement(sql)) {
                  statement.setInt(1, payrollId);
                  try (ResultSet resultSet = statement.executeQuery()) {
                      if (resultSet.next()) {
                          Payroll payroll = mapResultSetToPayroll(resultSet);
                          System.out.println("Payroll Details:");
                          System.out.println(payroll);
                      } else {
                          System.out.println("No payroll found with ID: " + payrollId);
                      }
                  }
              }
          } catch (SQLException e) {
              System.out.println("Error retrieving payroll: " + e.getMessage());
          }
      }
      private static Payroll mapResultSetToPayroll(ResultSet resultSet) throws SQLException {
          int payrollId = resultSet.getInt("payrollid");
          int employeeId = resultSet.getInt("employeeid");
          LocalDate startDate = resultSet.getDate("payperiodstartdate").toLocalDate();
          LocalDate endDate = resultSet.getDate("payperiodenddate").toLocalDate();
          double basicSalary = resultSet.getDouble("basicsalary");
          double overtimePay = resultSet.getDouble("overtimepay");
          double deductions = resultSet.getDouble("deductions");
          double netSalary = resultSet.getDouble("netsalary");
          return new Payroll(payrollId, employeeId, startDate, endDate, basicSalary, overtimePay, deductions, netSalary);
      }
		

	private static void removeEmployee(Connection connection, Scanner scanner) {
        System.out.println("Enter the Employee ID of the employee you want to remove:");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        try {
            String selectSql = "SELECT * FROM employee WHERE employeeid = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, employeeId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (!resultSet.next()) {
                    System.out.println("Employee with ID " + employeeId + " does not exist.");
                    return;
                }
            }

            String deleteSql = "DELETE FROM employee WHERE employeeid = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, employeeId);

                int rowsAffected = deleteStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Employee with ID " + employeeId + " removed successfully.");
                } else {
                    System.out.println("Failed to remove employee with ID " + employeeId + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


 
    private static void updateEmployee(Connection connection, Scanner scanner) {
        System.out.println("Enter the Employee ID of the employee you want to update:");
        int employeeId = scanner.nextInt();
        scanner.nextLine();  

        try {
 
            String selectSql = "SELECT * FROM employee WHERE employeeid = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, employeeId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (!resultSet.next()) {
                    System.out.println("Employee with ID " + employeeId + " does not exist.");
                    return;
                }
            }
 
            System.out.println("Enter updated details for the employee (press Enter to skip):");
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String dob = scanner.nextLine();
            System.out.print("Gender: ");
            String gender = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Phone Number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Address: ");
            String address = scanner.nextLine();
            System.out.print("Position: ");
            String position = scanner.nextLine();
            System.out.print("Joining Date (YYYY-MM-DD): ");
            String joiningDate = scanner.nextLine();
            System.out.print("Termination Date (YYYY-MM-DD): ");
            String terminationDate = scanner.nextLine();
 
            String updateSql = "UPDATE employee SET firstname = ?, lastname = ?, dateofbirth = ?, gender = ?, email = ?, phonenumber = ?, address = ?, position = ?, joiningdate = ?, terminationdate = ? WHERE employeeid = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.setString(1, firstName.isEmpty() ? null : firstName);
                updateStatement.setString(2, lastName.isEmpty() ? null : lastName);
                updateStatement.setString(3, dob.isEmpty() ? null : dob);
                updateStatement.setString(4, gender.isEmpty() ? null : gender);
                updateStatement.setString(5, email.isEmpty() ? null : email);
                updateStatement.setString(6, phoneNumber.isEmpty() ? null : phoneNumber);
                updateStatement.setString(7, address.isEmpty() ? null : address);
                updateStatement.setString(8, position.isEmpty() ? null : position);
                updateStatement.setString(9, joiningDate.isEmpty() ? null : joiningDate);
                updateStatement.setString(10, terminationDate.isEmpty() ? null : terminationDate);
                updateStatement.setInt(11, employeeId);

                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Employee with ID " + employeeId + " updated successfully.");
                } else {
                    System.out.println("Failed to update employee with ID " + employeeId + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


	 


	private static void getAllEmployees(Connection connection) {
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        String sql = "SELECT * FROM employee";
	        preparedStatement = connection.prepareStatement(sql);
	        resultSet = preparedStatement.executeQuery();

	        System.out.println("List of Employees:");
	        while (resultSet.next()) {
	            System.out.println("Employee ID: " + resultSet.getInt("employeeid"));
	            System.out.println("First Name: " + resultSet.getString("firstname"));
	            System.out.println("Last Name: " + resultSet.getString("lastname"));
	            System.out.println("Date of Birth: " + resultSet.getObject("dateofbirth", LocalDate.class));
	            System.out.println("Gender: " + resultSet.getString("gender"));
	            System.out.println("Email: " + resultSet.getString("email"));
	            System.out.println("Phone Number: " + resultSet.getString("phonenumber"));
	            System.out.println("Address: " + resultSet.getString("address"));
	            System.out.println("Position: " + resultSet.getString("position"));
	            System.out.println("Joining Date: " + resultSet.getObject("joiningdate", LocalDate.class));
	            System.out.println("Termination Date: " + resultSet.getObject("terminationdate", LocalDate.class));
	            System.out.println();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	
	
	private static void getEmployeeById(Connection connection, Scanner scanner) {
	    System.out.print("Enter employee ID: ");
	    int employeeId = scanner.nextInt();

	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        String sql = "SELECT * FROM employee WHERE employeeid = ?";
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setInt(1, employeeId);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            System.out.println("Employee Found:");
	            System.out.println("Employee ID: " + resultSet.getInt("employeeid"));
	            System.out.println("First Name: " + resultSet.getString("firstname"));
	            System.out.println("Last Name: " + resultSet.getString("lastname"));
	            System.out.println("Date of Birth: " + resultSet.getObject("dateofbirth", LocalDate.class));
	            System.out.println("Gender: " + resultSet.getString("gender"));
	            System.out.println("Email: " + resultSet.getString("email"));
	            System.out.println("Phone Number: " + resultSet.getString("phonenumber"));
	            System.out.println("Address: " + resultSet.getString("address"));
	            System.out.println("Position: " + resultSet.getString("position"));
	            System.out.println("Joining Date: " + resultSet.getObject("joiningdate", LocalDate.class));
	            System.out.println("Termination Date: " + resultSet.getObject("terminationdate", LocalDate.class));
	        } else {
	            System.out.println("Employee with ID " + employeeId + " not found.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	 

	private static void addEmployee(Connection connection, Scanner scanner) throws SQLException {
    	Scanner scanner1 = new Scanner(System.in);
    	System.out.println("Enter employee details:");
    	  System.out.print("First Name: ");
          String firstName = scanner.next();
          System.out.print("Last Name: ");
          String lastName = scanner.next();
          System.out.print("Date of Birth (YYYY-MM-DD): ");
          String dob = scanner.next();
          System.out.print("Gender: ");
          String gender = scanner.next();
          System.out.print("Email: ");
          String email = scanner.next();
          System.out.print("Phone Number: ");
          String phoneNumber = scanner.next();
          System.out.print("Address: ");
          String address = scanner.next();
          System.out.println("Position: ");
          String position = scanner.next();
          System.out.print("Joining Date (YYYY-MM-DD): ");
          String joiningdate = scanner.next();
   
          
          String sql = "INSERT INTO Employee (employeeid,firstname, lastname ,dateofbirth, gender, email, phonenumber, address, position, joiningdate,terminationdate) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
          try (PreparedStatement statement = connection.prepareStatement(sql)) {
        	  int employeeId = generateEmployeeId(connection);
              statement.setInt(1, employeeId);
              statement.setString(2, firstName);
              statement.setString(3, lastName);
              statement.setString(4, dob);
              statement.setString(5, gender);
              statement.setString(6, email);
              statement.setString(7, phoneNumber);
              statement.setString(8, address);
              statement.setString(9, position);
              statement.setString(10, joiningdate);
              statement.setNull(11, java.sql.Types.DATE);
              statement.executeUpdate();
              System.out.println("Employee added successfully.");
          }
    }

   private static int generateEmployeeId(Connection connection) throws SQLException {
        int employeeId = 0;
        String sql = "SELECT MAX(employeeid) FROM employee";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                employeeId = resultSet.getInt(1) + 1;
            } else {
                employeeId = 1;
            }
        }
        return employeeId;
    }


    


  

  
    private static final String PAYROLL_QUERY = "SELECT * FROM payroll";
    public static void generatePayroll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(PAYROLL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Generating Payroll:");

            while (resultSet.next()) {
                int payrollID = resultSet.getInt("payrollid");
                int employeeID = resultSet.getInt("employeeid");
                LocalDate payPeriodStartDate = resultSet.getDate("payperiodstartdate").toLocalDate();
                LocalDate payPeriodEndDate = resultSet.getDate("payperiodenddate").toLocalDate();
                double basicSalary = resultSet.getDouble("basicsalary");
                double overtimePay = resultSet.getDouble("overtimepay");
                double deductions = resultSet.getDouble("deductions");
                double netSalary = basicSalary + overtimePay - deductions;
                Payroll payroll = new Payroll(payrollID, employeeID, payPeriodStartDate, payPeriodEndDate,
                        basicSalary, overtimePay, deductions, netSalary);
                System.out.println(payroll);
            }

            System.out.println("Payroll generation completed.");
        }
    }


 
     
     
    
    
    private static final String INSERT_FINANCIAL_RECORD_QUERY = "INSERT INTO financialrecord (recordid,employeeID, recordDate, description, amount, recordType) VALUES (?,?, ?, ?, ?, ?)";
    public static void addFinancialRecord(Connection connection) throws SQLException {
       
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter financial record details:");
        System.out.print("Record ID: ");
        int recordid = scanner.nextInt();
        System.out.print("Employee ID: ");
        int employeeID = scanner.nextInt();
        System.out.print("Record Date (yyyy-MM-dd): ");
        String recordDateStr = scanner.next();
        LocalDate recordDate = LocalDate.parse(recordDateStr);
        System.out.print("Description: ");
        String description = scanner.next();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Record Type: ");
        String recordType = scanner.next();

    
        try (PreparedStatement statement = connection.prepareStatement(INSERT_FINANCIAL_RECORD_QUERY)) {
        	statement.setInt(1,recordid);
            statement.setInt(2, employeeID);
            statement.setDate(3, java.sql.Date.valueOf(recordDate));
            statement.setString(4, description);
            statement.setDouble(5, amount);
            statement.setString(6, recordType);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Financial record added successfully.");
            } else {
                System.out.println("Failed to add financial record.");
            }
        }
        
    }
  
public static void processPayrollForMultipleEmployees() {
	// TODO Auto-generated method stub
	
}

public static void verifyErrorHandlingForInvalidEmployeeData() {
	// TODO Auto-generated method stub
	
}


public static double calculateGrossSalaryForEmployee(int basicSalary, int overtimePay) {
	// TODO Auto-generated method stub
	 
	    return basicSalary + overtimePay;

}


public static double calculateNetSalaryAfterDeductions(double grossSalary, double deductions) {
	// TODO Auto-generated method stub
double netSalary = grossSalary - deductions;
    
    if (netSalary < 0) {
        return 0;
    }
    
    return netSalary;
}

public static double calculateTaxForHighIncomeEmployee(int highIncome) {
	// TODO Auto-generated method stub
	double taxRate = 0.30; 
    double threshold = 50000;
    if (highIncome > threshold) {
        double taxableIncome = highIncome - threshold;
        return taxableIncome * taxRate;
    } else {
        return 0; 
    }
}

 




 

 
}
