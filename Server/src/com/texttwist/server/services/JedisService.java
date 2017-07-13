package com.texttwist.server.services;

import models.User;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.texttwist.server.Server.jedisPool;

/**
 * Created by loke on 11/07/2017.
 */
public class JedisService {

    public JedisService(){

    }

    /** Read the object from Base64 string. */
    public static Object fromString(String s) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    public static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Void add(String key, Serializable o){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.append(key, JedisService.toString(o)+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static List<Serializable> get(String key){
        Jedis jedis = null;
        List<Serializable> l = new ArrayList<>();
        try {
            System.out.println("USER ss");

            jedis = jedisPool.getResource();
            String usersString = jedis.get(key);
            System.out.println("USER "+usersString);
            if(usersString!=null) {
                String[] lines = usersString.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    l.add((User) JedisService.fromString(lines[i]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return l;
    }

    public static Void removeAll(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

}
