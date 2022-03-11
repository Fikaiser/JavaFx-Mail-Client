/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;


import hr.algebra.dal.DataSourceSingleton;
import hr.algebra.dal.Repository;
import hr.algebra.model.Account;
import hr.algebra.model.Email;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author RnaBo
 */
public class SqlRepo implements Repository {

    private static final String REGISTER = "{ CALL RegisterUser (?,?,?) }";
    private static final String CHECK = "{ CALL CheckAccount (?,?) }";
    private static final String GETLOGIN = "{ CALL LogInUser (?,?) }";
    private static final String GETINBOX = "{ CALL GetUserInbox (?) }";
    private static final String GETOUTBOX = "{ CALL GetUserOutbox (?) }";
    private static final String GETRECIPIENTS = "{ CALL GetRecipients (?) }";
    private static final String SENDEMAIL = "{ CALL AddEmail (?,?,?,?) }";
    private static final String ADDRECIPIENT = "{ CALL AddRecipient (?,?) }";

    @Override
    public Optional<Account> getLogin(String email, String pass) throws Exception {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GETLOGIN)) {

            stmt.setString(1, email);
            stmt.setString(2, pass);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Account(rs.getInt(1), rs.getString(2), rs.getString(3)));
                }
            }
        }
        return Optional.empty();

    }

    @Override
    public void registerLogin(String email, String pass, String nickname) throws Exception {
        System.out.println("");
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(REGISTER)) {

            stmt.setString(1, email);
            stmt.setString(2, pass);
            stmt.setString(3, nickname);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean checkAccountExists(String email) throws Exception {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CHECK)) {

            stmt.setString(1, email);
            stmt.registerOutParameter(2, Types.BOOLEAN);

            stmt.executeUpdate();

            return stmt.getBoolean(2);

        }
    }

    @Override
    public List<Email> getInbox(String emailAdresse) throws Exception {

        List<Email> emails = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GETINBOX)) {

            stmt.setString(1, emailAdresse);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    emails.add(new Email(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }

            }
        }
        return emails;
    }

    @Override
    public List<Email> getOutbox(String emailAdresse) throws Exception {

        List<Email> emails = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GETOUTBOX)) {

            stmt.setString(1, emailAdresse);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    emails.add(new Email(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }

            }
        }
        return emails;

    }

    @Override
    public String getRecipients(int idEmail) throws Exception {

        StringBuilder sb = new StringBuilder("");

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GETRECIPIENTS)) {

            stmt.setInt(1, idEmail);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    sb.append(rs.getString(1)).append("; ");
                }

            }
        }

        return sb.toString();
    }

    @Override
    public void sendEmail(String sender, String recipient, String title, String content) throws SQLException {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SENDEMAIL)) {

            stmt.setString(1, sender);
            stmt.setString(2, title);
            stmt.setString(3, content);
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.executeUpdate();

            int emailId = stmt.getInt(4);

            addRecipient(emailId, recipient);

        }

    }

    private void addRecipient(int emailId, String recipient) throws SQLException {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(ADDRECIPIENT)) {

            stmt.setInt(1, emailId);
            stmt.setString(2, recipient);
            stmt.executeUpdate();
        }

    }

}
