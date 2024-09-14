package utils.conn;


import utils.listener.SendDataListenerInterface;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public abstract class Connector {
    protected SendDataListenerInterface _listener;
    public void execute() {

    }

    public void setup(HashMap settings) {

    }

    public void set_listener(SendDataListenerInterface listener) {
        this._listener=listener;
    }

}
