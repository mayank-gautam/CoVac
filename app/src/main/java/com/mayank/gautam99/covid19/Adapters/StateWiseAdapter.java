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

import com.mayank.gautam99.covid19.EachStateActivity;
import com.mayank.gautam99.covid19.Models.StateWiseItem;
import com.mayank.gautam99.covid19.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StateWiseAdapter extends RecyclerView.Adapter<StateWiseAdapter.ViewHolder> {

    private final Context mContext;
    private  ArrayList<StateWiseItem> stateWiseItemArrayList;
    private String textSearch = "";
    SpannableStringBuilder sb;

    public StateWiseAdapter(Context context, ArrayList<StateWiseItem> stateWiseItemArrayList) {
        this.mContext = context;
        this.stateWiseItemArrayList = stateWiseItemArrayList;
    }

    public void filteredList(ArrayList<StateWiseItem> filterList,String textSearch){
        this.textSearch = textSearch;
        stateWiseItemArrayList = filterList;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.state_or_district_item,parent,false);
        stateWiseItemArrayList.sort(new Comparator<StateWiseItem>() {
            @Override
            public int compare(StateWiseItem o1, StateWiseItem o2) {
                return Integer.parseInt(o2.getConfirmed())-Integer.parseInt(o1.getConfirmed());
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StateWiseItem currentItem = stateWiseItemArrayList.get(position);
        String stateName = currentItem.getState();
        String totalCases = NumberFormat.getInstance().format(Integer.parseInt(currentItem.getConfirmed()));
        holder.stateTotalCases.setText(totalCases);

        if(textSearch.length()==0){
            holder.stateName.setText(stateName);
        }else{
            sb = new SpannableStringBuilder(stateName);
            Pattern pattern = Pattern.compile(textSearch.toLowerCase());
            Matcher matcher = pattern.matcher(stateName.toLowerCase());
            while(matcher.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(110, 195, 235));
                sb.setSpan(fcs, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            holder.stateName.setText(sb);
        }


        holder.stateClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EachStateActivity.class);
                StateWiseItem clickedItem = stateWiseItemArrayList.get(holder.getAdapterPosition());
                intent.putExtra("state",clickedItem.getState());
                intent.putExtra("confirmed",clickedItem.getConfirmed());
                intent.putExtra("confirmedNew",clickedItem.getConfirmedNew());
                intent.putExtra("active",clickedItem.getActive());
                intent.putExtra("death",clickedItem.getDeath());
                intent.putExtra("deathNew",clickedItem.getDeathNew());
                intent.putExtra("recovered",clickedItem.getRecovered());
                intent.putExtra("recoveredNew",clickedItem.getRecoveredNew());
                intent.putExtra("lastUpdate",clickedItem.getLastUpdate());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stateWiseItemArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView stateName, stateTotalCases;
        LinearLayout stateClicked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stateName = itemView.findViewById(R.id.stateName);
            stateTotalCases = itemView.findViewById(R.id.stateTotalCases);
            stateClicked = itemView.findViewById(R.id.stateWiseDataClicked);
        }
    }
}
