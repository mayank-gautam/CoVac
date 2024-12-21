package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.Objects;

public class EachCountryActivity extends AppCompatActivity {
    private TextView tvConfirmed, tvConfirmedNew, tvActive, tvActiveNew, tvRecovered, tvRecoveredNew, tvDeath,
            tvDeathNew;

    private String confirmed, confirmedNew, active, activeNew, recovered, recoveredNew, death,
            deathNew,country;

    private PieChart pieChart;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_country);

        init();
        getAllValue();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(country);
        setAllValue();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getAllValue() {
        Intent intent = getIntent();

        country = intent.getStringExtra("country");
        confirmed = intent.getStringExtra("confirmed");
        confirmedNew = intent.getStringExtra("confirmedNew");
        active = intent.getStringExtra("active");
        death = intent.getStringExtra("death");
        deathNew = intent.getStringExtra("deathNew");
        recovered = intent.getStringExtra("recovered");
        recoveredNew = intent.getStringExtra("recoveredNew");
        int activeInt = Integer.parseInt(confirmedNew) - Integer.parseInt(recoveredNew)-Integer.parseInt(deathNew);
        activeNew = String.valueOf(activeInt);

    }

    @SuppressLint("SetTextI18n")
    private void setAllValue() {
        tvConfirmed.setText(NumberFormat.getInstance().format(Long.parseLong(confirmed)));
        tvConfirmedNew.setText(NumberFormat.getInstance().format(Long.parseLong(confirmedNew)));
        tvActive.setText(NumberFormat.getInstance().format(Long.parseLong(active)));
        tvDeath.setText(NumberFormat.getInstance().format(Long.parseLong(death)));
        tvDeathNew.setText(NumberFormat.getInstance().format(Long.parseLong(deathNew)));
        tvRecovered.setText(NumberFormat.getInstance().format(Long.parseLong(recovered)));
        tvRecoveredNew.setText(NumberFormat.getInstance().format(Long.parseLong(recoveredNew)));
        tvActiveNew.setText(NumberFormat.getInstance().format(Long.parseLong(activeNew)));
        showPieChart();
    }

    private void showPieChart() {

        pieChart.addPieSlice(new PieModel("Confirmed",Long.parseLong(confirmed), Color.parseColor("#FF9100")));
        pieChart.addPieSlice(new PieModel("Active",Long.parseLong(active),Color.parseColor("#007afe")));
        pieChart.addPieSlice(new PieModel("Recovered",Long.parseLong(recovered),Color.parseColor("#08a045")));
        pieChart.addPieSlice(new PieModel("Death",Long.parseLong(death),Color.parseColor("#F6404F")));

        pieChart.startAnimation();

    }

    private void init() {
        tvConfirmed = findViewById(R.id.activity_eachCountry_confirmed_textview);
        tvConfirmedNew = findViewById(R.id.activity_eachCountry_confirmed_new_textview);

        tvActive = findViewById(R.id.activity_eachCountry_active_textview);
        tvActiveNew = findViewById(R.id.activity_eachCountry_active_new_textview);

        tvRecovered = findViewById(R.id.activity_eachCountry_recovered_textview);
        tvRecoveredNew = findViewById(R.id.activity_eachCountry_recovered_new_textview);

        tvDeath = findViewById(R.id.activity_eachCountry_death_textview);
        tvDeathNew = findViewById(R.id.activity_eachCountry_death_new_textview);
        pieChart = findViewById(R.id.pieChartEachCountry);

    }
}