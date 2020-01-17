package com.reportedsocks.testproject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        requestQueue = Volley.newRequestQueue(this);

        new getUserCountryAsyncTask().execute();

    }

    private void getUserCountry(){
        String url ="http://ip-api.com/json/?fields=status,message,country";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            String status = response.getString("status");
                            if(status.equals("success")){
                                String userCountry = response.getString("country");
                                launchNewActivity( userCountry );
                            } else {
                                String msg = response.getString("message");
                                Toast.makeText(SplashScreenActivity.this, msg, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            //Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //Toast.makeText(SplashScreenActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void launchNewActivity(String userCountry) {

        //userCountry = "Russia";
        switch(userCountry) {
            case "Germany":
                Toast.makeText(SplashScreenActivity.this, "You are in " + userCountry, Toast.LENGTH_LONG).show();
                Uri webpage = Uri.parse("https://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case "Russia":
                Toast.makeText(SplashScreenActivity.this, "You are in " + userCountry, Toast.LENGTH_LONG).show();
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                break;
            default:
                Toast.makeText(SplashScreenActivity.this,
                        "You are in " + userCountry + ". No action specified for this scenario", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private class getUserCountryAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getUserCountry();
            return null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
