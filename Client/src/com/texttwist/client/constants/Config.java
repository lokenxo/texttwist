package com.texttwist.client.constants;

/**
 * Created by loke on 15/06/2017.
 */
public class Config {

    private static String RMIServerURI = "localhost";
    private static Integer RMIServerPort = 9999;


    public static String getRMIServerAddress(){
        return "rmi://".concat(RMIServerURI).concat(":").concat(RMIServerPort.toString());
    }
}
