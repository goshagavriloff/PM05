package core.translator;


import java.util.concurrent.CompletableFuture;

public abstract class TextTranslator {

    public String auto_from;
    public String auto_to;

    public abstract CompletableFuture<Translation> execute(String txt, String from, String to);
}
