package com.texttwist.server.tasks;
import com.texttwist.server.services.AccountsService;
import com.texttwist.server.models.Dictionary;
import com.texttwist.server.models.Match;
import javafx.util.Pair;
import models.User;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Jedis Service
 */
public class ComputeScore implements Callable<Integer> {

    public DefaultListModel<String> words;
    public final String sender;
    public Match match;
    public DefaultListModel<String> wordsValid;

    public ComputeScore(String sender, DefaultListModel<String> words, Match match){
        this.words = words;
        this.match = match;
        this.sender = sender;
    }

    @Override
    public Integer call() throws Exception {
            wordsValid = new DefaultListModel<>();
            Integer score = 0;

            for (int i = 0; i < words.size(); i++) {
                if (isValid(words.get(i), match.letters)) {
                    score += words.get(i).length();
                    System.out.println(words.get(i) + " is valid!" + " : " + score );
                    wordsValid.addElement(words.get(i));
                }
            }


            System.out.println(sender +" totalize SCORE = " + score);
            match.setScore(sender, score);
        for (Pair<String, Integer> player : match.playersScore) {
            System.out.println(player.getValue());

        }
            System.out.println(score);

            User u = AccountsService.getInstance().findUser(sender);
            u.addScore(score);

            if(match.allPlayersSendedHisScore()) {

                match.matchTimeout = false;
                System.out.println("MATCH TIMEOUT CANCELLATO");

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
