package com.mayank.gautam99.covid19.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mayank.gautam99.covid19.Models.VaccinationModel;
import com.mayank.gautam99.covid19.R;

import java.util.ArrayList;

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.VaccineViewHolder>{
    ArrayList<VaccinationModel> vaccinationList;

    public VaccinationAdapter(ArrayList<VaccinationModel> vaccinationList) {
        this.vaccinationList = vaccinationList;
    }

    @NonNull
    @Override
    public VaccineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_recylerview_item,parent,false);
        return new VaccineViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VaccineViewHolder holder, int position) {
        VaccinationModel curr = vaccinationList.get(position);
        holder.centreName.setText(curr.getCenterName());
        String address = curr.getCenterAddress();
        holder.location.setText(address);
        holder.availability.setText("Availability : "+curr.getAvailableCapacity());
        holder.ageLimit.setText("Age Limit : "+curr.getAgeLimit());
        holder.fee.setText(curr.getFee_type());
        holder.timing.setText("From : " +curr.getCenterFromTime()+" To "+curr.getCenterToTime());
        holder.vaccineName.setText(curr.getVaccineName());

    }

    @Override
    public int getItemCount() {
        return vaccinationList.size();
    }

    static class VaccineViewHolder extends RecyclerView.ViewHolder{
        TextView centreName,location,timing,vaccineName,fee,ageLimit,availability;
        public VaccineViewHolder(@NonNull View itemView) {
            super(itemView);
            centreName = itemView.findViewById(R.id.tvCentreName);
            location = itemView.findViewById(R.id.tvLocation);
            timing = itemView.findViewById(R.id.tvTiming);
            vaccineName = itemView.findViewById(R.id.vaccineName);
            fee = itemView.findViewById(R.id.fees);
            ageLimit = itemView.findViewById(R.id.tvAgeLimit);
            availability = itemView.findViewById(R.id.tvAvailivity);
        }
    }
}
