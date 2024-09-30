package gui.dialog;

import org.json.JSONObject;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public abstract class DialogForm extends JDialog {
    protected JPanel _contentPane;
    protected JButton _buttonOK;
    protected JButton _buttonCancel;
    public DialogForm(){
        super();
    }


    protected void initiate(String title){
        setContentPane(_contentPane);
        setModal(true);
        setTitle(title);
        getRootPane().setDefaultButton(_buttonOK);

        _buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        _buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        _contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    protected SendDBListener getDBListener(String error_message){
        return new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> data) {
                dispose();
            }
            @Override
            public void onError(Exception e) {
                JOptionPane.showMessageDialog(_buttonOK,error_message);
            }
        };
    }

    protected abstract void build();
    protected abstract void onOK();
    protected abstract void onCancel();
}
