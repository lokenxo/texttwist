package constants;

/**
 * Created by loke on 15/06/2017.
 */
public class Config {

    public static String AuthServerURI = "localhost";
    public static Integer AuthServerPort = 9999;

    public static String GameServerURI = "localhost";
    public static Integer GameServerPort = 10000;


    public static String WordsReceiverServerURI = "localhost";
    public static Integer WordsReceiverServerPort = 10001;

    public static String ScoreMulticastServerURI = "226.226.226.226";

    public static String NotificationServerURI = "localhost";
    public static Integer NotificationServerPort = 20000;
    public static Integer NotificationServerStubPort = 5000;
    public static String NotificationServerName ="notification";


    public static int timeoutGame = 10;


    public static String getNotificationServerURI(){
        return "rmi://".concat(NotificationServerURI).concat(":").concat(NotificationServerPort.toString());
    }

    public static String getAuthServerURI(){
        return "rmi://".concat(AuthServerURI).concat(":").concat(AuthServerPort.toString());
    }

    public static String getGameServerURI(){
        return "tcp://".concat(GameServerURI).concat(":").concat(GameServerPort.toString());
    }

}
