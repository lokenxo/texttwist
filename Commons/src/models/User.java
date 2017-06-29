package models;

/**
 * Created by loke on 18/06/2017.
 */
public class User {

    public String userName;
    public String password;
    public Integer score = 0;

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public void addScore(Integer score){
        this.score += score;
    }


}
