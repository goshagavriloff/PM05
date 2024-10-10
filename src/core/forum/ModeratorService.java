package core.forum;

import org.json.JSONObject;
import utils.listener.SendDBListener;

import java.util.HashMap;
import java.util.List;

public class ModeratorService extends Service{

    public ModeratorService(){
        super();

        methods.put("deleteTopicById",this::deleteTopicById);
        methods.put("deleteMessageById",this::deleteMessageById);
        methods.put("editTopicById",this::editTopicById);
        methods.put("bunUserByLogin",this::bunUserByLogin);
    }

    public void deleteTopicById(HashMap<String, String> params){
        String id=params.get("topic_id");
        String _sql="call api_delete_topic('%s')".formatted(id);

        activeListener= getDBListenerProcedure();
        handle(_sql);
    }

    public void deleteMessageById(HashMap<String, String> params){
        String id=params.get("message_id");
        String _sql="DELETE FROM `messages` WHERE id='%s'".formatted(id);

        activeListener= getDBListenerProcedure();
        handle(_sql);
    }

    public void editTopicById(HashMap<String, String> params){
        String id=params.get("topic_id");
        String title=params.get("topic_title");

        String _sql="UPDATE `topics` SET `title`='%s' WHERE id='%s'".formatted(title,id);

        activeListener= getDBListenerProcedure();
        //handle(_sql);
        System.out.println(_sql);
    }


    public void bunUserByLogin(HashMap<String, String> params){
        String id=params.get("login");
        String interval=params.get("interval");
        String _sql="call api_bun_user('%s','%s')".formatted(id,interval);

        activeListener= getDBListenerProcedure();
        handle(_sql);
    }

    private SendDBListener getDBListenerProcedure(){
        return new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> value) {
                next.onResult(value);
            }

            @Override
            public void onError(Exception e) {
                next.onError(e);
            }
        };
    }

}
