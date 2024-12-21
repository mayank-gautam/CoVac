package com.mayank.gautam99.covid19.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mayank.gautam99.covid19.EachDistrictActivity;
import com.mayank.gautam99.covid19.EachStateActivity;
import com.mayank.gautam99.covid19.Models.DistrictWiseModel;
import com.mayank.gautam99.covid19.Models.StateWiseItem;
import com.mayank.gautam99.covid19.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistrictWiseAdapter extends RecyclerView.Adapter<DistrictWiseAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<DistrictWiseModel> districtWiseModelArrayList;
    private String searchDistrict="";

    public DistrictWiseAdapter(Context mContext, ArrayList<DistrictWiseModel> districtWiseModelArrayList) {
        this.mContext = mContext;
        this.districtWiseModelArrayList = districtWiseModelArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.state_or_district_item,parent,false);
        districtWiseModelArrayList.sort(new Comparator<DistrictWiseModel>() {
            @Override
            public int compare(DistrictWiseModel o1, DistrictWiseModel o2) {
                return Integer.parseInt(o2.getConfirmed())-Integer.parseInt(o1.getConfirmed());            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DistrictWiseModel currDistrict = districtWiseModelArrayList.get(position);

        String districtName = currDistrict.getDistrict();
        String totalCases = NumberFormat.getInstance().format(Integer.parseInt(currDistrict.getConfirmed()));
        holder.districtTotalCases.setText(totalCases);

        if(searchDistrict.length()==0){
            holder.districtName.setText(districtName);
        }else{
            Pattern pattern = Pattern.compile(searchDistrict.toLowerCase());
            Matcher matcher = pattern.matcher(districtName.toLowerCase());
            SpannableStringBuilder sb = new SpannableStringBuilder(districtName);
            while(matcher.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(110, 195, 235));
                sb.setSpan(fcs, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            holder.districtName.setText(sb);
        }

        holder.districtClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EachDistrictActivity.class);
                DistrictWiseModel clickedItem = districtWiseModelArrayList.get(holder.getAdapterPosition());
                intent.putExtra("district",clickedItem.getDistrict());
                intent.putExtra("confirmed",clickedItem.getConfirmed());
                intent.putExtra("confirmedNew",clickedItem.getNewConfirmed());
                intent.putExtra("active",clickedItem.getActive());
                intent.putExtra("death",clickedItem.getDecreased());
                intent.putExtra("deathNew",clickedItem.getNewDecreased());
                intent.putExtra("recovered",clickedItem.getRecovered());
                intent.putExtra("recoveredNew",clickedItem.getNewRecovered());
                mContext.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return districtWiseModelArrayList.size();
    }

    public void filteredDistrict(ArrayList<DistrictWiseModel> filterList, String searchDistrict){
        this.searchDistrict=searchDistrict;
        districtWiseModelArrayList=filterList;
        notifyDataSetChanged();
    }



     static class ViewHolder extends RecyclerView.ViewHolder{
        TextView districtName, districtTotalCases;
        LinearLayout districtClicked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            districtName = itemView.findViewById(R.id.stateName);
            districtTotalCases = itemView.findViewById(R.id.stateTotalCases);
            districtClicked = itemView.findViewById(R.id.stateWiseDataClicked);
        }
    }
}
