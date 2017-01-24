package com.home.nam.iotcontrol;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ButtonSetting extends ActionBarActivity {
    private int SELECT_IMAGE = 1;
    private ItemListAdapter adapter;
    String name[] = {"light", "laptop"};
    String cmdOn[] = {"temp=1", "temp=2"};
    String cmdOff[] = {"p=1", "p=2"};
    ArrayList<Bean> arrayList=new ArrayList<Bean>();

    ImageView imgView;
    Button btChange;
    public static final int REQUEST_CODE_SECOND_ACTIVITY = 100;
    public static final String SHOW_BUTTON = "shouldShowButton";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        for (int i = 0; i < 2; i++) {
            Bean atomPayment = new Bean();
            atomPayment.setName(name[i]);
            atomPayment.setCmdON(cmdOn[i]);
            atomPayment.setCmdOFF(cmdOff[i]);
            arrayList.add(atomPayment);
        }
        adapter = new ItemListAdapter(ButtonSetting.this, R.layout.row_item, arrayList);
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);

/*
        btChange = (Button)findViewById(R.id.btChange);
        btChange.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                //startActivityForResult(new Intent(ButtonSetting.this, ChooseIcon.class), REQUEST_CODE_SECOND_ACTIVITY);
            }
        });
*/
        imgView = (ImageView) findViewById(R.id.img);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == RESULT_OK) {
            //Check if you passed 'true' from the other activity to show the button, and also, only set visibility to VISIBLE if the view is not yet VISIBLE
            if (data.hasExtra(SHOW_BUTTON) && data.getBooleanExtra(SHOW_BUTTON, false) && btChange.getVisibility() != View.VISIBLE) {
                imgView.setImageResource(R.drawable.light_bulb_2);
            }
        }
    }

    public void changeIconClick(View v) {
        startActivityForResult(new Intent(ButtonSetting.this, ChooseIcon.class), REQUEST_CODE_SECOND_ACTIVITY);
        Intent i = new Intent(getApplicationContext(), ChooseIcon.class);
        startActivity(i);
    }

}