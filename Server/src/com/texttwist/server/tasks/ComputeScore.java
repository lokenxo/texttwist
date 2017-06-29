package com.texttwist.server.tasks;
import com.texttwist.client.App;
import com.texttwist.server.components.AccountsManager;
import com.texttwist.server.models.Dictionary;
import com.texttwist.server.models.Match;
import models.User;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 27/06/2017.
 */
public class ComputeScore implements Callable<Integer> {

    public DefaultListModel<String> words;
    public String sender;
    public Match match;

    public ComputeScore(String sender, Match match, DefaultListModel<String> words){
        this.words = words;
        this.sender = sender;
        this.match = match;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("SET SCORE");
        Integer score = 0;
        for(int i = 0; i< words.size(); i++){
            if(isValid(words.get(i), match.letters)){
                score += words.get(i).length();
            }
        }
        match.setScore(sender, score);
        User u = AccountsManager.getInstance().findUser(sender);
        u.addScore(score);
        return score;
    }

    private Boolean isValid(String word, DefaultListModel<String> letters) {
        for ( int i =0 ; i< word.length(); i++){
            String c = Character.toString(word.charAt(i));
            Boolean isCharacterPresent = false;
            for(int j =0 ; j< letters.size(); j++){
                if(c.equals(letters.get(j))){
                    isCharacterPresent = true;
                }
            }
            if(!isCharacterPresent){
                return false;
            }
        }
        return Dictionary.isContainedInDictionary(word);
    }

}
