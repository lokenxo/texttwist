package com.texttwist.server.models;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by loke on 26/06/2017.
 */
public class Dictionary {

    static DefaultListModel<String> wordList = new DefaultListModel<>();
    private Random randomGenerator;

    public Dictionary(String dictionaryPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(dictionaryPath)))) {
            for (String line; (line = br.readLine()) != null; ) {
                wordList.addElement(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandomWord(int minimumWordSize, int maximumWordSize){

        randomGenerator = new Random();
        int index = randomGenerator.nextInt(wordList.size());
        String word = wordList.get(index);
        if(word.length() >= minimumWordSize && word.length() <= maximumWordSize) {
            return word;
        } else {
            for(int i = index; i< wordList.size()-index; i++){
                word = wordList.get(i);
                if(word.length() >= minimumWordSize && word.length() <= maximumWordSize) {
                    return word;
                }
            }
            for(int i = index; i>0 ; i--){
                word = wordList.get(i);
                if(word.length() >= minimumWordSize && word.length() <= maximumWordSize) {
                    return word;
                }
            }
            return "";
        }
    }

    public static Boolean isContainedInDictionary(String word){
        if(word.equals("")){
            return true;
        }
        for(int i = 0; i< wordList.size(); i++){
            if(wordList.get(i).equals(word)) {
                return true;
            }
        }
        return false;
    }

}
