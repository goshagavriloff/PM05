package core.forum;

import java.util.HashMap;

@FunctionalInterface
public interface Method {
    void execute(HashMap<String,String> params);
}