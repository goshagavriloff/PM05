package gui.components.builder;

import gui.components.JTableListener;
import org.json.JSONObject;
import utils.MyEntry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map.Entry;

public class JTableBuilder {
    private List<MyEntry<String,String>> _columns;
    private JTableListener _listener;

    public JTableBuilder(){

    }

    public JTableBuilder columns(List<MyEntry<String,String>> columns){
        this._columns=columns;
        return this;
    }

    public JTableBuilder callback(JTableListener callback){
        this._listener=callback;
        return this;
    }



    public JTable build(){
        DefaultTableModel model = new DefaultTableModel();
        for (Entry<String,String> column : _columns) {
            model.addColumn(column.getKey());
        }

        JTable result=new JTable(model);
        result.addMouseListener(_listener);

        return result;
    }

    public void reset(JTable table){
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
    }

    public void renderRow(JSONObject topic,JTable table){

        Object[] row =new Object[_columns.size()];

        for (int i = 0; i < _columns.size(); i++) {
            String key=_columns.get(i).getValue();
            String value="";
            if (topic.has(key)){
                value=topic.getString(key);
            }
            row[i]=value;
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(row);
    }

}
