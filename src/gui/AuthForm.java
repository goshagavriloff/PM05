package gui;

import org.json.JSONObject;
import utils.conn.MysqlDBConnector;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

public class AuthForm extends JFrame{
    private JPanel panel1;
    private JButton buttonLogin;
    private JLabel LabelLogin;
    private JLabel LabelPass;
    private JPasswordField passwordField;
    private JTextField LoginField;

    private MysqlDBConnector connector;

    public AuthForm() {
        setContentPane(panel1);
        setTitle("Авторизация в систему");
        setVisible(true);
        setSize(350,150);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setup();
    }

    private void setup(){
        connector=new MysqlDBConnector();

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

        String _sql="SELECT * FROM users WHERE name='%s' and password='%s'".formatted(login,pwd);

        connector.setup();
        connector.set_sql(_sql);

        SendDBListener ClassListener = new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> value) {
                if (value.isEmpty()) {
                    JOptionPane.showMessageDialog(buttonLogin,"Auth failed");
                } else {
                    JSONObject obj=value.getFirst();
                    String login=obj.getString("NAME");
                    ServiceForm form = new ServiceForm();

                    form.setActiveUser(login);
                    setVisible(false);
                    dispose();
                }
            }

            @Override
            public void onError(Exception e) {

                JOptionPane.showMessageDialog(buttonLogin,"Auth failed");
                super.onError(e);
            }
        };

        connector.set_listener(ClassListener);
        connector.execute();
    }

}
