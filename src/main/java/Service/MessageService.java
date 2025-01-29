package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO dao){
        this.messageDAO = dao;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesFromUser(int id){
        return messageDAO.getAllMessagesFromUser(id);
    }

    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    public Message postMessage(Message message){
        return messageDAO.insertMessage(message);
    }

    public Message updateMessage(int message_id, String updated_text){

        if((updated_text.length() == 0) || (updated_text.length() >= 256) || (messageDAO.getMessageByID(message_id) == null)){
            return null;
        }

        messageDAO.updateMessage(message_id, updated_text);

        return messageDAO.getMessageByID(message_id);
    }
    
    public Message deleteMessageByID(int id){
        return messageDAO.deleteMessageByID(id);
    }
}
