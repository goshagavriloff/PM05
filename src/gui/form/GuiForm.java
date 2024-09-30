package gui.form;

import javax.swing.*;
import java.awt.*;

public abstract class GuiForm extends JFrame {


    protected String _title;
    protected int _width;
    protected int _height;
    protected JPanel _panel1;

    public GuiForm(){
        super();
    }

    protected void initiate(String title, Dimension size,JPanel panel){
        _title=title;
        _width= (int) size.getWidth();
        _height=(int) size.getHeight();
        _panel1=panel;
    }

    protected void render(){
        setContentPane(_panel1);
        setTitle(_title);
        setVisible(true);
        setSize(_width,_height);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    protected void close(){
        setVisible(false);
        dispose();
    }

    protected abstract void setup();
}
