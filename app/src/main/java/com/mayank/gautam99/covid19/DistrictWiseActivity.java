package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mayank.gautam99.covid19.Adapters.DistrictWiseAdapter;
import com.mayank.gautam99.covid19.Adapters.StateWiseAdapter;
import com.mayank.gautam99.covid19.Models.DistrictWiseModel;
import com.mayank.gautam99.covid19.Models.StateWiseItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DistrictWiseActivity extends AppCompatActivity {
    private ArrayList<DistrictWiseModel> districtItemArrayList;
    private DistrictWiseModel districtWiseModel;
    private EditText edSearch;
    private String stateName;
    private MainActivity mainActivity;
    DistrictWiseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_wise);
        GetIntent();

        Objects.requireNonNull(getSupportActionBar()).setTitle(stateName+" Districts");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainActivity = new MainActivity();

        init();
        fetchDistrict();
        districtSearch();
    }

    private void districtSearch() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterDistrict(s.toString());
            }
        });
    }

    private void filterDistrict(String searchDistrict) {
        ArrayList<DistrictWiseModel> filterList = new ArrayList<>();
        for(int i=0;i<districtItemArrayList.size();i++){
            String countryName = districtItemArrayList.get(i).getDistrict().toLowerCase();
            String searchText = searchDistrict.toLowerCase();
            if(countryName.contains(searchText)){
                filterList.add(districtItemArrayList.get(i));
            }
        }
        adapter.filteredDistrict(filterList,searchDistrict);
    }


    private void fetchDistrict() {
        mainActivity.showProgressDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.covid19india.org/v2/state_district_wise.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    districtItemArrayList.clear();

                    for(int i=1;i<response.length();i++){
                        JSONObject jsonObjectState = response.getJSONObject(i);

                        if(stateName.toLowerCase().equals(jsonObjectState.getString("state").toLowerCase())){
                            JSONArray jsonArrayDistrict = jsonObjectState.getJSONArray("districtData");

                            for(int j=0;j<jsonArrayDistrict.length();j++){
                                JSONObject jsonObjectDistrict = jsonArrayDistrict.getJSONObject(j);
                                String district = jsonObjectDistrict.getString("district");
                                String confirmed = jsonObjectDistrict.getString("confirmed");
                                String active = jsonObjectDistrict.getString("active");
                                String death = jsonObjectDistrict.getString("deceased");
                                String recovered = jsonObjectDistrict.getString("recovered");

                                JSONObject jsonObjectDistNew = jsonObjectDistrict.getJSONObject("delta");
                                String confirmed_new = jsonObjectDistNew.getString("confirmed");
                                String recovered_new = jsonObjectDistNew.getString("recovered");
                                String death_new = jsonObjectDistNew.getString("deceased");

                                districtWiseModel = new DistrictWiseModel(district,confirmed,active,recovered,death,confirmed_new,recovered_new,death_new);
                                districtItemArrayList.add(districtWiseModel);
                            }
                           break;
                        }
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
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
                Log.i("Error in dist. Activity",error.getLocalizedMessage());
            }
        });

        queue.add(jsonArrayRequest);

    }

    private void init() {
        edSearch = findViewById(R.id.districtEditTextSearch);
        districtItemArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.districtWiseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


       adapter = new DistrictWiseAdapter(this, districtItemArrayList);
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

    private void GetIntent() {
        Intent intent = getIntent();
        stateName = intent.getStringExtra("stateName");
    }
}