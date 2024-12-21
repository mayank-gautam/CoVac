package com.mayank.gautam99.covid19.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mayank.gautam99.covid19.EachCountryActivity;
import com.mayank.gautam99.covid19.EachDistrictActivity;
import com.mayank.gautam99.covid19.Models.CountryWiseModel;
import com.mayank.gautam99.covid19.Models.DistrictWiseModel;
import com.mayank.gautam99.covid19.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountryWiseAdapter extends RecyclerView.Adapter<CountryWiseAdapter.CountryViewHolder> {
    private final Context mContext;
    private ArrayList<CountryWiseModel> countryWiseModelArrayList;
    private String searchCountryText="";
    SpannableStringBuilder sb;

    public CountryWiseAdapter(Context mContext, ArrayList<CountryWiseModel> countryWiseModelArrayList) {
        this.mContext = mContext;
        this.countryWiseModelArrayList = countryWiseModelArrayList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_countrywise,parent,false);
        countryWiseModelArrayList.sort(new Comparator<CountryWiseModel>() {
            @Override
            public int compare(CountryWiseModel o1, CountryWiseModel o2) {
                return Integer.parseInt(o2.getConfirmed())-Integer.parseInt(o1.getConfirmed());
            }
        });
        return new CountryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryWiseModel currentItem = countryWiseModelArrayList.get(position);
        String countryName = currentItem.getCountry();
        holder.tvRankTextView.setText(position+1+".");
        holder.tvCountryTotalCase.setText(NumberFormat.getInstance().format(Integer.parseInt(currentItem.getConfirmed())));
        Glide.with(mContext).load(currentItem.getFlag()).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.flag);

        if(searchCountryText.length()==0){
            holder.tvCountryName.setText(countryName);
        }else{
            sb = new SpannableStringBuilder(countryName);
            Pattern pattern = Pattern.compile(searchCountryText.toLowerCase());
            Matcher matcher = pattern.matcher(countryName.toLowerCase());
            while(matcher.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(110, 195, 235));
                sb.setSpan(fcs, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            holder.tvCountryName.setText(sb);
        }

        holder.linCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EachCountryActivity.class);
                CountryWiseModel clickedItem = countryWiseModelArrayList.get(holder.getAdapterPosition());
                intent.putExtra("country",clickedItem.getCountry());
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
        return countryWiseModelArrayList.size();
    }

    public void fillterCountry(ArrayList<CountryWiseModel> filterList, String searchCountry) {
        countryWiseModelArrayList = filterList;
        this.searchCountryText = searchCountry;
        notifyDataSetChanged();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder{
        TextView tvCountryName,tvCountryTotalCase,tvRankTextView;
        ImageView flag;
        LinearLayout linCountry;
        ProgressBar progressBar;

        @SuppressLint("CutPasteId")
        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountryName = itemView.findViewById(R.id.layoutCountryWiseCountryNameTextView);
            tvCountryTotalCase = itemView.findViewById(R.id.layoutCountryWiseConfirmedTextView);
            tvRankTextView = itemView.findViewById(R.id.layoutCountryWiseRank);
            flag = itemView.findViewById(R.id.layoutCountryWiseFlagImage);
            linCountry = itemView.findViewById(R.id.countryWiseLinerLayout);
//            progressBar = itemView.findViewById(R.id.layoutProgressBar);
        }
    }
}
