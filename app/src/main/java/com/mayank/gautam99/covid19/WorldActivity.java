package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mayank.gautam99.covid19.Models.StateWiseItem;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Objects;

public class WorldActivity extends AppCompatActivity {

    private TextView tvConfirmed, tvConfirmedNew, tvActive, tvActiveNew, tvRecovered, tvRecoveredNew, tvDeath,
            tvDeathNew, tvSampledTest;

    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private LinearLayout countryButton;
    private final MainActivity mainActivity = new MainActivity();
    private StateWiseItem stateWiseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Covid-19 Tracker (World)");


        init();
        fetchData();
        refreshLayout();

        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorldActivity.this,CountryWiseActivity.class));
            }
        });
    }

    private void refreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
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

    private void fetchData() {
        mainActivity.showProgressDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://corona.lmao.ninja/v2/all";
        pieChart.clearChart();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String confirmed = response.getString("cases");
                  String confirmedNew = response.getString("todayCases");
                    String active = response.getString("active");
                    String death = response.getString("deaths");
                    String newDeath = response.getString("todayDeaths");
                    String recovered = response.getString("recovered");
                    String newRecover = response.getString("todayRecovered");
                    int newActive = Integer.parseInt(confirmedNew) - Integer.parseInt(newDeath)-Integer.parseInt(newRecover);
                    String totalTested = response.getString("tests");

                    Handler delayToShowProgress = new Handler();
                    delayToShowProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAllValuesInLayout(confirmed,confirmedNew,active,death,newDeath,recovered,newRecover,newActive,totalTested);
                            pieChart.addPieSlice(new PieModel("Confirmed",Long.parseLong(confirmed), Color.parseColor("#FF9100")));
                            pieChart.addPieSlice(new PieModel("Active",Long.parseLong(active),Color.parseColor("#007afe")));
                            pieChart.addPieSlice(new PieModel("Recovered",Long.parseLong(recovered),Color.parseColor("#08a045")));
                            pieChart.addPieSlice(new PieModel("Death",Long.parseLong(death),Color.parseColor("#F6404F")));
                            pieChart.startAnimation();

                            mainActivity.dismissProgressDialog();

                        }
                    },1000);



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error",error.networkResponse.toString());
            }
        });

        queue.add(jsonObjectRequest);
    }

    @SuppressLint("SetTextI18n")
    private void setAllValuesInLayout(String confirmed, String confirmedNew, String active, String death, String newDeath, String recovered,
                                      String newRecover, int newActive, String totalTested) {

        tvConfirmed.setText(NumberFormat.getInstance().format(Long.parseLong(confirmed)));
        tvConfirmedNew.setText(NumberFormat.getInstance().format(Long.parseLong(confirmedNew)));
        tvActive.setText(NumberFormat.getInstance().format(Long.parseLong(active)));
        tvDeath.setText(NumberFormat.getInstance().format(Long.parseLong(death)));
        tvDeathNew.setText(NumberFormat.getInstance().format(Long.parseLong(newDeath)));
        tvRecovered.setText(NumberFormat.getInstance().format(Long.parseLong(recovered)));
        tvRecoveredNew.setText(NumberFormat.getInstance().format(Long.parseLong(newRecover)));
        tvActiveNew.setText(NumberFormat.getInstance().format(newActive));

        tvSampledTest.setText(NumberFormat.getInstance().format(Long.parseLong(totalTested)));
    }

    private void init() {
        tvConfirmed = findViewById(R.id.activity_world_confirmed_textview);
        tvConfirmedNew = findViewById(R.id.activity_world_confirmed_new_textview);

        tvActive = findViewById(R.id.activity_world_active_textview);
        tvActiveNew = findViewById(R.id.activity_world_active_new_textview);

        tvRecovered = findViewById(R.id.activity_world_recovered_textview);
        tvRecoveredNew = findViewById(R.id.activity_world_recovered_new_textview);

        tvDeath = findViewById(R.id.activity_world_death_textview);
        tvDeathNew = findViewById(R.id.activity_world_death_new_textview);

        tvSampledTest = findViewById(R.id.activity_world_sample_textview);
        swipeRefreshLayout = findViewById(R.id.swipeReferceWorld);
        pieChart = findViewById(R.id.pieChartWorld);

         countryButton= findViewById(R.id.activity_world_country_data_cardView);
    }
}