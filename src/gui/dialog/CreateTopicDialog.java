package gui.dialog;

import core.forum.Service;
import core.forum.TopicService;
import gui.components.director.Director;
import gui.components.director.JTextDirector;
import gui.components.director.ServiceDirector;
import gui.form.ForumForm;
import org.json.JSONObject;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class CreateTopicDialog extends DialogForm {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tTitleField;
    private JTextArea tDescrField;

    private String _login;
    public CreateTopicDialog(String login) {
        super();
        build();
        initiate("Создание новой темы");
        _login=login;
     }

    @Override
    protected void build() {
        _buttonOK=buttonOK;
        _contentPane=contentPane;
        _buttonCancel=buttonCancel;
    }

    @Override
    protected void onOK() {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("topic_title",tTitleField.getText());
            put("topic_descr",tDescrField.getText());
            put("topic_login",_login);
        }};

        SendDBListener ClassListener = getDBListener("Topic's create failed");

        Director<Service> director=new ServiceDirector()
                .method("store")
                .params(params)
                .next(ClassListener)
                .build(TopicService::new);

        Service result=director.make();

    }

    @Override
    protected void onCancel() {
        dispose();
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
