package utils.listener;

import org.json.JSONObject;

import java.util.List;

public abstract class SendDBListener implements IListener<List<JSONObject>> {
    @Override
    public void onError(Exception e) {

    }


    public void onResult(List<JSONObject> value) {

    }
}