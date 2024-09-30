package gui.dialog;

import core.forum.MessageService;
import core.forum.Service;
import core.forum.TopicService;
import gui.components.builder.JTableBuilder;
import gui.components.director.Director;
import gui.components.director.JTextDirector;
import gui.components.director.ServiceDirector;
import org.json.JSONObject;
import utils.MyEntry;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class TopicDialog extends DialogForm {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableMessages;
    private JLabel lTitleLabel;
    private JLabel lDescrLabel;
    private JTextArea tMessageArea;

    private JTableBuilder _builder;

    private HashMap<String, String> params;
    public TopicDialog(String id,String user) {
        super();

        params = new HashMap<String, String>() {{
            put("topic_id",id);
            put("message_login",user);
        }};

        build();
        initiate("");
        setup();

    }

    @Override
    protected void build() {
        _buttonOK=buttonOK;
        _contentPane=contentPane;
        _buttonCancel=buttonCancel;


    }

    protected void setup(){
        setupTopic();
        setupTopicMessages();
    }

    protected void onOK() {

        params.put("message_data",tMessageArea.getText());

        SendDBListener listener = new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> arr) {
                setupTopicMessages();
                tMessageArea.setText("");
            }

            @Override
            public void onError(Exception e) {
                JOptionPane.showMessageDialog(buttonOK,"Topic failed");
            }
        };

        Director<Service> director=new ServiceDirector()
                .method("store")
                .params(params)
                .next(listener)
                .build(MessageService::new);

        Service result=director.make();
    }

    protected void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void setupTopic(){

        SendDBListener ClassListener = new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> data) {
                JSONObject obj=data.get(0);
                String title=obj.getString("TITLE");
                String descr=obj.getString("DESCR");
                lTitleLabel.setText(title);
                lDescrLabel.setText(descr);
                setTitle("Тема: %s".formatted(title));
            }

            @Override
            public void onError(Exception e) {
                JOptionPane.showMessageDialog(buttonOK,"Topic failed");
            }
        };
        Director<Service> director=new ServiceDirector()
                .method("getById")
                .params(params)
                .next(ClassListener)
                .build(TopicService::new);

        Service result=director.make();

    }

    private void setupTopicMessages(){


        SendDBListener listener = new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> arr) {
                _builder.reset(tableMessages);
                arr.forEach((JSONObject topic)->{
                    _builder.renderRow(topic,tableMessages);
                });
            }

            @Override
            public void onError(Exception e) {
                JOptionPane.showMessageDialog(buttonOK,"Messages not found");
            }
        };

        Director<Service> director=new ServiceDirector()
                .method("getByTopicId")
                .params(params)
                .next(listener)
                .build(MessageService::new);

        Service result=director.make();
    }


    private void createUIComponents() {
        _builder=new JTableBuilder();
        List<MyEntry<String,String>> columns=MessageService.getColumns();
        _builder=_builder.columns(columns);
        tableMessages=_builder
                .build();

        tableMessages.setDefaultEditor(Object.class, null);
        tableMessages.setFillsViewportHeight(true);
        tableMessages.removeColumn(tableMessages.getColumnModel().getColumn(0));

        JTextDirector director=new JTextDirector();

        tMessageArea= (JTextArea) director
                .build(JTextArea::new)
                .make("Введите сообщение");
    }


}
