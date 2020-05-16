package com.mayodo.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonElement;
import com.mayodo.news.Home.HomePageActivity;
import com.mayodo.news.retrofit.RestClient;
import com.mayodo.news.utils.SharedObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 5000;
    SharedObjects sharedObjects ;
    ImageView rvLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedObjects = new SharedObjects(SplashActivity.this);

        getAppLogoApi();
        getAppColorApi();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rvLogo=(ImageView)findViewById(R.id.rvLogo);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, HomePageActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);



    }
    private void getAppColorApi() {
        Call<JsonElement> call = RestClient.get().getAppColor();
        call.enqueue(new Callback<JsonElement>() {

            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = null;
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int flag = jsonObject.getInt("flag");

                        if (flag == 1) {
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    obj = jsonArray.getJSONObject(i);

                                    String header_color = obj.getString("header_color");
                                    String footer_color = obj.getString("footer_color");
                                    String primary_color = obj.getString("primary_color");
                                    sharedObjects.setColor(header_color, footer_color, primary_color);

                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }


    private void getAppLogoApi() {
        Call<JsonElement> call = RestClient.get().getAppLogo();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = null;
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int flag = jsonObject.getInt("flag");

                        if (flag == 1) {
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    obj = jsonArray.getJSONObject(i);

                                    String logo = obj.getString("logo");
                                    sharedObjects.setLogo(logo);
                                    sharedObjects.getLogo();
                                    Glide.with(getApplicationContext()).load(sharedObjects.getLogo())
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .placeholder(R.drawable.splash)
                                            .error(R.drawable.splash)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(rvLogo);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }


}
