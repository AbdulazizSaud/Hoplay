package com.example.kay.hoplay.Services;


import android.os.AsyncTask;


import com.example.kay.hoplay.Interfaces.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

//

public class GetAPI extends AsyncTask<String,String,String> implements Constants{

    public HashMap<String,String> data = new HashMap<>();
    public static final String REGISTER="1";
    public static final String LOGIN="2";


    public GetAPI()
    {
        super();

    }

    @Override
    protected String doInBackground(String... params)  {

        try {
            return doWork(params);
        }
        catch (ConnectException e) {
            return ErrorHandler.ERROR_CONNECTION;
        } catch (UnsupportedEncodingException e) {
            return ErrorHandler.ERROR_CONNECTION;
        } catch (ProtocolException e) {
            return ErrorHandler.ERROR_CONNECTION;
        } catch (MalformedURLException e) {
            return ErrorHandler.ERROR_CONNECTION;
        } catch (IOException e) {
            return ErrorHandler.ERROR_IO_EXP;
        }

    }


    private String doWork(String... params) throws
            ConnectException, UnsupportedEncodingException,ProtocolException,MalformedURLException,IOException{

        switch (params[0]){
            case REGISTER :
                return doPostRequest(REGISTER_PATH);

            case LOGIN:
                return doPostRequest(LOGIN_PATH);

            default:
                return ErrorHandler.ERROR_PATH;
        }

    }

    private HttpURLConnection makeHttp(String url) throws ProtocolException,IOException{
        URL _url = new URL(url);


        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setReadTimeout(3000);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();
        return conn;
    }

    private String doPostRequest(String url) throws IOException{
        HttpURLConnection connection = makeHttp(url);


        OutputStream os = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(data));

        writer.flush();
        writer.close();
        os.close();
        return doPostResponse(connection);
    }

    private String doPostResponse(HttpURLConnection connection) throws IOException{
        String res="";
        if ( connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null) {
                res += line;
            }
            br.close();

        } else {
            return ErrorHandler.ERROR_CONNECTION;
        }
        return res;

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }





}
