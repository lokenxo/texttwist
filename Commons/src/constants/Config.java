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
    public static Integer NotificationServerStubPort = 30000;
    public static String NotificationServerName ="notification";

    public static String RedisServerURI = "localhost";

    public static int gameTimeout = 120; //2 minuti in sec
    public static int joinMatchTimeout = 7*1000*60; //7 minuti in millisec
    public static int sendWordsTimeout = 5*1000*60; //5 minuti in millisec

    public static String getAuthServerURI(){
        return "rmi://".concat(AuthServerURI).concat(":").concat(AuthServerPort.toString());
    }

}
