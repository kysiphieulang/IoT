package com.home.nam.iotcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Nam on 1/19/2017.
 */

public class ChooseIcon_Adapter extends BaseAdapter {
    Context context;
    int icon[];
    LayoutInflater inflter;

    public ChooseIcon_Adapter(Context applicationContext, int[] icon) {
        this.context = applicationContext;
        this.icon = icon;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return icon.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null);
        ImageView icon_view = (ImageView) view.findViewById(R.id.icon);
        icon_view.setImageResource(icon[i]);
        return view;
    }
}
