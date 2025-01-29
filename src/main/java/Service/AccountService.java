package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;


public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO dao){
        this.accountDAO = dao;
    }

    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    public boolean checkUsernameAvailability(String username){
        return accountDAO.isUsernameAvailable(username);
    }

    public Account verifyAccountLogin(Account account){
        return accountDAO.verifyLogin(account.getUsername(), account.getPassword());
    }
}
