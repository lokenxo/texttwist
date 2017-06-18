package models;

/**
 * Created by loke on 17/06/2017.
 */
public class Session {
    public String userName;
    public String token;

    public Session(String userName, String token){
        this.userName = userName;
        this.token = token;
    }
}
