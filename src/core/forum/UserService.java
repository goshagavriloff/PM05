package core.forum;

import org.json.JSONObject;
import utils.MyEntry;
import utils.listener.SendDBListener;

import java.util.HashMap;
import java.util.List;

public class UserService extends Service{


    public UserService(){
        super();
        methods.put("auth",this::auth);
        methods.put("register",this::register);
        methods.put("getById",this::getById);
    }


    @Override
    protected void getById(HashMap<String, String> params) {
        String id=params.get("id");

        String _sql="SELECT * FROM users where id='%s'".formatted(id);
        activeListener= getDBListener();

        handle(_sql);
    }


    private void register(HashMap<String, String> params) {
        String login=params.get("login");
        String pwd=params.get("pwd");
        String _sql="call api_register('%s','%s')".formatted(login,pwd);

        activeListener =getDBListener();

        handle(_sql);

    }

    private void auth(HashMap<String, String> params) {
        String login=params.get("login");
        String pwd=params.get("pwd");
        String _sql="SELECT * FROM users WHERE name='%s' and password='%s'".formatted(login,pwd);

        activeListener =getDBListener();
        handle(_sql);
    }




}
