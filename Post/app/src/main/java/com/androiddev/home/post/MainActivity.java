package com.androiddev.home.post;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    String web = "";
    String msg = "";
    Button sendButton;
    Button btGet;
    Button btOpenChat;
    EditText txtSend;
    EditText txtcontent;
    EditText txtAddress;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtSend = (EditText) findViewById(R.id.txtsend);
        txtcontent = (EditText) findViewById(R.id.txtcontent);


        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = txtSend.getText().toString();
                web = txtAddress.getText().toString();
                if (msg.length()>0)     new MyHttpPost().execute();
                else                    Toast.makeText(getBaseContext(),"All field are required",Toast.LENGTH_SHORT).show();

                txtSend.setText("");
            }
        });

        btGet = (Button) findViewById(R.id.btGet);
        btGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpTask().execute("http://luutruthongtin.net46.net/post_data.html");
            }
        });

        btOpenChat = (Button) findViewById(R.id.btChatOpen);
        btOpenChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
            }
        });
    }

    class MyHttpPost extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params)
        {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(web);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("message", msg));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();
                InputStream result = httpEntity.getContent();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return true;
        }

    }

    private class HttpTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            // TODO Auto-generated method stub
            String response = getWebPage(urls[0]);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            int start_data, end_data;
            String data = "";

            start_data = response.indexOf("|");
            end_data = response.lastIndexOf("|");
            for (int i = start_data+1; i < end_data; i++) {
                data = data + response.charAt(i);
            }
            txtcontent.setText(data);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

    }

    public String getWebPage(String adresse) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet();

        InputStream inputStream = null;

        String response = null;

        try {
            URI uri = new URI(adresse);
            httpGet.setURI(uri);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            //int statutCode = httpResponse.getStatusLine().getStatusCode();
            //int length = (int) httpResponse.getEntity().getContentLength();

            inputStream = httpResponse.getEntity().getContent();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            int inChar;
            StringBuffer stringBuffer = new StringBuffer();

            while ((inChar = reader.read()) != -1) {
                stringBuffer.append((char) inChar);
            }

            response = stringBuffer.toString();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (inputStream != null)
                inputStream.close();
        } catch (IOException e) {
        }


        return response;
    }

}