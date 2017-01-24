package com.home.nam.iotcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView simpleGrid;
    int flags[] = {R.drawable.flag, R.drawable.vietnam};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleGrid = (GridView) findViewById(R.id.simpleGridView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), flags);
        simpleGrid.setAdapter(customAdapter);
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getBaseContext(), "position: " + position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CheckConnection:
            {
                Toast.makeText(getBaseContext(),"CheckConnection",Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.ButtonSetting:
            {
                Intent i = new Intent(getApplicationContext(), ButtonSetting.class);
                startActivity(i);
                return true;
            }
            case R.id.About:
                // refresh
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
