package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView tvConfirmed, tvConfirmedNew, tvActive, tvActiveNew, tvRecovered, tvRecoveredNew, tvDeath,
            tvDeathNew, tvSampledTest, tvSampledTestNew, tvTime,tvDate;

    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private ProgressDialog progressDialog;
    private LinearLayout stateData, worldData;
    private boolean backPressedForExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Covid-19 Tracker (India)");

        importAllView();
        fetchData();
        refreshLayout();

        stateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,StateActivity.class));
            }
        });

        worldData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WorldActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {

       if(backPressedForExit){
            super.onBackPressed();
            return;
        }

        backPressedForExit = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedForExit = false;
            }
        },3000);
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

    private void importAllView() {
        tvConfirmed = findViewById(R.id.activity_main_confirmed_textview);
        tvConfirmedNew = findViewById(R.id.activity_main_confirmed_new_textview);

        tvActive = findViewById(R.id.activity_main_active_textview);
        tvActiveNew = findViewById(R.id.activity_main_active_new_textview);

        tvRecovered = findViewById(R.id.activity_main_recovered_textview);
        tvRecoveredNew = findViewById(R.id.activity_main_recovered_new_textview);

        tvDeath = findViewById(R.id.activity_main_death_textview);
        tvDeathNew = findViewById(R.id.activity_main_death_new_textview);

        tvSampledTest = findViewById(R.id.activity_main_sample_textview);
        tvSampledTestNew = findViewById(R.id.activity_main_sample_new_textview);

        tvTime= findViewById(R.id.activity_main_time_textview);
        tvDate = findViewById(R.id.activity_main_lastUpdates_date_textview);

        swipeRefreshLayout = findViewById(R.id.swipeReferce);
        pieChart = findViewById(R.id.pieChart);

        stateData = findViewById(R.id.activity_main_statewise_cardView);
        worldData = findViewById(R.id.activity_main_world_cardView);

    }

    private void fetchData() {
        showProgressDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.covid19india.org/data.json";
        pieChart.clearChart();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                JSONArray allStateJsonArray;
                JSONArray testDataJsonArray;

                try {
                    allStateJsonArray = response.getJSONArray("statewise");
                    testDataJsonArray = response.getJSONArray("tested");

                    JSONObject indiaData = allStateJsonArray.getJSONObject(0);
                    JSONObject testedData = testDataJsonArray.getJSONObject(testDataJsonArray.length()-1);

                    String confirmed = indiaData.getString("confirmed");
                    String confirmedNew = indiaData.getString("deltaconfirmed");
                    String active = indiaData.getString("active");
                    String death = indiaData.getString("deaths");
                    String newDeath = indiaData.getString("deltadeaths");
                    String recovered = indiaData.getString("recovered");
                    String newRecover = indiaData.getString("deltarecovered");
                    int newActive = Integer.parseInt(confirmedNew) - Integer.parseInt(newDeath)-Integer.parseInt(newRecover);
                    String dateTime = indiaData.getString("lastupdatedtime");
                    String data = dateTime.substring(0,dateTime.indexOf(" "));
                    String time = dateTime.substring(dateTime.indexOf(" ")+1);
                    String totalTested = testedData.getString("totalsamplestested");
                    String todayTested = testedData.getString("samplereportedtoday");

                    Handler delayToShowProgress = new Handler();
                    delayToShowProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAllValuesInLayout(confirmed,confirmedNew,active,death,newDeath,recovered,newRecover,newActive,data,time,totalTested,todayTested);
                            pieChart.addPieSlice(new PieModel("Confirmed",Long.parseLong(confirmed),Color.parseColor("#FF9100")));
                            pieChart.addPieSlice(new PieModel("Active",Long.parseLong(active),Color.parseColor("#007afe")));
                            pieChart.addPieSlice(new PieModel("Recovered",Long.parseLong(recovered),Color.parseColor("#08a045")));
                            pieChart.addPieSlice(new PieModel("Death",Long.parseLong(death),Color.parseColor("#F6404F")));
                            pieChart.startAnimation();

                            dismissProgressDialog();

                        }
                    },1000);



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "hai");
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void showProgressDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAllValuesInLayout(String confirmed, String confirmedNew, String active, String death, String newDeath, String recovered,
                                      String newRecover, int newActive, String data, String time, String totalTested, String todayTested) {

        tvConfirmed.setText(NumberFormat.getInstance().format(Long.parseLong(confirmed)));
        tvConfirmedNew.setText(NumberFormat.getInstance().format(Long.parseLong(confirmedNew)));
        tvActive.setText(NumberFormat.getInstance().format(Long.parseLong(active)));
        tvDeath.setText(NumberFormat.getInstance().format(Long.parseLong(death)));
        tvDeathNew.setText(NumberFormat.getInstance().format(Long.parseLong(newDeath)));
        tvRecovered.setText(NumberFormat.getInstance().format(Long.parseLong(recovered)));
        tvRecoveredNew.setText(NumberFormat.getInstance().format(Long.parseLong(newRecover)));
        tvActiveNew.setText(NumberFormat.getInstance().format(newActive));
        tvDate.setText(data);
        tvTime.setText(getTimeFormated(time));

        if(todayTested.length()==0) todayTested = "0";
        if(totalTested.length()==0) totalTested = "0";
//        tvSampledTest.setText(totalTested);
//        tvSampledTestNew.setText(todayTested);

        tvSampledTest.setText(NumberFormat.getInstance().format(Long.parseLong(totalTested)));
        tvSampledTestNew.setText(NumberFormat.getInstance().format(Long.parseLong(todayTested)));

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    public String getTimeFormated(String time) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "null";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.infoMenu == item.getItemId()){
            startActivity(new Intent(this,AboutActivity.class));
        }else if(R.id.vacctineClicked==item.getItemId()){
            startActivity(new Intent(this,VaccinationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}