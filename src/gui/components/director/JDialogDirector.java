package gui.components.director;


import gui.dialog.CreateTopicDialog;
import gui.dialog.DialogForm;

import javax.swing.text.JTextComponent;
import java.util.function.Supplier;

public class JDialogDirector implements Director<DialogForm>{
    private Supplier<DialogForm> _supplier;
    public JDialogDirector(){}


    @Override
    public JDialogDirector build(Supplier<DialogForm> supplier) {
        _supplier=supplier;
        return this;
    }

    @Override
    public DialogForm make() {
        DialogForm dialog = _supplier.get();
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
