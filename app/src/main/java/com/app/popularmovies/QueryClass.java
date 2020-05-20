package com.app.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class QueryClass extends AsyncTask<String, Void, JSONObject> {
//    public boolean isOnline() {
//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
//    }
    public interface AsyncResponse {
        void processFinish(JSONObject result);
        void errorSignal();
    }

    private final AsyncResponse delegate;
    private String jsonString ="";

    QueryClass(AsyncResponse delegate){
        this.delegate = delegate;

    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        String apiKey = ApiKey.apiKey;
        String str=urls[0]+"?api_key="+ apiKey; //"&query=Jack+Ryan"
        URLConnection urlConn;
        BufferedReader bufferedReader = null;
        try
        {
            URL url = new URL(str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuffer.append(line);

            jsonString =stringBuffer.toString();
            return new JSONObject(stringBuffer.toString());
        }
        catch(Exception ex)
        {
            Log.e("App", "yourDataTask"+ex, ex);
            return null;
        }
        finally
        {
            if(bufferedReader != null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.i("PostExecute",jsonString);
        if(jsonObject != null)
            delegate.processFinish(jsonObject);
        else
            delegate.errorSignal();

    }
}