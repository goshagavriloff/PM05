package gui.dict;

import utils.conn.JsonApiConnector;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.listener.SendHTTPListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class DictonaryForm extends JFrame{
    private JPanel panel1;
    private JTextField tSearch;
    private JButton bSearch;
    private JLabel lWord;
    private JLabel lPart;
    private JLabel lDefinition;

    private JsonApiConnector connector;

    public DictonaryForm() {
        setContentPane(panel1);
        setTitle("Словарь API");
        setVisible(true);
        setSize(350,550);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setup();
    }
    private void setup(){
        connector=new JsonApiConnector();

        bSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                search();
            }
        });
    }
    private void search(){

        HashMap<String,String> settings = new HashMap<>();
        String searchName=tSearch.getText().toLowerCase();
        String url="https://api.dictionaryapi.dev/api/v2/entries/en/%s".formatted(searchName);

        settings.put("url",url);
        settings.put("method", "GET");
        settings.put("body", "");
        settings.put("type", "application/json; charset=utf-8");

        connector.setup(settings);

        SendHTTPListener ClassListener = new SendHTTPListener() {
            @Override
            public void onResult(String value) {

                JSONArray data=new JSONArray(value);
                JSONObject obj=data.getJSONObject(0);
                JSONArray meanings=obj.getJSONArray("meanings");
                JSONArray definitions=meanings.getJSONObject(0).getJSONArray("definitions");

                String word=tSearch.getText();
                String partOfSpeech=meanings.getJSONObject(0).getString("partOfSpeech");

                String definition=definitions.getJSONObject(0).getString("definition");

                lWord.setText(word);
                lPart.setText(partOfSpeech);
                lDefinition.setText(definition);

            }

            @Override
            public void onError(Exception e) {

                JOptionPane.showMessageDialog(bSearch,"Search failed");
                super.onError(e);
            }
        };

        connector.set_listener(ClassListener);
        connector.execute();
    }
}
