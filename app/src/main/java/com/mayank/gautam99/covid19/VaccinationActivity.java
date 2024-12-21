package com.mayank.gautam99.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mayank.gautam99.covid19.Adapters.VaccinationAdapter;
import com.mayank.gautam99.covid19.Models.VaccinationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class VaccinationActivity extends AppCompatActivity {
     EditText edPincode;
     Button searchButton;
     ArrayList<VaccinationModel> vaccinationModelArrayList;
     VaccinationAdapter adapter;
     RecyclerView  recyclerView;
     Button vaccinationSlotBooting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Vaccination Availability ");

        edPincode = findViewById(R.id.edPinCode);
        searchButton = findViewById(R.id.searchButton);
        vaccinationModelArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.vaccinationRecyclerView);
        vaccinationSlotBooting = findViewById(R.id.vaccinationSlotBooting);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edPincode.getText().toString().length() != 6){
                    Toast.makeText(VaccinationActivity.this, "Invalid Pincode ", Toast.LENGTH_SHORT).show();
                    vaccinationSlotBooting.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    vaccinationModelArrayList.clear();
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int date = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(VaccinationActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month++;
                            String monthString = month < 10 ? ("0" + month) : ("" + month);
                            String dateString = dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth);

                            String date = dateString + "-" + monthString + "-" + year;
                            String pincode = edPincode.getText().toString();
                            getAppointmentDetail(date, pincode);
                        }
                    }, year, month, date);
                    datePickerDialog.show();
                }

            }

            private void getAppointmentDetail(String date, String pincode) {

                RequestQueue queue = Volley.newRequestQueue(VaccinationActivity.this);
                Log.i("pic date",pincode+" "+date);
                String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+pincode+"&date="+date;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray centres = response.getJSONArray("centers");
                            if(centres.length()==0){
                                Toast.makeText(VaccinationActivity.this, "Sorry No Vaccination Centre Available", Toast.LENGTH_SHORT).show();
                            }else {
                                for (int i = 0; i < centres.length(); i++) {
                                    JSONObject centreObject = centres.getJSONObject(i);

                                    String centreName = centreObject.getString("name");
                                    String from = centreObject.getString("from");
                                    from = timeConverter(from);

                                    String to = centreObject.getString("to");
                                    to = timeConverter(to);

                                    String fee_type = centreObject.getString("fee_type");
                                    String address = centreObject.getString("address");

                                    JSONObject sectionObject = centreObject.getJSONArray("sessions").getJSONObject(0);

                                    String min_age_limit = sectionObject.getString("min_age_limit");
                                    String available_capacity = sectionObject.getString("available_capacity");
                                    String vaccineName = sectionObject.getString("vaccine");

                                    VaccinationModel vaccinationModel = new VaccinationModel(centreName,address,from,to,fee_type,min_age_limit,vaccineName,available_capacity);
                                    vaccinationModelArrayList.add(vaccinationModel);
                                }
                                vaccinationSlotBooting.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter = new VaccinationAdapter(vaccinationModelArrayList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(VaccinationActivity.this));
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            vaccinationSlotBooting.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VaccinationActivity.this, "Sorry! Fail to get response", Toast.LENGTH_SHORT).show();
                        vaccinationSlotBooting.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
                queue.add(jsonObjectRequest);

            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    public String timeConverter(String time){
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("hh:mm aa").format(dateObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void slotBookingClicked(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Slot Booking ?")
                .setMessage("Do you want to book a slot for Vaccination ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://selfregistration.cowin.gov.in/")));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}