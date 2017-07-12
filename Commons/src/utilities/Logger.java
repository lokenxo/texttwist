package utilities;

import java.io.*;
import java.util.Date;

/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: This is a module for logging the System output in files
 */
public class Logger {


    private final File logFile;
    private final String name;
    private static PrintWriter out;
    private static BufferedWriter bw;
    private static FileWriter fw;
    private Boolean debug;

    public Logger(File logFile, String name, Boolean debug) throws IOException {
        this.logFile = logFile;
        this.name = name;
        this.debug = debug;
    }

    public synchronized void write(String msg){
        try {
            fw = new FileWriter(logFile, true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
            Date d = new Date();
            out.append("LOGGER ("+name+"): " + d.toString() + " - " + msg + "\n");
            if(debug) {
                System.out.println("LOGGER (" + name + "): " + d.toString() + " - " + msg + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
