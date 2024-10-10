package core.forum;

import org.json.JSONObject;
import utils.MyEntry;
import utils.conn.MysqlDBConnector;
import utils.listener.IListener;
import utils.listener.SendDBListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Service {
    protected IListener<Object> next;
    protected SendDBListener activeListener;
    protected final MysqlDBConnector connector;

    public HashMap<String, Method> methods=new HashMap<String, Method>();

    public Service(){
        connector=new MysqlDBConnector();
    }

    public void setNext(IListener<Object> next) {
        this.next = next;
    }

    protected void handle(String _sql){
        connector.setup();
        connector.set_sql(_sql);
        connector.set_listener(activeListener);
        connector.execute();
    }

    protected SendDBListener getDBListener(){
        return new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> value) {
                if (value.isEmpty()) {
                    this.onError(new Exception());
                } else {
                    next.onResult(value);
                }
            }

            @Override
            public void onError(Exception e) {

                next.onError(e);
            }
        };
    }


}
