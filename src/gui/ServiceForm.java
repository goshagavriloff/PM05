package gui;

import gui.dict.DictonaryForm;
import gui.reciept.RecieptListForm;
import gui.translator.TranslatorForm;
import org.json.JSONObject;
import utils.conn.MysqlDBConnector;
import utils.consts.Service;
import utils.listener.SendDBListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ServiceForm extends JFrame {
    private JPanel panel1;
    private JLabel imageReciepts;
    private JLabel imageDict;
    private JLabel imageTranslator;

    private String activeUser;
    private Service activeService;

    public ServiceForm() {
        setContentPane(panel1);
        setTitle("Server API");
        setVisible(true);
        setSize(750,300);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void createUIComponents() {
        String cwd = System.getProperty("user.dir");
        String image_path="res/drawable";
        String image_dict="book-cover.png";
        String image_recpt="vending-machine.png";
        String image_transltr="server-rack.png";

        String cwd_dict="%s/%s/%s".formatted(cwd,image_path,image_dict);
        String cwd_rcpt="%s/%s/%s".formatted(cwd,image_path,image_recpt);
        String cwd_transltr="%s/%s/%s".formatted(cwd,image_path,image_transltr);

        ImageIcon icon_dict = new ImageIcon(cwd_dict);
        ImageIcon icon_rcpt = new ImageIcon(cwd_rcpt);
        ImageIcon icon_transltr = new ImageIcon(cwd_transltr);

        imageDict=new JLabel(icon_dict);
        imageReciepts=new JLabel(icon_rcpt);
        imageTranslator=new JLabel(icon_transltr);


        imageDict.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                activeService=Service.dictonary;
                setup();

            }
        });

        imageReciepts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                activeService=Service.reciept;
                setup();

            }
        });

        imageTranslator.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                activeService=Service.translator;
                setup();

            }
        });

    }
    private void setup(){
        MysqlDBConnector connector=new MysqlDBConnector();
        String service=activeService.toString();

        String sql = "SELECT * FROM v1 WHERE login='%s' and service='%s'".formatted(activeUser,service);

        connector.setup();
        connector.set_sql(sql);

        SendDBListener ClassListener = new SendDBListener() {
            @Override
            public void onResult(List<JSONObject> value) {
                if (value.isEmpty()){
                    JOptionPane.showMessageDialog(panel1,"You have not access");
                } else {
                    next();
                }

            }

            public void onError(Exception e) {

                JOptionPane.showMessageDialog(panel1,"You have not access");
                super.onError(e);
            }
        };

        connector.set_listener(ClassListener);
        connector.execute();
    }

    private void next(){

        switch (activeService){
            case reciept -> new RecieptListForm();
            case dictonary -> new DictonaryForm();
            case translator -> new TranslatorForm();
        }

        setVisible(false);
        dispose();
    }

    public void setActiveUser(String activeUser) {
        this.activeUser = activeUser;
    }
}
