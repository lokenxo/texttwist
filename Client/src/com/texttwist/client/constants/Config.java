package com.texttwist.client.constants;

/**
 * Created by loke on 15/06/2017.
 */
public class Config {

    private static String AuthServerURI = "localhost";
    private static Integer AuthServerPort = 9999;

    public static String GameServerURI = "localhost";
    public static Integer GameServerPort = 10000;


    public static String getAuthServerURI(){
        return "rmi://".concat(AuthServerURI).concat(":").concat(AuthServerPort.toString());
    }

    public static String getGameServerURI(){
        return "tcp://".concat(GameServerURI).concat(":").concat(GameServerPort.toString());
    }

}
