package utils.listener;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IListener<T> {
    void onError(Exception e);
    void onResult(T value);

}