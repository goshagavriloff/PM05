package gui.components.director;

import gui.components.builder.JTextFieldBuilder;

import javax.swing.text.JTextComponent;
import java.util.function.Supplier;

public class JTextDirector implements Director<JTextComponent>{
    private Supplier<JTextComponent> _supplier;
    public JTextDirector(){

    }

    public JTextDirector build(Supplier<JTextComponent> supplier){
        _supplier=supplier;
        return this;
    }

    @Override
    public JTextComponent make() {
        JTextFieldBuilder _builder = new JTextFieldBuilder();
        _builder.set_field(_supplier);

        return _builder.get_field();
    }

    public JTextComponent make(String placeholder){
        JTextFieldBuilder _builder = new JTextFieldBuilder();

        _builder.set_field(_supplier);
        _builder.setPlaceholder(placeholder);

        return _builder.get_field();
    }
}
