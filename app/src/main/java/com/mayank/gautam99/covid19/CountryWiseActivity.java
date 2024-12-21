package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mayank.gautam99.covid19.Adapters.CountryWiseAdapter;
import com.mayank.gautam99.covid19.Adapters.DistrictWiseAdapter;
import com.mayank.gautam99.covid19.Models.CountryWiseModel;
import com.mayank.gautam99.covid19.Models.DistrictWiseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CountryWiseActivity extends AppCompatActivity {
    private ArrayList<CountryWiseModel> countryWiseModelArrayList;
    private CountryWiseModel countryWiseModel;
    private EditText edSearch;
    private String stateName;
    private CountryWiseAdapter countryWiseAdapter;
    private MainActivity mainActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_wise);


        Objects.requireNonNull(getSupportActionBar()).setTitle("World Data (Select Country)");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainActivity = new MainActivity();
        init();
        fetchCountryData();
        countrySearch();
    }

    private void countrySearch() {
            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filterCountry(s.toString());
                }
            });

    }

    private void filterCountry(String searchCountryText) {
        ArrayList<CountryWiseModel> filterList = new ArrayList<>();
        for(int i=0;i<countryWiseModelArrayList.size();i++){
            String countryName = countryWiseModelArrayList.get(i).getCountry().toLowerCase();
            String searchText = searchCountryText.toLowerCase();
            if(countryName.contains(searchText)){
                filterList.add(countryWiseModelArrayList.get(i));
            }
        }
        countryWiseAdapter.fillterCountry(filterList,searchCountryText);
    }

    private void fetchCountryData() {
        mainActivity.showProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://corona.lmao.ninja/v2/countries";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                countryWiseModelArrayList.clear();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject jsonObjectCountry = response.getJSONObject(i);

                        String countryName = jsonObjectCountry.getString("country");
                        String confirmed = jsonObjectCountry.getString("cases");
                        String confirmedNew = jsonObjectCountry.getString("todayCases");
                        String death = jsonObjectCountry.getString("deaths");
                        String deathNew = jsonObjectCountry.getString("todayDeaths");
                        String activte = jsonObjectCountry.getString("active");
                        String tests = jsonObjectCountry.getString("tests");
                        String recovered = jsonObjectCountry.getString("recovered");
                        JSONObject countryInfo = jsonObjectCountry.getJSONObject("countryInfo");
                        String flagUrl = countryInfo.getString("flag");
                        String newRecover = jsonObjectCountry.getString("todayRecovered");

                        CountryWiseModel countryWiseModel = new CountryWiseModel(countryName,confirmed,confirmedNew,activte,death,deathNew,recovered,tests,flagUrl,newRecover);
                        countryWiseModelArrayList.add(countryWiseModel);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                countryWiseAdapter.notifyDataSetChanged();
                                mainActivity.dismissProgressDialog();
                            }
                        },1000);

                    } catch (JSONException e) {
                        mainActivity.dismissProgressDialog();
                        Toast.makeText(mainActivity, "Unexpected response code 502\nTry After Sometime", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainActivity.dismissProgressDialog();
                Toast.makeText(mainActivity, "Unexpected response code 502\nTry After Sometime", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void init() {
        edSearch = findViewById(R.id.countryEditTextSearch);
        countryWiseModelArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.countryWiseRecyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(CountryWiseActivity.this));

        countryWiseAdapter = new CountryWiseAdapter(this, countryWiseModelArrayList);
        recyclerView.setAdapter(countryWiseAdapter);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}