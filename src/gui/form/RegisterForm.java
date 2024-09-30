package gui.form;

import core.forum.Service;
import core.forum.UserService;
import gui.components.director.JTextDirector;
import gui.components.director.ServiceDirector;
import utils.listener.IListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class RegisterForm extends GuiForm{
    private JPanel panel1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField;
    private JTextField LoginField;

    private JButton buttonOk;
    private JButton buttonCancel;



    public String title="Регистрация в систему";
    public Dimension size=new Dimension(350,200);

    public RegisterForm(){
        initiate(title,size,panel1);
        render();
        setup();
    }


    @Override
    protected void setup(){

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValidated()){
                    register();
                }
                passwordField.setText("");
                passwordField1.setText("");


            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame form=new AuthForm();
                close();

            }
        });
    }

    private boolean isValidated(){

        String pwd=passwordField.getText();
        String pwd1=passwordField1.getText();
        String login=LoginField.getText();

        boolean isValidatePassword=pwd.equals(pwd1) &&!pwd.isEmpty();
        boolean isValidateLogin=!login.isEmpty();
        boolean result=isValidatePassword && isValidateLogin;

        return result;
    }

    private void register(){
        String login=LoginField.getText();
        String pwd=passwordField.getText();

        HashMap<String, String> params = new HashMap<String, String>() {{
            put("pwd",pwd);
            put("login",login);
        }};



        IListener ClassListener = new IListener<String>() {
            @Override
            public void onResult(String value) {
                AuthForm form=new AuthForm(login,pwd);
                close();
            }

            @Override
            public void onError(Exception e) {
                JOptionPane.showMessageDialog(buttonOk,"Register failed");
            }
        };

        ServiceDirector director= (ServiceDirector) new ServiceDirector()
                .method("auth")
                .params(params)
                .next(ClassListener)
                .build(UserService::new);

        Service result=director.make();
    }

    private void createUIComponents() {
        JTextDirector director=new JTextDirector();

        LoginField= (JTextField) director
                .build(JTextField::new)
                .make("Введите email");

        passwordField=(JPasswordField) director
                .build(JPasswordField::new)
                .make("Введите пароль");

        passwordField1=(JPasswordField) director
                .build(JPasswordField::new)
                .make("Введите_пароль");
    }
}
