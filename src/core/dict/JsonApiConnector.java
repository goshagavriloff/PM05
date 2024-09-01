package core.dict;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

public class JsonApiConnector implements Connector{
    private String _url;
    private String _method;
    private String _body;
    private String _type;

    public interface SendDataListenerInterface {
        void onError(IOException e);
        void onResult(String value);
    }

    public abstract static class  SendDataListener implements SendDataListenerInterface {
        @Override
        public void onError(IOException e) {

        }

        @Override
        public void onResult(String value) {

        }
    }

    public JsonApiConnector(){
        super();
    }

    @Override
    public void execute() {

    }

    public void SendData(final SendDataListenerInterface listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = _url;
                String json = _body;
                RequestBody body = RequestBody.create( json,MediaType.parse(_type));
                Request request =null;

                if (_method.equalsIgnoreCase("get")){
                   request=new Request.Builder()
                           .url(url)
                           .build();
                }

                if (request==null){
                    request=new Request.Builder()
                            .url(url)
                            .method(_method,body)
                            .build();
                }


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String myResponse = response.body().string();
                            listener.onResult(myResponse);
                        } else{
                            listener.onError(new IOException());
                        }

                    }
                });
            }
        }).start();
    }

    @Override
    public void setup(HashMap settings) {
        _url= (String) settings.get("url");
        _method=(String) settings.get("method");
        _body=(String) settings.get("body");
        _type=(String) settings.get("type");
    }


}
