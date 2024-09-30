package core.forum;

import utils.MyEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageService extends Service{

    public MessageService(){
        super();
        methods.put("getById",this::getById);
        methods.put("store",this::store);
        methods.put("getByTopicId",this::getByTopicId);
    }

    @Override
    protected void getById(HashMap<String, String> params) {

    }

    public static List<MyEntry<String, String>> getColumns() {

        List<MyEntry<String,String>> columns=new ArrayList<MyEntry<String,String>>();

        columns.add(new MyEntry<String,String>("id","ID"));
        columns.add(new MyEntry<String,String>("Дата сообщения","DATE_CREATE"));
        columns.add(new MyEntry<String,String>("Тема","TITLE"));
        columns.add(new MyEntry<String,String>("Автор","AUTHOR"));
//        columns.add(new MyEntry<String,String>("id темы","TOPIC"));

        return columns;

    }

    private void store(HashMap<String, String> params){
        String title=params.get("message_data");
        String topic=params.get("topic_id");
        String login=params.get("message_login");

        String _sql="call api_create_message('%s','%s','%s')".formatted(login,title,topic);
        activeListener= getDBListener();

        handle(_sql);
    }

    private void getByTopicId(HashMap<String, String> params){
        String topic=params.get("topic_id");
        String _sql="SELECT * FROM `view_messages` where topic='%s'".formatted(topic);
        activeListener= getDBListener();

        handle(_sql);

    }
}
