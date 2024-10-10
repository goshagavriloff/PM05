package gui.form;

import core.forum.Service;
import core.forum.UserService;
import gui.components.director.Director;
import gui.components.director.ServiceDirector;
import org.json.JSONObject;
import utils.listener.IListener;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class AuthForm extends GuiForm{
    private JPanel panel1;
    private JButton buttonLogin;
    private JLabel LabelLogin;
    private JLabel LabelPass;
    private JPasswordField passwordField;
    private JTextField LoginField;
    private JButton buttonRegister;


    public String title="Авторизация в систему";
    public Dimension size=new Dimension(350,170);


    public AuthForm() {
        super();

        initiate(title,size,panel1);
        render();
        setup();
    }


    public AuthForm(String _login,String _pwd){
        super();

        initiate(title,size,panel1);
        render();
        setup();

        LoginField.setText(_login);
        passwordField.setText(_pwd);
    }



    @Override
    protected void setup(){

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });

        buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame form=new RegisterForm();

                close();
            }
        });
    }


    private void login(){

        HashMap<String, String> params = new HashMap<String, String>() {{
            put("pwd",passwordField.getText());
            put("login",LoginField.getText());
        }};

        SendDBListener ClassListener = new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> data) {
                String login=data.get(0).getString("NAME");
                String role=data.get(0).getString("ROLE_NAME");
                Supplier<ForumForm> form=()->new ForumForm(login);

                if (role.equals("moderator")) {
                    form=()->new ForumModeratorForm(login);
                }

                form.get();

                close();
            }

            @Override
            public void onError(Exception e) {
                JOptionPane.showMessageDialog(buttonLogin,"Auth failed");
            }
        };

        Director<Service> director=new ServiceDirector()
                .method("auth")
                .params(params)
                .next(ClassListener)
                .build(UserService::new);

        Service result=director.make();


    }

}
