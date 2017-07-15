package constants;

/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: Config
 */
public class Config {

    public static String AuthServiceURI = "localhost";
    public static Integer AuthServicePort = 9999;

    public static String MessageServiceURI = "localhost";
    public static Integer MessageServicePort = 10000;

    public static String WordsReceiverServiceURI = "localhost";
    public static Integer WordsReceiverServicePort = 10001;

    public static String ScoreMulticastServiceURI = "226.226.226.226";

    public static Integer NotificationServicePort = 20000;
    public static Integer NotificationServiceStubPort = 30000;
    public static String NotificationServiceName ="notification";

    public static String RedisServiceURI = "localhost";

    public static int gameTimeout = 120; //2 minuti in sec
    public static int joinMatchTimeout = 7*1000*60; //7 minuti in millisec
    public static int sendWordsTimeout = 5*1000*60; //5 minuti in millisec

    public static String getAuthServiceURI(){
        return "rmi://".concat(AuthServiceURI).concat(":").concat(AuthServicePort.toString());
    }

}
