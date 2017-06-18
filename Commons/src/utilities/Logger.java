package utilities;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * Created by loke on 15/06/2017.
 */
public class Logger {


    private static File logFile;
    private static String name;
    private static PrintWriter out;
    private static BufferedWriter bw;
    private static FileWriter fw;

    public Logger(File logFile, String name) throws IOException {
        this.logFile = logFile;
        this.name = name;
    }

    public synchronized static void write(String msg){
        try {
            fw = new FileWriter(logFile, true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
            System.out.println(msg);
            Date d = new Date();
            out.append("LOGGER ("+name+"): " + d.toString() + " - " + msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            out.close();
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
