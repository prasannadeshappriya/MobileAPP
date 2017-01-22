package com.a14roxgmail.prasanna.mobileapp.ListAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.a14roxgmail.prasanna.mobileapp.Model.Entry;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/22/2017.
 */
public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Entry> arrEntry;

    public CalendarAdapter(Context context, ArrayList<Entry> arrEntry) {
        this.context = context;
        this.arrEntry = arrEntry;
    }

    @Override
    public int getCount() {
        return arrEntry.size();
    }

    @Override
    public Object getItem(int i) {
        return arrEntry.get(i);
    }

    @Override
    public long getItemId(int i) {
        return arrEntry.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_calendar,null);

        ListView lstListView = (ListView) v.findViewById(R.id.lstCalenderView);
        TextView tvDetails = (TextView)v.findViewById(R.id.adapterCalendarDetails);

        String day = arrEntry.get(i).getDay();
        String Month = arrEntry.get(i).getMonth();
        ArrayList<String> arrDetails = arrEntry.get(i).getEvents();

        tvDetails.setText("day:- " + day + ", month :- " +  Month);
        ArrayAdapter adapter  = new ArrayAdapter(context,android.R.layout.simple_list_item_1,arrDetails);
        lstListView.setAdapter(adapter);
        Utility.setListViewHeightBasedOnItems(lstListView);

        return v;
    }
}
