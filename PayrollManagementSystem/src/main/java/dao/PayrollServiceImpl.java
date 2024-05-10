package dao;

import entity.Payroll;
import util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayrollServiceImpl implements IPayrollService {
    private Map<Integer, Payroll> payrolls;

    public PayrollServiceImpl() {
        this.payrolls = new HashMap<>();
    }
 
    public void generatePayroll(int employeeId, LocalDate startDate, LocalDate endDate) {
        try (Connection connection = DBConnUtil.getConnection()) {
            
            String sql = "INSERT INTO payroll (employeeid, payperiodstartdate, payperiodenddate, basicsalary, overtimepay, deductions, netsalary) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeId);
            statement.setDate(2, java.sql.Date.valueOf(startDate));
            statement.setDate(3, java.sql.Date.valueOf(endDate));
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Payroll generated successfully for Employee ID: " + employeeId +
                                   " for the period from " + startDate + " to " + endDate);
            } else {
                System.out.println("Failed to generate payroll for Employee ID: " + employeeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            }
    }

    @Override
    public Payroll getPayrollById(int payrollId) {
        return payrolls.get(payrollId);
    }

    @Override
    public List<Payroll> getPayrollsForEmployee(int employeeId) {
        List<Payroll> employeePayrolls = new ArrayList<>();
        for (Payroll payroll : payrolls.values()) {
            if (payroll.getEmployeeID() == employeeId) {
                employeePayrolls.add(payroll);
            }
        }
        return employeePayrolls;
    }

    @Override
    public List<Payroll> getPayrollsForPeriod(LocalDate startDate, LocalDate endDate) {
        List<Payroll> periodPayrolls = new ArrayList<>();
        for (Payroll payroll : payrolls.values()) {
            LocalDate payrollStartDate = payroll.getPayPeriodStartDate();
            LocalDate payrollEndDate = payroll.getPayPeriodEndDate();
            if (payrollStartDate.isEqual(startDate) || payrollStartDate.isAfter(startDate)) {
                if (payrollEndDate.isEqual(endDate) || payrollEndDate.isBefore(endDate)) {
                    periodPayrolls.add(payroll);
                }
            }
        }
        return periodPayrolls;
    }
}