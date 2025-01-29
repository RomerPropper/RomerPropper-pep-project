package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> getAllMessages(){

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();

        try{
            String sql = "Select * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public List<Message> getAllMessagesFromUser(int id){

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();

        try{
            String sql = "Select * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message insertMessage(Message message){

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            if(doesUserExist(message.getPosted_by()) && (message.getMessage_text().length() > 0) && (message.getMessage_text().length() < 256)){
                    preparedStatement.executeUpdate();
                    ResultSet pKey = preparedStatement.getGeneratedKeys();
                    if(pKey.next()){
                    int generatedID = (int) pKey.getLong(1);
                    return new Message(generatedID, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void updateMessage(int message_id, String updated_text){

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1, updated_text);
            preparedStatement.setInt(2, message_id);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean doesUserExist(int id){

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "Select * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                return true;
            }
        
            return false;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Message getMessageByID(int id){

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "Select * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        
            return null;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessageByID(int id){

        Connection connection = ConnectionUtil.getConnection();
        try{
            
            Message messageToDelete = getMessageByID(id);
            if(messageToDelete != null){

                String sql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

                return messageToDelete;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
    
}
