package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mayank.gautam99.covid19.Adapters.StateWiseAdapter;
import com.mayank.gautam99.covid19.Models.StateWiseItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class StateActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ArrayList<StateWiseItem> stateWiseItemArrayList;
    private StateWiseAdapter adapter;
    private StateWiseItem stateWiseItem;
    private EditText edSearch;
    private MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Covid-19 Tracker (State)");
        mainActivity = new MainActivity();

        init();
        fetchStateWiseDate();
        search();
    }

    private void search() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterState(s.toString());
            }
        });
    }

    private void filterState(String searchText) {
        ArrayList<StateWiseItem> filteredArray = new ArrayList<>();
        for(int i=0;i<stateWiseItemArrayList.size();i++){
            if(stateWiseItemArrayList.get(i).getState().toLowerCase().contains(searchText.toLowerCase())){
                filteredArray.add(stateWiseItemArrayList.get(i));
            }
        }

        adapter.filteredList(filteredArray,searchText);
    }


    private void fetchStateWiseDate() {
        mainActivity.showProgressDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                JSONArray allStateJsonArray;
                try {
                    allStateJsonArray = response.getJSONArray("statewise");
                    for(int i=1;i<allStateJsonArray.length()-1;i++) {
                        JSONObject statewiseDate = allStateJsonArray.getJSONObject(i);
                        String state = statewiseDate.getString("state");
                        String confirmed = statewiseDate.getString("confirmed");
                        String confirmedNew = statewiseDate.getString("deltaconfirmed");
                        String active = statewiseDate.getString("active");
                        String death = statewiseDate.getString("deaths");
                        String newDeath = statewiseDate.getString("deltadeaths");
                        String recovered = statewiseDate.getString("recovered");
                        String newRecover = statewiseDate.getString("deltarecovered");
                        int newActive = Integer.parseInt(confirmedNew) - Integer.parseInt(newDeath) - Integer.parseInt(newRecover);
                        String dateTime = statewiseDate.getString("lastupdatedtime");


                        stateWiseItem = new StateWiseItem(state, confirmed, confirmedNew, active, death, newDeath, recovered, newRecover, dateTime,String.valueOf(newActive));
                        stateWiseItemArrayList.add(stateWiseItem);
                    }

                    Handler delayToShowProgress = new Handler();
                    delayToShowProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
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

    private void init() {
        edSearch = findViewById(R.id.etSearch);
        stateWiseItemArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.stateWiseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new StateWiseAdapter(this,stateWiseItemArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}