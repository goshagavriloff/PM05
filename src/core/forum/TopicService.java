package core.forum;

import utils.MyEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopicService extends Service{


    public TopicService(){
        super();
        methods.put("getById",this::getById);
        methods.put("getAll",this::getAll);
        methods.put("store",this::store);
        methods.put("update",this::update);
    }


    public void get(){
        getAll(null);
    }

    protected void getAll(HashMap<String, String> params){
        String _sql="SELECT * FROM view_topics";
        activeListener= getDBListener();
        handle(_sql);
    }

    protected void getById(HashMap<String, String> params){
        String id=params.get("topic_id");
        String _sql="SELECT * FROM topics where id='%s'".formatted(id);
        activeListener= getDBListener();

        handle(_sql);
    }

    public static List<MyEntry<String, String>> getColumns() {

        List<MyEntry<String,String>> columns=new ArrayList<MyEntry<String,String>>();

        columns.add(new MyEntry<String,String>("id","ID"));
        columns.add(new MyEntry<String,String>("Дата сообщения","DATE_CREATE"));
        columns.add(new MyEntry<String,String>("Тема","TITLE"));
        columns.add(new MyEntry<String,String>("Автор","LOGIN"));

        return columns;

    }

    public void store(HashMap<String, String> params){
        String title=params.get("topic_title");
        String descr=params.get("topic_descr");
        String login=params.get("topic_login");

        String _sql="call api_create_topic('%s','%s','%s')".formatted(login,title,descr);
        activeListener= getDBListener();

        handle(_sql);
    }

    public void update(HashMap<String, String> params){
        String title=params.get("topic_title");
        String descr=params.get("topic_descr");
        String id=params.get("topic_id");

        String _sql="call api_update_topic('%s','%s','%s')".formatted(id,title,descr);
        activeListener= getDBListener();

        handle(_sql);
    }
}
