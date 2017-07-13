package com.texttwist.server.models;

import com.texttwist.server.Server;
import javax.swing.*;
import java.io.*;
import java.util.Random;


/**
 * Author:      Lorenzo Iovino on 26/06/2017.
 * Description: Dictionary Model. Provides the dictionary and methods for manage it
 */

public class Dictionary {

    private static DefaultListModel<String> wordList = new DefaultListModel<>();

    public Dictionary(String dictionaryPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(dictionaryPath)))) {
            for (String line; (line = br.readLine()) != null; ) {
                wordList.addElement(line);
            }
        } catch (FileNotFoundException e) {
            Server.logger.write("DICTIONARY: Dictionary file not found!");
        } catch (IOException e) {
            Server.logger.write("DICTIONARY: Can't read dictionary file!");
        }
    }

    //Get a random word in wordsList with minimumWordSize < size < maximumWordSize
    public String getRandomWord(int minimumWordSize, int maximumWordSize){

        Random randomGenerator = new Random();
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

    //Check if a word is contained in dictionary
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
