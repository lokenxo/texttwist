package models;
import org.json.simple.JsonObject;
import java.io.Serializable;

/**
 * Created by loke on 15/06/2017.
 */
public class TTResponse implements Serializable{
    public String message;
    public Integer code;
    public JsonObject data;

    public TTResponse(String message, Integer code, JsonObject data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }
}
