package com.example.matias;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapterModelSklad extends ArrayAdapter<DataModelSklad> implements View.OnClickListener{


    private ArrayList<DataModelSklad> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtpin;
        TextView txtnum;
        TextView txtdata;
        TextView txtnumbank;

    }



    public CustomAdapterModelSklad(ArrayList<DataModelSklad> data, Context context) {
        super(context, R.layout.row_sclad, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelSklad dataModel=(DataModelSklad)object;



//
//        switch (v.getId())
//        {
//
//            case R.id.item_info:
//
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//
//                break;
//
//
//        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelSklad dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_sclad, parent, false);
            viewHolder.txtpin = convertView.findViewById(R.id.sklad_fio);
            viewHolder.txtnum = convertView.findViewById(R.id.sklad_nomer_korobki);
            viewHolder.txtdata = convertView.findViewById(R.id.sklad_data);
            viewHolder.txtnumbank = convertView.findViewById(R.id.sklad_nomer_banka);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;



        viewHolder.txtpin.setText(dataModel.getpin());
        viewHolder.txtnum.setText(dataModel.getnum());
        viewHolder.txtdata.setText(dataModel.getdata());
        viewHolder.txtnumbank.setText(dataModel.getnumbank());

        return convertView;
    }


}







