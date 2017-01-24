package com.home.nam.iotcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Nam on 1/19/2017.
 */

public class ChooseIcon extends ActionBarActivity {
    int icon[] = {R.drawable.light_bulb, R.drawable.light_bulb_2, R.drawable.ac, R.drawable.camera, R.drawable.tv,
            R.drawable.heater, R.drawable.laptop, R.drawable.refrigerator, R.drawable.shower, R.drawable.washing_machine};
    GridView simpleGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_icon);

        simpleGrid = (GridView) findViewById(R.id.chooseIcon_gridview);
        ChooseIcon_Adapter iconAdapter = new ChooseIcon_Adapter(getApplicationContext(), icon);
        simpleGrid.setAdapter(iconAdapter);
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getBaseContext(), "position: " + position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(ButtonSetting.SHOW_BUTTON, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }


}
