package com.texttwist.client.tasks;

import com.texttwist.client.App;
import constants.Config;
import models.Message;
import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: This job will waits for the score of the match sent by server, at end it will execute a callback
 *              function that show the highscore pages.
 */
public class WaitForScore extends SwingWorker<Void,Void> {

    private SwingWorker callback;
    public WaitForScore(SwingWorker callback){
        this.callback = callback;
    }

    @Override
    public Void doInBackground() {

        try {
            //Wait for the final scores of the match
            while(true) {
                byte[] buf = new byte[1024];
                DatagramPacket receivedDatagram = new DatagramPacket(buf, buf.length);
                App.game.multicastSocket.receive(receivedDatagram);

                String s = new String(receivedDatagram.getData());
                Message msg = Message.toMessage(s);

                //When arrive a message with message=FINALSCORE popolate ranks
                if(msg.message.equals("FINALSCORE")){
                    if(msg.data != null) {
                        App.game.ranks.clear();
                        App.game.ranks = utilities.Parse.score(msg.data);
                    }
                    break;
                }
            }
        } catch (UnknownHostException e) {
            App.logger.write("WAIT FOR SCORE: Host unknown");
        } catch (IOException e) {
            App.logger.write("WAIT FOR SCORE: Can't read from multicast Socket");
        }
        return null;
    }

    @Override
    public void done(){
        try {
            //Leave group and close multicast socket
            App.game.multicastSocket.leaveGroup(InetAddress.getByName(Config.ScoreMulticastServerURI));
            App.game.multicastSocket.close();

            //Stop game
            App.game.stop();

            //Call callback
            this.callback.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
