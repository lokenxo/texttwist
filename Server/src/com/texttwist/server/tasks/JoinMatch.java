package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;
import javafx.util.Pair;

import javax.swing.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

import static com.texttwist.server.components.GameServer.activeMatches;
import static com.texttwist.server.models.Match.findMatch;

/**
 * Created by loke on 23/06/2017.
 */
public class JoinMatch implements Callable<Boolean> {
    public String matchName;
    public String playerName;
    public SocketChannel socketChannel;

    public JoinMatch(String playerName, DefaultListModel<String> matchName, SocketChannel socketChannel) {
        this.playerName = playerName;
        this.matchName = matchName.get(0);
        this.socketChannel = socketChannel;
    }


    @Override
    public Boolean call() throws Exception {
        Match thisMatch= findMatch(this.matchName);
        if(thisMatch!=null){
            for(int j = 0; j<thisMatch.playersStatus.size(); j++){
                String name = thisMatch.playersStatus.get(j).getKey();
                if (name.equals(playerName)){
                    thisMatch.playersStatus.remove(j);
                    thisMatch.playersStatus.addElement(new Pair<>(name,1));
                    thisMatch.playersSocket.remove(j);
                    thisMatch.playersSocket.addElement(new Pair<>(name,socketChannel));
                    System.out.println(playerName + ": JOINED");
                    return allJoined(thisMatch);
                }
            }
        }
        return allJoined(thisMatch);
    }


    private void printAll(Match match){
        for (int i = 0; i < match.playersStatus.size(); i++) {

            System.out.println(match.playersStatus.get(i).getKey());
            System.out.println(match.playersStatus.get(i).getValue());
            System.out.println(match.playersSocket.get(i).getKey());
            System.out.println(match.playersSocket.get(i).getValue());

        }
    }

    private Boolean allJoined(Match match) {
        for (int i = 0; i < match.playersStatus.size(); i++) {
            if (match.playersStatus.get(i).getValue() == 0) {
                return false;
            }
        }
        return true;
    }
}
