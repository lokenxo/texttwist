package com.texttwist.server.tasks;

import com.texttwist.server.models.Accounts;
import com.texttwist.server.models.Dictionary;
import com.texttwist.server.models.Match;
import models.User;
import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Task: Ccmpute Score
 */
public class ComputeScore implements Callable<Integer> {

    private DefaultListModel<String> words;
    private final String sender;
    public Match match;
    private DefaultListModel<String> wordsValid = new DefaultListModel<>();

    public ComputeScore(String sender, DefaultListModel<String> words, Match match){
        this.words = words;
        this.match = match;
        this.sender = sender;
    }

    @Override
    public Integer call() throws Exception {
        Integer score = 0;
        for (int i = 0; i < words.size(); i++) {
            if (isValid(words.get(i), match.letters)) {
                score += words.get(i).length();
                wordsValid.addElement(words.get(i));
            }
        }
        match.setScore(sender, score);

        User u = Accounts.getInstance().findUser(sender);
        u.addScore(score);

        if(match.allPlayersSendedHisScore()) {
            match.matchTimeout = false;
            match.setUndefinedScorePlayersToZero();
            new SendScores(match).call();
        }
        return score;
    }

    private Boolean isValid(String word, DefaultListModel<String> letters) {
        for (int i =0 ; i< word.length(); i++){
            String c = Character.toString(word.charAt(i));
            Boolean isCharacterPresent = false;
            for(int j =0 ; j< letters.size(); j++){
                if(c.equals(letters.get(j))){
                    isCharacterPresent = true;
                }
            }
            if(word.equals("")){
                return true;
            }
            if(!isCharacterPresent || wordsValid.indexOf(word)!=-1){
                return false;
            }
        }
        return Dictionary.isContainedInDictionary(word);
    }

}
