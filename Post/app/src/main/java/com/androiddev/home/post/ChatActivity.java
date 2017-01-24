package com.androiddev.home.post;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nam on 1/22/2017.
 */

public class ChatActivity extends Activity {

    String web = "http://luutruthongtin.net46.net/post.php";


    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    String messageText = null;
    String lastMsg = null;
    String data = null;
    String lastData = null;
    boolean isDone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initControls();

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if(isDone){
                                isDone = false;
                                new HttpTask().execute("http://luutruthongtin.net46.net/post_data.html");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000); //execute in every 50000 ms
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                messageET.setText("");
                displayMessage(chatMessage);

                lastMsg = messageText;
                messageText = "|" + messageText + "|";
                if (messageText.length()>0)     new MyHttpPost().execute();

            }
        });
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
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
                nameValuePairs.add(new BasicNameValuePair("message", messageText));
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

            start_data = response.indexOf("|");
            end_data = response.lastIndexOf("|");
            data = response.substring(start_data+1, end_data);
            if(!data.equals(lastData) && !data.equals(lastMsg)) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(1);
                chatMessage.setMessage(data);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(false);

                messageET.setText("");
                displayMessage(chatMessage);
                lastData = data;
            }
            data = null;
            isDone = true;
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
