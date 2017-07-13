package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;
import javafx.util.Pair;
import javax.swing.*;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 23/06/2017.
 * Description: Task: Join Match
 */
public class JoinMatch implements Callable<Boolean> {
    private final String matchName;
    private final String playerName;
    private final SocketChannel socketChannel;

    public JoinMatch(String playerName, DefaultListModel<String> matchName, SocketChannel socketChannel) {
        this.playerName = playerName;
        this.matchName = matchName.get(0);
        this.socketChannel = socketChannel;
    }

    @Override
    public Boolean call() throws Exception {
        final Match thisMatch = Match.findMatch(Match.activeMatches, this.matchName);
        if (thisMatch != null) {
            for (int j = 0; j < thisMatch.playersStatus.size(); j++) {
                String name = thisMatch.playersStatus.get(j).getKey();
                if (name.equals(playerName)) {
                    thisMatch.playersStatus.remove(j);
                    thisMatch.playersStatus.add(new Pair<>(name, 1));
                    thisMatch.playersSocket.remove(j);
                    thisMatch.playersSocket.add(new Pair<>(name, socketChannel));
                    return allJoined(thisMatch);
                }
            }
        }
        return false;
    }

    private Boolean allJoined(Match match) {
        for (int i = 0; i < match.playersStatus.size(); i++) {
            if (match.playersStatus.get(i).getValue() == 0) {
                return false;
            }
        }
        match.joinTimeout = false;
        return true;
    }
}
