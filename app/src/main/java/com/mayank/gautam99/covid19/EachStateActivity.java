package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class EachStateActivity extends AppCompatActivity {
    private TextView tvConfirmed, tvConfirmedNew, tvActive, tvActiveNew, tvRecovered, tvRecoveredNew, tvDeath,
            tvDeathNew, tvTimeUpdate,tvDistric;

    private String confirmed, confirmedNew, active, activeNew, recovered, recoveredNew, death,
            deathNew, timeUpdate,dateUpdate, state,lastUpdate;

    private PieChart pieChart;
    private LinearLayout districClicked;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_state);

        init();
        getAllValue();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(state);
        setAllValue();

        districClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DistrictWiseActivity.class);
                intent.putExtra("stateName",state);
                startActivity(intent);
            }
        });
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
    private void setAllValue() {

        tvConfirmed.setText(NumberFormat.getInstance().format(Long.parseLong(confirmed)));
        tvConfirmedNew.setText(NumberFormat.getInstance().format(Long.parseLong(confirmedNew)));
        tvActive.setText(NumberFormat.getInstance().format(Long.parseLong(active)));
        tvDeath.setText(NumberFormat.getInstance().format(Long.parseLong(death)));
        tvDeathNew.setText(NumberFormat.getInstance().format(Long.parseLong(deathNew)));
        tvRecovered.setText(NumberFormat.getInstance().format(Long.parseLong(recovered)));
        tvRecoveredNew.setText(NumberFormat.getInstance().format(Long.parseLong(recoveredNew)));
        tvTimeUpdate.setText(dateUpdate+", "+timeUpdate);

        tvDistric.setText("District data of "+state);
        tvActiveNew.setText(NumberFormat.getInstance().format(Long.parseLong(activeNew)));

        showPieChart();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getAllValue() {
        Intent intent = getIntent();

        state = intent.getStringExtra("state");
        confirmed = intent.getStringExtra("confirmed");
        confirmedNew = intent.getStringExtra("confirmedNew");
        active = intent.getStringExtra("active");
        death = intent.getStringExtra("death");
        deathNew = intent.getStringExtra("deathNew");
        recovered = intent.getStringExtra("recovered");
        recoveredNew = intent.getStringExtra("recoveredNew");
        lastUpdate = intent.getStringExtra("lastUpdate");
        int activeNewInt = Integer.parseInt(confirmedNew) - Integer.parseInt(recoveredNew)-Integer.parseInt(deathNew);
        activeNew = String.valueOf(activeNewInt);

         dateUpdate = lastUpdate.substring(0,lastUpdate.indexOf(" "));
         timeUpdate = lastUpdate.substring(lastUpdate.indexOf(" ")+1);
         timeUpdate = getTimeFormated(timeUpdate);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private String getTimeFormated(String time) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            assert dateObj != null;
            return new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "null";
    }

    private void showPieChart() {

        pieChart.addPieSlice(new PieModel("Confirmed",Long.parseLong(confirmed), Color.parseColor("#FF9100")));
        pieChart.addPieSlice(new PieModel("Active",Long.parseLong(active),Color.parseColor("#007afe")));
        pieChart.addPieSlice(new PieModel("Recovered",Long.parseLong(recovered),Color.parseColor("#08a045")));
        pieChart.addPieSlice(new PieModel("Death",Long.parseLong(death),Color.parseColor("#F6404F")));

        pieChart.startAnimation();

    }

    private void init() {
        tvConfirmed = findViewById(R.id.activity_eachState_confirmed_textview);
        tvConfirmedNew = findViewById(R.id.activity_eachState_confirmed_new_textview);

        tvActive = findViewById(R.id.activity_eachState_active_textview);
        tvActiveNew = findViewById(R.id.activity_eachState_active_new_textview);

        tvRecovered = findViewById(R.id.activity_eachState_recovered_textview);
        tvRecoveredNew = findViewById(R.id.activity_eachState_recovered_new_textview);

        tvDeath = findViewById(R.id.activity_eachState_death_textview);
        tvDeathNew = findViewById(R.id.activity_eachState_death_new_textview);

//        tvDateUpdate = findViewById(R.id.activity_eachState_dateUpdate_new_textview);
        tvTimeUpdate = findViewById(R.id.activity_eachState_timeUpdate_textview);
        pieChart = findViewById(R.id.pieChartEachState);

        districClicked= findViewById(R.id.activity_eachState_distric_data_cardView);
        tvDistric = findViewById(R.id.activity_eachState_distric_data_textview);
    }
}