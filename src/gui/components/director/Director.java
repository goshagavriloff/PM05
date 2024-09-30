package gui.components.director;

import javax.swing.text.JTextComponent;
import java.util.function.Supplier;

public interface Director<T> {
    public Director<T> build(Supplier<T> supplier);
    public T make();
}
