package utilities;

import javafx.util.Pair;
import javax.swing.*;

/**
 * Author:      Lorenzo Iovino on 12/07/2017.
 * Description: This is a module of parse utilities for different purposes
 */
public class Parse {

    //Parse score and split by ":" character
    public static DefaultListModel<Pair<String, Integer>> score(DefaultListModel<String> score) {
        DefaultListModel<Pair<String, Integer>> list = new DefaultListModel<>();
        for (int i = 0; i < score.size() - 1; i++) {
            String[] splitted = score.get(i).split(":");
            list.addElement(new Pair<>(splitted[0], new Integer(splitted[1])));
        }
        return list;
    }
}
