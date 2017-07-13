package constants;

/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: Config
 */
public class Config {

    public static String AuthServerURI = "localhost";
    public static Integer AuthServerPort = 9999;

    public static String GameServerURI = "localhost";
    public static Integer GameServerPort = 10000;

    public static String WordsReceiverServerURI = "localhost";
    public static Integer WordsReceiverServerPort = 10001;

    public static String ScoreMulticastServerURI = "226.226.226.226";

    public static Integer NotificationServerPort = 20000;
    public static Integer NotificationServerStubPort = 5000;
    public static String NotificationServerName ="notification";
    public static int timeoutGame = 120;

    public static String getAuthServerURI(){
        return "rmi://".concat(AuthServerURI).concat(":").concat(AuthServerPort.toString());
    }

}
