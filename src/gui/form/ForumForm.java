package gui.form;

import core.forum.TopicService;
import gui.components.JTableListener;
import gui.components.builder.JTableBuilder;
import gui.components.director.JDialogDirector;
import gui.dialog.CreateTopicDialog;
import gui.dialog.DialogForm;
import gui.dialog.TopicDialog;
import kotlin.Pair;
import org.json.JSONObject;
import utils.MyEntry;
import utils.listener.IListener;
import utils.listener.SendDBListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class ForumForm extends GuiForm{
    private JPanel panel1;
    private JTable tableTopics;
    private JButton addTopicButton;

    public String title="Форум \"Бедный Студент\"";
    public Dimension size=new Dimension(750,600);

    private TopicService _topics;
    private String _login;
    private JTableBuilder _builder;

    public ForumForm(String login) {
        super();

        initiate(title,size,panel1);
        render();
        setup();
        setActiveUser(login);
    }

    private void setActiveUser(String login){
        this._login=login;
    }


    @Override
    protected void setup(){


        IListener ClassListener = new SendDBListener() {
            @Override
            public void onError(Exception e) {
                super.onError(e);
            }

            @Override
            public void onResult(List<JSONObject> arr) {
                _builder.reset(tableTopics);
                arr.forEach((JSONObject topic)->{
                    _builder.renderRow(topic,tableTopics);
                });
            }
        };

        _topics=new TopicService();
        _topics.setNext(ClassListener);
        _topics.get();

        addTopicButton.addActionListener(actionEvent -> {
            openModal(()->new CreateTopicDialog(_login));
            _topics.get();
        });

    }

    private void openModal(Supplier<DialogForm> supplier){
        JDialogDirector director=new JDialogDirector()
                .build(supplier);

        director.make();
    }

    private void createUIComponents() {
        _builder=new JTableBuilder();

        List<MyEntry<String,String>> columns=TopicService.getColumns();

        JTableListener callback=new JTableListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row_index = tableTopics.getSelectedRow();
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1 && row_index!=-1) {

                    String id= (String) tableTopics.getModel().getValueAt(row_index, 0);

                    openModal(() -> new TopicDialog(id,_login));
                    _topics.get();
                }
            }
        };

        _builder=_builder.columns(columns);
        tableTopics=_builder
                .callback(callback)
                .build();

        tableTopics.setDefaultEditor(Object.class, null);
        tableTopics.setFillsViewportHeight(true);
        tableTopics.removeColumn(tableTopics.getColumnModel().getColumn(0));

    }
}
