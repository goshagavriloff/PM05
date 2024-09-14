package core.translator;

import org.json.JSONArray;
import utils.consts.LanguageConstants;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Properties;
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
//        JSONArray arr=new JSONArray(body);
//
//        return arr.getJSONArray(0).getJSONArray(0).get(0).toString();
        JSONArray arr=new JSONArray(body);
        JSONArray trs=arr.getJSONArray(0);
        String result="";

        for (Object cursor : trs)
        {
            result+="%s".formatted(((JSONArray) cursor).get(0).toString());
        }

        return result;
    }
    @Override
    public CompletableFuture<Translation> execute(String txt, String from, String to)  {
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

        String _txt=encodeUri(txt);
        String _from=getLanguageCode(from);
        String _to=getLanguageCode(to);
        String url="https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s".formatted(_from, _to,_txt);

        SSLContext sc=getSSLContext();
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());



        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(3 * 1000))
                .sslContext(sc) // SSL context 'sc' initialised as earlier
                .proxy(ProxySelector.of(new InetSocketAddress("10.0.33.52", 3128)))
                .build();
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
    private SSLContext getSSLContext() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sc =null;
        try{
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception er){

        }

        return sc;
    }

}
