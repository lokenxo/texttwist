package models;

import org.json.simple.JsonObject;
import java.io.Serializable;

/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: Response
 */
public class Response implements Serializable{
    public String message;
    public Integer code;
    public JsonObject data;

    public Response(String message, Integer code, JsonObject data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }
}
