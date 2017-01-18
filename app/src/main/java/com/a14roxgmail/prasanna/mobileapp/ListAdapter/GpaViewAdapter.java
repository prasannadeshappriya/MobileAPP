package com.a14roxgmail.prasanna.mobileapp.ListAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;
import com.a14roxgmail.prasanna.mobileapp.R;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class GpaViewAdapter extends BaseAdapter {
    List<GPA> lstGpa;
    Context context;

    public GpaViewAdapter(Context context, List<GPA> lstGpa) {
        this.context = context;
        this.lstGpa = lstGpa;
    }

    @Override
    public int getCount() {
        return lstGpa.size();
    }

    @Override
    public Object getItem(int i) {
        return lstGpa.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstGpa.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_gpa_view,null);

        TextView tvSemName = (TextView)v.findViewById(R.id.adapterVWSemName);
        TextView tvGPAValue = (TextView)v.findViewById(R.id.adapterVWGpa);
        TextView tvGpaType = (TextView)v.findViewById(R.id.adapterVWGpaType);

        String type = lstGpa.get(i).getType();

        if(type.toLowerCase().equals("sgpa")){
            tvSemName.setText("Semester " + lstGpa.get(i).getSemester());
        }else{
            tvSemName.setText("Overall Status");
        }
        tvGpaType.setText(lstGpa.get(i).getType());
        tvGPAValue.setText(lstGpa.get(i).getGpa());

        return v;
    }
}
