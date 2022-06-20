package com.firstapp.fa_mayanksanjeevnibhoria_c0854281_android;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListPlacesAdapter extends ArrayAdapter
{
    Activity activity;
    List<BeanPlaces> list;

    public ListPlacesAdapter(Activity activity, List<BeanPlaces> list)
    {
        super(activity, R.layout.list_item, list);

        this.activity=activity;
        this.list=list;
    }


    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=activity.getLayoutInflater();

        View v = layoutInflater.inflate(R.layout.list_item,null, true);

        TextView tv1=v.findViewById(R.id.textview1);
        TextView tv2=v.findViewById(R.id.textview2);
        TextView tv3=v.findViewById(R.id.textview3);


        BeanPlaces b = list.get(position);

        if(b.getPv().equalsIgnoreCase("visited"))
        {
            v.setBackgroundColor(Color.RED);
        }

        tv1.setText(b.getPname());
        if(b.getPaddr().equalsIgnoreCase("no address"))
        {
            tv2.setText(b.getPdate());
        }
        else
        {
            tv2.setText(b.getPaddr());
        }
        tv3.setText(b.getPv());

        return v;
    }
}
