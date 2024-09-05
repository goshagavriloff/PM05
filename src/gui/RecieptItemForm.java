package gui;

import core.reciept.JsonApiConnector;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class RecieptItemForm extends JDialog{
    private JPanel panel1;
    private JLabel LabelMealThumb;
    private JLabel LabelStrMeal;
    private JLabel LabelStrArea;
    private JTabbedPane tabbedPane1;
    private JTextPane descTextPane;
    private JLabel LabId;
    private JsonApiConnector connector;
    private JList IngrJList;

    private String _id;
    public RecieptItemForm(String id) {

        _id= id;
        setup();

        setContentPane(panel1);
        LabId.setText("Рецепт №%s".formatted(id));

        setSize(450,350);

        setModal(true);
        setVisible(true);


    }
    private void setup(){

        connector=new JsonApiConnector();
        downloadData();

    }
    private void downloadData(){
        String id=_id;
        HashMap<String,String> settings = new HashMap<>();

        String url="https://www.themealdb.com/api/json/v1/1/lookup.php?i=%s".formatted(id);

        settings.put("url",url);
        settings.put("method", "GET");
        settings.put("body", "");
        settings.put("type", "application/json; charset=utf-8");

        connector.setup(settings);

        JsonApiConnector.SendDataListener ClassListener = new JsonApiConnector.SendDataListener() {
            @Override
            public void onResult(String value) {

                JSONObject data=new JSONObject(value);


                if (data.get("meals")!=JSONObject.NULL){
                    JSONArray meals=data.getJSONArray("meals");
                    JSONObject obj=meals.getJSONObject(0);
                    render(obj);

                }
            }

            @Override
            public void onError(IOException e) {

                JOptionPane.showMessageDialog(LabId,"Search failed");
                super.onError(e);
            }
        };
        connector.SendData( ClassListener);

    }

    private void render(JSONObject meal){
        String strMeal=meal.getString("strMeal");
        String strArea=meal.getString("strArea");
        String strMealThumb=meal.getString("strMealThumb");
        String strInstructions=meal.getString("strInstructions");


        List<String> strIngrList=new ArrayList<>();
        String strIngr="";
        String strMes="";
        String strIngMes="";

        for (int i = 1; i < 21; i++) {
            strIngr=meal.getString("strIngredient%s".formatted(i));
            strMes=meal.getString("strMeasure%s".formatted(i));

            strIngMes="%s - %s".formatted(strIngr,strMes).trim();


            strIngrList.add(strIngMes);
        }
        strIngrList.removeAll(Arrays.asList("", null,"-"));

        try {

            URL url = new URL(strMealThumb);
            BufferedImage image = ImageIO.read(url);
            Image dimg = image.getScaledInstance(LabelMealThumb.getWidth(), LabelMealThumb.getHeight(),
                    Image.SCALE_SMOOTH);
            LabelMealThumb.setIcon(new ImageIcon(dimg));
        } catch (Exception ex){

        }


        LabelStrMeal.setText(strMeal);
        LabelStrArea.setText(strArea);
        descTextPane.setText(strInstructions);
        IngrJList.setListData(strIngrList.toArray());


    }


    private void createUIComponents() {
        IngrJList= new JList<String>();
    }


}
