package gui;

import core.reciept.JsonApiConnector;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class AuthForm extends JFrame{
    private JPanel panel1;
    private JButton buttonLogin;
    private JLabel LabelLogin;
    private JLabel LabelPass;
    private JPasswordField passwordField;
    private JTextField LoginField;

    private JsonApiConnector connector;

    public AuthForm() {
        setContentPane(panel1);
        setTitle("Авторизация в систему");
        setVisible(true);
        setSize(100,50);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setup();
    }

    private void setup(){
        connector=new JsonApiConnector();

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });
    }

    private void login(){
        String pwd=passwordField.getText();
        String login=LoginField.getText();

        JSONObject json = new JSONObject();
        json.put("password",pwd);
        json.put("email",login);

        HashMap<String,String> settings = new HashMap<>();

        settings.put("url","https://api.escuelajs.co/api/v1/auth/login");
        settings.put("method", "POST");
        settings.put("body", json.toString());
        settings.put("type", "application/json; charset=utf-8");

        connector.setup(settings);

        JsonApiConnector.SendDataListener ClassListener = new JsonApiConnector.SendDataListener() {
            @Override
            public void onResult(String value) {
                RecieptListForm form=new RecieptListForm();
                setVisible(false);
                dispose();
            }

            @Override
            public void onError(IOException e) {

                JOptionPane.showMessageDialog(buttonLogin,"Auth failed");
                super.onError(e);
            }
        };
        connector.SendData( ClassListener);
    }

}
