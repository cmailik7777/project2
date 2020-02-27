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

import java.util.ArrayList;

public class CustomZapros  extends ArrayAdapter<DataZapros> implements View.OnClickListener{
    private ArrayList<DataZapros> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {

        TextView txtnum;
        TextView txtnumbank;

    }



    public CustomZapros(ArrayList<DataZapros> data, Context context) {
        super(context, R.layout.row_zapros, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataZapros dataZapros=(DataZapros)object;




    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataZapros dataZapros = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_sclad, parent, false);
            viewHolder.txtnum = convertView.findViewById(R.id.zapros_nomer_korobki);
            viewHolder.txtnumbank = convertView.findViewById(R.id.zapros_nomer_banka);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;



        viewHolder.txtnum.setText(dataZapros.getnum1());
        viewHolder.txtnumbank.setText(dataZapros.getnumbank1());

        return convertView;
    }


}
