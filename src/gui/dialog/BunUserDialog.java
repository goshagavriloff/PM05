package gui.dialog;

import core.forum.ModeratorService;
import org.json.JSONObject;
import utils.listener.IListener;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class BunUserDialog extends DialogForm {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton банНа1ЧасRadioButton;
    private JRadioButton банНа1ДеньRadioButton;
    private JLabel lTitleLabel;
    private JRadioButton банНа1МесяцRadioButton;
    private JRadioButton банНавсегдаRadioButton;
    HashMap<String, String> params;
    private String _activeLogin;
    ModeratorService service;
    public BunUserDialog(String login){
        super();

        _activeLogin=login;
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
        service.bunUserByLogin(params);
    }

    @Override
    protected void onCancel() {
        dispose();
    }

    private void setup(){
        params=new HashMap<String, String>() {{
            put("interval","1");
            put("login",_activeLogin);
        }};

        service=new ModeratorService();
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
        service.setNext(ClassListener);

        банНа1ЧасRadioButton.addActionListener(e->{
            банНа1ДеньRadioButton.setSelected(false);
            банНа1МесяцRadioButton.setSelected(false);
            банНавсегдаRadioButton.setSelected(false);
            params.put("interval","1");
        });
        банНа1ДеньRadioButton.addActionListener(e->{
            банНа1ЧасRadioButton.setSelected(false);
            банНа1МесяцRadioButton.setSelected(false);
            банНавсегдаRadioButton.setSelected(false);
            params.put("interval","24");
        });
        банНа1МесяцRadioButton.addActionListener(e->{
            банНа1ДеньRadioButton.setSelected(false);
            банНа1ЧасRadioButton.setSelected(false);
            банНавсегдаRadioButton.setSelected(false);
            params.put("interval","720");
        });
        банНавсегдаRadioButton.addActionListener(e->{
            банНа1ДеньRadioButton.setSelected(false);
            банНа1ЧасRadioButton.setSelected(false);
            банНа1МесяцRadioButton.setSelected(false);
            params.put("interval","10000000000000000000");
        });
    }

}
