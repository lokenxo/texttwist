package models;

/**
 * Created by loke on 17/06/2017.
 */
public class Session {

    public String token;
    public User account;

    public Session(User account, String token){
        this.token = token;
        this.account = account;
    }

}
