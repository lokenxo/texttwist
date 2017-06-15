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
public class TTLogger {


    private static File logFile;
    private static PrintWriter out;
    private static BufferedWriter bw;
    private static FileWriter fw;

    public TTLogger(File logFile) throws IOException {
        this.logFile = logFile;

    }
    public static void write(String msg){
        try {
            fw = new FileWriter(logFile, true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
            System.out.println(msg);
            Date d = new Date();
            out.append(d.toString());
            out.append(" - ");
            out.append(msg);
            out.append("\n");
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
