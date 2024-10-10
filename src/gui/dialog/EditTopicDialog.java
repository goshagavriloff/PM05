package gui.dialog;

import core.forum.Service;
import core.forum.TopicService;
import gui.components.director.Director;
import gui.components.director.JTextDirector;
import gui.components.director.ServiceDirector;
import org.json.JSONObject;
import utils.listener.IListener;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

public class EditTopicDialog extends DialogForm {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JTextField tTitleField;
    private JTextArea tDescrField;

    private HashMap<String, String> params=new HashMap<>();

    public EditTopicDialog(String topic_id) {
        super();
        this.params.put("topic_id",topic_id);

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

    @Override
    protected void onOK() {
        IListener ClassListener = new SendDBListener() {
            @Override
            public void onError(Exception e) {
                super.onError(e);
            }

            @Override
            public void onResult(List<JSONObject> arr) {
                dispose();
            }
        };

        this.params.put("topic_title",tTitleField.getText());
        this.params.put("topic_descr",tDescrField.getText());

        Director<Service> director=new ServiceDirector()
                .method("update")
                .params(params)
                .next(ClassListener)
                .build(TopicService::new);

        Service result=director.make();

    }
    @Override
    protected void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void setup(){
        //download data by id
        IListener ClassListener = new SendDBListener() {
            @Override
            public void onError(Exception e) {
                super.onError(e);
            }

            @Override
            public void onResult(List<JSONObject> arr) {
                if (arr.size()==1){
                    JSONObject first= arr.get(0);
                    tDescrField.setText(first.getString("DESCR"));
                    tTitleField.setText(first.getString("TITLE"));
                }
            }
        };

        Director<Service> director=new ServiceDirector()
                .method("getById")
                .params(params)
                .next(ClassListener)
                .build(TopicService::new);

        Service result=director.make();
    }

    private void createUIComponents() {
        JTextDirector director=new JTextDirector();

        tDescrField= (JTextArea) director
                .build(JTextArea::new)
                .make("Введите название темы");

        tTitleField=(JTextField) director
                .build(JTextField::new)
                .make("Введите сообщение");

    }
}
