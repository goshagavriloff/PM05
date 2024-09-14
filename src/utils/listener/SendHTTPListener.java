package utils.listener;

import java.io.IOException;

public abstract class  SendHTTPListener implements SendDataListenerInterface<String> {
    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onResult(String value) {

    }
}