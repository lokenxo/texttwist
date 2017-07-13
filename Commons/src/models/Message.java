package models;

import javax.swing.*;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Author:      Lorenzo Iovino on 18/06/2017.
 * Description: Main
 */
public class Message implements Serializable {
    public String sender;
    public String message;
    public DefaultListModel<String> data;
    public String token;

    public Message(String message, String sender, String token, DefaultListModel<String> data) {
        this.message = message;
        this.sender = sender;
        this.data = data;
        this.token = token;
    }

    @Override
    public String toString(){
        String dataToString = "";
        if(data!=null) {
            for (int i = 0; i < data.size(); i++) {
                dataToString += data.get(i) + "|";
            }
        }
        return "MESSAGE?sender="+sender+"&token="+token+"&message="+message+"&"+dataToString;
    }

    public static Message toMessage(String data){
        int divisorType = data.indexOf("=");
        data = data.substring(divisorType+1, data.length());

        int divisorSender= data.indexOf("&");
        String sender = data.substring(0,divisorSender);
        int divisorSender_end = data.indexOf("=");
        data = data.substring(divisorSender_end+1, data.length());

        int divisorToken= data.indexOf("&");
        String token = data.substring(0,divisorToken);
        int divisorToken_end = data.indexOf("=");
        data = data.substring(divisorToken_end+1, data.length());

        int divisorMessage = data.indexOf("&");
        String message = data.substring(0,divisorMessage);
        data = data.substring(divisorMessage+1, data.length());

        String dataString = data.substring(0,data.length());

        String[] dataArray = dataString.split((Pattern.quote("|")));

        DefaultListModel<String> dataList = new DefaultListModel<String>();

        for (int i = 0; i<dataArray.length; i++){
            if(!dataArray[i].equals("")) {
                dataList.addElement(dataArray[i]);
            }
        }

        if(dataList.size() == 1 && dataList.firstElement().equals("")){
            dataList = null;
        }

        return new Message(message,sender,token,dataList);
    }
}
