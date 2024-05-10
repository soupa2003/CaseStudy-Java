 package dao;

import entity.Payroll;
import entity.Tax;
import entity.FinancialRecord;
import java.util.List;

public class ReportGenerator {
    public static void generatePayrollReport(List<Payroll> payrolls) {
        System.out.println("Generating payroll report...");
        for (Payroll payroll : payrolls) {
            System.out.println("Payroll ID: " + payroll.getPayrollID());
        }
        System.out.println("Payroll report generated successfully.");
    }

    public static void generateTaxReport(List<Tax> taxes) 
    {
        System.out.println("Generating tax report...");
        for (Tax tax : taxes) {
            System.out.println("Tax ID: " + tax.getTaxID());
        }
        System.out.println("Tax report generated successfully.");
    }

    public static void generateFinancialRecordReport(List<FinancialRecord> financialRecords) 
    {
        System.out.println("Generating financial record report...");
        for (FinancialRecord record : financialRecords) {
            System.out.println("Record ID: " + record.getRecordID());
        }
        System.out.println("Financial record report generated successfully.");
    }

     
}