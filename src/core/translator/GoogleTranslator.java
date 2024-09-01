package core.translator;

import org.json.JSONArray;
import utils.LanguageConstants;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class GoogleTranslator extends TextTranslator {
    public GoogleTranslator(){
       super();
       this.auto_from="auto";
       this.auto_to="en";
    }

    private static String encodeUri(String string) {
        if (string == null) {
            return null;
        }
        return URLEncoder.encode(string, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String getLanguageCode(String language){
        Collection<String> map= LanguageConstants.google.keySet();
        AtomicReference<String> result= new AtomicReference<>("auto");

        map.forEach(item->{
            String _lg=LanguageConstants.google.get(item);
            if (_lg.equals(language)) {
                result.set(item);
            }
        });

        return result.get();
    }

    private String getTranslateFromJSON(String body) {
        JSONArray arr=new JSONArray(body);

        return arr.getJSONArray(0).getJSONArray(0).get(0).toString();

    }
    @Override
    public CompletableFuture<Translation> execute(String txt, String from, String to)  {

        String _txt=encodeUri(txt);
        String _from=getLanguageCode(from);
        String _to=getLanguageCode(to);
        String url="https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s".formatted(_from, _to,_txt);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        CompletableFuture<HttpResponse<String>> futureRequest=client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        return futureRequest.thenApply(response->{

            Translation tr=new Translation();

            tr.language_from=_from;
            tr.language_to=_to;
            tr.text=txt;
            tr.translation=getTranslateFromJSON(response.body());

            return tr;
        });
    }

}
