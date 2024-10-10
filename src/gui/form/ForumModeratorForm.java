package gui.form;

import core.forum.ModeratorService;
import core.forum.TopicService;
import gui.components.ButtonColumn;
import gui.components.JTableListener;
import gui.components.builder.JTableBuilder;
import gui.dialog.*;
import org.json.JSONObject;
import utils.MyEntry;
import utils.listener.IListener;
import utils.listener.SendDBListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ForumModeratorForm extends ForumForm{
    private final ModeratorService service;

    public ForumModeratorForm(String login){
        super(login);

        service=new ModeratorService();
    }

    @Override
    protected void onCellClick(MouseEvent e, Supplier<DialogForm> modal) {
        int col_index=tableTopics.getSelectedColumn();
        HashMap<String, String> params=getParams();

        String login=params.get("login");
        String topic_id=params.get("topic_id");
        switch (col_index){
            case 1:
                openModal(()->new EditTopicDialog(topic_id));
                break;
            case 2:
                openModal(()->new BunUserDialog(login));
                break;
            case 3:
                break;
            default:
                super.onCellClick(e,modal);

        }

    }



    private HashMap<String, String> getParams(){
        int row_index = tableTopics.getSelectedRow();
        DefaultTableModel tbl=((DefaultTableModel)tableTopics.getModel());
        String id= tbl.getValueAt(row_index,0).toString();
        String title=tbl.getValueAt(row_index,2).toString();

        return new HashMap<String, String>() {{
            put("topic_id",id);
            put("login",_login);
            put("topic_title",title);
        }};
    }

    @Override
    protected void createUIComponents() {

        List<MyEntry<String,String>> columns= TopicService.getColumns();

        JTableListener callback=new JTableListener(){
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int row_index = tableTopics.getSelectedRow();
                String id= (String) tableTopics.getModel().getValueAt(row_index, 0);

                onCellClick(mouseEvent,() -> new TopicModeratorDialog(id,_login));
            }
        };

        Action next = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                HashMap<String, String> params=getParams();
                IListener ClassListener = new SendDBListener() {
                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                    }

                    @Override
                    public void onResult(List<JSONObject> arr) {
                        _topics.get();
                    }
                };

                service.setNext(ClassListener);
                service.deleteTopicById(params);

            }
        };

        columns.add(new MyEntry<String,String>("",null));

        _builder=new JTableBuilder();
        _builder=_builder.columns(columns);
        tableTopics=_builder
                .callback(callback)
                .build();

        tableTopics.setDefaultEditor(Object.class, null);
        tableTopics.setFillsViewportHeight(true);
        tableTopics.removeColumn(tableTopics.getColumnModel().getColumn(0));

        ButtonColumn buttonColumn = new ButtonColumn(tableTopics, next, 3,"delete");
    }
}
