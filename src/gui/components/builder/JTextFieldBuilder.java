package gui.components.builder;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Supplier;

public class JTextFieldBuilder{

    private Font originalFont;
    private Color originalForeground;
    /**
     * Grey by default*
     */
    private Color placeholderForeground = new Color(160, 160, 160);
    private boolean textWrittenIn;

    private JTextComponent _field;
    /**
     * You can insert all constructors.
     * I inserted only this one.*
     */
    public JTextFieldBuilder() {
    }
    //override
    private void _setFont(Font f) {
        _field.setFont(f);
//        if (!isTextWrittenIn()) {
//            originalFont = f;
//        }
    }

    private void _setForeground(Color fg) {
        _field.setForeground(fg);
//        if (!isTextWrittenIn()) {
//            originalForeground = fg;
//        }
    }

    public Color getPlaceholderForeground() {
        return placeholderForeground;
    }

    public void setPlaceholderForeground(Color placeholderForeground) {
        this.placeholderForeground = placeholderForeground;
    }

    public boolean isTextWrittenIn() {
        return textWrittenIn;
    }

    public void setTextWrittenIn(boolean textWrittenIn) {
        this.textWrittenIn = textWrittenIn;
    }

    public void setPlaceholder(final String text) {

        this.customizeText(text);

        _field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (_field.getText().trim().length() != 0) {
                    _setFont(originalFont);
                    _setForeground(originalForeground);
                    setTextWrittenIn(true);
                }

            }
        });

        _field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!isTextWrittenIn()) {
                    _field.setText("");
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (_field.getText().trim().length() == 0) {
                    customizeText(text);
                }
            }

        });

    }

    private void customizeText(String text) {
        _field.setText(text);
        /**If you change font, family and size will follow
         * changes, while style will always be italic**/
        _setFont(new Font(_field.getFont().getFamily(), Font.ITALIC, _field.getFont().getSize()));
        _setForeground(getPlaceholderForeground());
        setTextWrittenIn(false);
    }

    public void set_field(Supplier<JTextComponent> supplier) {
        this._field = supplier.get();

        originalForeground=new Color(0,0,0);
        originalFont=new Font(_field.getFont().getFamily(), _field.getFont().getStyle(), _field.getFont().getSize());
    }

    public JTextComponent get_field() {
        return _field;
    }
}