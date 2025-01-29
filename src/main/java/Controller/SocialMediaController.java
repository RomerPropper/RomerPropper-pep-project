package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {

        Javalin app = Javalin.create();
        
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIDHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);

        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::postMessageHandler);

        app.patch("messages/{message_id}", this::updateMessageHandler);

        app.delete("messages/{message_id}", this::deleteMesageByIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    private void registerHandler(Context ctx) throws JsonProcessingException{
        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount)).status(200);
        }
        else{
            ctx.status(400);
        }

    }

    private void loginHandler(Context ctx) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account verifiedAccount = accountService.verifyAccountLogin(account);
        if(verifiedAccount != null){
            ctx.json(mapper.writeValueAsString(verifiedAccount)).status(200);
        }
        else{
            ctx.status(401);
        }
        
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message addedMessage = messageService.postMessage(message);
        if(addedMessage != null){
            ctx.json(mapper.writeValueAsString(addedMessage)).status(200);
        }
        else{
            ctx.status(400);
        }

    }

    private void getAllMessagesHandler(Context ctx) {

        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);

    }

    private void getAllMessagesFromUserHandler(Context ctx){

        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesFromUser(id);
        ctx.json(messages);

    }

    private void getMessageByIDHandler(Context ctx) {

        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if(message != null){
            ctx.json(message);
        }

    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        Message updatedMessage = messageService.updateMessage(id, message.getMessage_text());

        if(updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage)).status(200);
        }
        else{
            ctx.status(400);
        }

    }

    private void deleteMesageByIDHandler(Context ctx){

        int id = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = messageService.deleteMessageByID(id);

        if(deletedMessage != null){
            ctx.json(deletedMessage).status(200);
        }
        else{
            ctx.status(200);
        }
        
    }

}