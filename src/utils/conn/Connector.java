package utils.conn;


import utils.listener.IListener;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public abstract class Connector {
    protected IListener _listener;
    public void execute() {

    }

    public void setup(HashMap settings) {

    }

    public void set_listener(IListener listener) {
        this._listener=listener;
    }

}
