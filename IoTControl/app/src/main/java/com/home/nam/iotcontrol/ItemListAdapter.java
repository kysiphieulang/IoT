package com.home.nam.iotcontrol;

/**
 * Created by Nam on 1/16/2017.
 */

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter<Bean>
{
    protected static final String LOG_TAG = ItemListAdapter.class.getSimpleName();

    private ArrayList<Bean> items;
    private int layoutResourceId;
    private Context context;

    public ItemListAdapter(Context context, int layoutResourceId, ArrayList<Bean> items)
    {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.bean = items.get(position);

        holder.cmdOn = (EditText)row.findViewById(R.id.cmdOn);
        holder.cmdOff = (EditText)row.findViewById(R.id.cmdOff);
        holder.cmdName=(EditText) row.findViewById(R.id.cmdName);

        row.setTag(holder);

        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.cmdOn.setText(String.valueOf(holder.bean.getCmdON()));
        holder.cmdOff.setText(String.valueOf(holder.bean.getCmdOFF()));
        holder.cmdName.setText(String.valueOf(holder.bean.getName()));
    }

    public static class Holder {
        Bean bean;
        EditText cmdOn;
        EditText cmdOff;
        EditText cmdName;
        ImageView imgChange;

    }
}
