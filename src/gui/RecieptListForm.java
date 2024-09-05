package gui;

import core.reciept.ButtonColumn;
import core.reciept.JsonApiConnector;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class RecieptListForm extends JFrame{
    private JPanel panel1;
    private JTextField tSearch;
    private JButton bSearch;
    private JTable tableReciepts;

    private JsonApiConnector connector;
    public RecieptListForm() {
        setContentPane(panel1);
        setTitle("Рецепты онлайн");
        setVisible(true);
        setSize(550,450);

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
        String url="https://www.themealdb.com/api/json/v1/1/search.php?s=%s".formatted(searchName);

        settings.put("url",url);
        settings.put("method", "GET");
        settings.put("body", "");
        settings.put("type", "application/json; charset=utf-8");

        connector.setup(settings);

        JsonApiConnector.SendDataListener ClassListener = new JsonApiConnector.SendDataListener() {
            @Override
            public void onResult(String value) {

                JSONObject data=new JSONObject(value);

                reset();

                if (data.get("meals")!=JSONObject.NULL){
                    JSONArray meals=data.getJSONArray("meals");

                    for (int i=0; i < meals.length(); i++) {
                        JSONObject obj =meals.getJSONObject(i);
                        render(obj);
                    }
                }
            }

            @Override
            public void onError(IOException e) {

                JOptionPane.showMessageDialog(bSearch,"Search failed");
                super.onError(e);
            }
        };
        connector.SendData( ClassListener);
    }

    private void render(JSONObject meal){
        String idMeal=meal.getString("idMeal");
        String strMeal=meal.getString("strMeal");
        String strArea=meal.getString("strArea");

        DefaultTableModel model = (DefaultTableModel) tableReciepts.getModel();
        model.addRow(new Object[]{idMeal,strMeal, strArea, "Next"});

    }
    private void reset(){
        DefaultTableModel dtm = (DefaultTableModel) tableReciepts.getModel();
        dtm.setRowCount(0);
    }
    private void open(String idMeal){
        new RecieptItemForm(idMeal);
    }


    private void createUIComponents() {
        Action next = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );
                String id= ((DefaultTableModel)table.getModel()).getValueAt(modelRow,0).toString();

                open(id);
            }
        };

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("id");
        model.addColumn("strMeal");
        model.addColumn("strArea");
        model.addColumn("Link");
        tableReciepts=new JTable(model);

        tableReciepts.setFillsViewportHeight(true);
        ButtonColumn buttonColumn = new ButtonColumn(tableReciepts, next, 3);
    }
}
