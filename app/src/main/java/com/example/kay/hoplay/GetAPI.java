package com.example.kay.hoplay;


import android.os.AsyncTask;


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

/**
 * Created by Azoz-LabTop on 10/25/2016.
 */

public class GetAPI extends AsyncTask<String,String,String> {

    public HashMap<String,String> data = new HashMap<>();
    public static final String REGISTER="1";
    public static final String LOGIN="2";


    public GetAPI()
    {
        super();

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String response = "";

            switch (params[0]){
                case REGISTER :
                    response = doPostRequest(Constants.REGISTER_PATH);
                    break;
                case LOGIN:
                    response = doPostRequest(Constants.LOGIN_PATH);
                    break;
                default:
                    checkErros(ErrorHandler.ERROR_PATH);
                    return null;
            }

            return response;

        }
        catch (ConnectException e) {
            checkErros(ErrorHandler.ERROR_CONNECTION);
        } catch (UnsupportedEncodingException e) {
            checkErros(ErrorHandler.ERROR_CONNECTION);
        } catch (ProtocolException e) {
            checkErros(ErrorHandler.ERROR_CONNECTION);
        } catch (MalformedURLException e) {
            checkErros(ErrorHandler.ERROR_CONNECTION);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

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


    private HttpURLConnection makeHttp(String url) throws ProtocolException,IOException{
        URL _url = new URL(url);


        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
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

        }
        return res;

    }

    private String checkErros(String error){
        switch (error){
            case ErrorHandler.ERROR_CONNECTION:
                return "ERROR";
            case ErrorHandler.ERROR_PATH:
                return "ERROR";

        }

        return  "ERROR_PRINT_STACK";
    }

}
