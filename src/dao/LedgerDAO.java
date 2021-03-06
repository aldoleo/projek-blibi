package dao;

import model.LedgerModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LedgerDAO {
    public static final String database = "org.postgresql.Driver";
    public static final String url = "jdbc:postgresql://localhost:5432/safirapusparanti?currentSchema=kanmakan";
    public static final String username = "postgres";
    public static final String password = "dbpass";

    Connection conn;

    public LedgerDAO() {

        try {
            Class.forName(database);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("masuk exception dao");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<LedgerModel> getWeeklyLedger() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT SUM(income) AS weekly_income, " +
                "(DATE_TRUNC('week', time_trans) + '6 days'::interval)::DATE AS weekly_date, " +
                "TO_CHAR((DATE_TRUNC('week', time_trans) + '6 days'::interval)::DATE, 'Mon dd, yyyy') AS ledger_name " +
                "FROM ledger " +
                "GROUP BY weekly_date, ledger_name " +
                "ORDER BY weekly_date DESC");
        ResultSet rs = ps.executeQuery();

        List<LedgerModel> weeklyLedger = new ArrayList<LedgerModel>();
        while (rs.next()) {
            weeklyLedger.add(new LedgerModel(null, rs.getFloat(1), rs.getString(3)));
        }
        return weeklyLedger;
    }

    public List<LedgerModel> getMonthlyLedger() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT SUM(income) AS monthly_income, " +
                "DATE_TRUNC('month', time_trans)::DATE AS monthly_date, " +
                "TO_CHAR(time_trans, 'Month yyyy') AS ledger_name " +
                "FROM ledger " +
                "GROUP BY monthly_date, ledger_name " +
                "ORDER BY monthly_date DESC");
        ResultSet rs = ps.executeQuery();

        List<LedgerModel> monthlyLedger = new ArrayList<LedgerModel>();
        while (rs.next()) {
            monthlyLedger.add(new LedgerModel(null, rs.getFloat(1), rs.getString(3)));
        }
        return monthlyLedger;
    }

    public List<LedgerModel> getYearlyLedger() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT SUM(income) AS yearly_income, " +
                "DATE_TRUNC('year', time_trans)::DATE AS yearly_date, " +
                "TO_CHAR(time_trans, 'yyyy') AS ledger_name " +
                "FROM ledger " +
                "GROUP BY yearly_date, ledger_name " +
                "ORDER BY yearly_date DESC");
        ResultSet rs = ps.executeQuery();

        List<LedgerModel> yearlyLedger = new ArrayList<LedgerModel>();
        while (rs.next()) {
            yearlyLedger.add(new LedgerModel(null, rs.getFloat(1), rs.getString(3)));
        }
        return yearlyLedger;
    }
}
