package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public List<Account> getAllAccounts(){

        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<Account>();

        try{
            String sql = "Select * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Account acc = new Account(rs.getInt("accountId"), rs.getString("username"), rs.getString("password"));
                accounts.add(acc);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return accounts;
    }

    public Account insertAccount(Account account){

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            if(isUsernameAvailable(account.getUsername()) && (account.getUsername().length() > 0) && (account.getPassword().length() >= 4)){
                preparedStatement.executeUpdate();
                ResultSet pKey = preparedStatement.getGeneratedKeys();
                if(pKey.next()){
                int generatedID = (int) pKey.getLong(1);
                return new Account(generatedID, account.getUsername(), account.getPassword());
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean isUsernameAvailable(String username){

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "Select * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                return false;
            }
        
            return true;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }
}
