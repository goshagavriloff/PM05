package gui;

import core.translator.Translation;
import core.translator.GoogleTranslator;
import core.translator.TextTranslator;
import utils.LanguageConstants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TranslatorForm extends JFrame {
    private JPanel panel1;
    private JComboBox cBox_from;
    private JComboBox cBox_to;
    private JTextArea tArea_from;
    private JTextArea tArea_to;
    private JButton bSwap;
    private TextTranslator translator;

    public TranslatorForm() {
        setup();

        setContentPane(panel1);
        setTitle("Google Переводчик");
        setSize(1080,750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void setup(){

        translator=new GoogleTranslator();

        Collection<String> map=LanguageConstants.google.values();
        List<String> langByKey = new ArrayList<>(map);
        Collections.sort(langByKey);

        Collection<JComboBox> cBoxes=new ArrayList<>(Arrays.asList(cBox_from,cBox_to));

        cBoxes.forEach(cBox->{
            cBox.addItem("Auto(Detect)");
            langByKey.forEach(lang-> cBox.addItem(lang));

            cBox.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    translate();
                }
            });

        });

        bSwap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                swap();
            }
        });
        tArea_from.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                translate();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                translate();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                translate();
            }
        });
    }

    public void swap(){
        String txt_temp=tArea_from.getText();
        int int_temp=cBox_from.getSelectedIndex();

        tArea_from.setText(tArea_to.getText());
        tArea_to.setText(txt_temp);

        cBox_from.setSelectedIndex(cBox_to.getSelectedIndex());
        cBox_to.setSelectedIndex(int_temp);

    }

    public void translate(){
        String txt=tArea_from.getText();
        String from=String.valueOf(cBox_from.getSelectedItem());
        String to=String.valueOf(cBox_to.getSelectedItem());

        CompletableFuture<Translation> data=translator.execute(txt,from,to);
        data.thenAccept(result->{
            tArea_to.setText(result.translation);
        });

    }
}
