package core.dict;


import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public interface Connector {
    void execute();
    void setup(HashMap settings);
}
