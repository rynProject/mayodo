package com.mayodo.news.Search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayodo.news.Evenements.AdapterEventment;
import com.mayodo.news.Evenements.EvenmentDataBean;
import com.mayodo.news.Home.HomePageActivity;
import com.mayodo.news.NewsDetailActivity;

import com.mayodo.news.R;
import com.mayodo.news.retrofit.RestClient;
import com.mayodo.news.utils.AppConstants;
import com.google.gson.JsonElement;
import com.mayodo.news.utils.SharedObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterEventment.OnNewsidSelectedListner {
    ProgressDialog progressDialog;
    NewsBySearchAdapter newsByIDAdapter;
    ArrayList<EvenmentDataBean> newslist;
    RecyclerView rvNews;
    LinearLayout llNoDataFound;
    Toolbar toolbar;
    TextView logo;
    ImageView ivBackBtn, ivOptionBtn;
    String newsId;
    SharedObjects sharedObjects;
    String searchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sharedObjects = new SharedObjects(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }
        initView();
        setColorTheme();
    }
    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        ivBackBtn = findViewById(R.id.ivBackBtn);
        ivBackBtn.setOnClickListener(this);
        logo = findViewById(R.id.tvToolbarTitle);
        logo.setText("Search result");
        setSupportActionBar(toolbar);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, HomePageActivity.class);
                startActivity(i);
            }
        });
        rvNews = (RecyclerView) findViewById(R.id.rvNews);
        llNoDataFound = findViewById(R.id.llNoDataFound);
        ProgressDialogSetup();
        searchWord = getIntent().getStringExtra(AppConstants.KEYWORD);
        newslist = new ArrayList<>();
        newsByIDAdapter = new NewsBySearchAdapter(this, newslist);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(newsByIDAdapter);
        getNewsByCatID();
    }
    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }
    private void setColorTheme() {
        toolbar.setBackgroundColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivBackBtn.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        logo.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
    }
    private void getNewsByCatID() {
        progressDialog.show();
        Call<JsonElement> call1 = RestClient.post().search(searchWord);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                Log.e("Search News", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {
                            for (int i = 0; i < jsonArr.length(); i++) {
                                String img = "";
                                String category = "";

                                JSONObject json2 = jsonArr.getJSONObject(i);
                                String id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                String title = objtitle.getString("rendered");
                                String dateString = json2.getString("date");
                                System.out.println(dateString);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                DateFormat targetFormat = new SimpleDateFormat("MMMM dd yyyy");
                                String formattedDate = null;
                                Date convertedDate = new Date();
                                try {
                                    convertedDate = dateFormat.parse(dateString);
                                    System.out.println(dateString);
                                    formattedDate = targetFormat.format(convertedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.e("date", formattedDate);
                                if (json2.has("_embedded")) {
                                    JSONObject ObjEmbeded = json2.getJSONObject("_embedded");
                                    JSONArray wpTermArr = ObjEmbeded.getJSONArray("wp:term");
                                    if (wpTermArr.length() > 0) {
                                        for (int j = 0; j < wpTermArr.length(); j++) {
                                            JSONArray wpTermSubArr = wpTermArr.getJSONArray(j);

                                            if (wpTermSubArr.length() > 0) {
                                                for (int k = 0; k < wpTermSubArr.length(); k++) {
                                                    JSONObject wpTerm = wpTermSubArr.getJSONObject(k);
                                                    category = String.valueOf(Html.fromHtml(wpTerm.getString("name")));
                                                }
                                            }
                                        }
                                    }

                                    if (ObjEmbeded.has("wp:featuredmedia")) {
                                        JSONArray featureMediaArr = ObjEmbeded.getJSONArray("wp:featuredmedia");
                                        if (featureMediaArr.length() > 0) {
                                            for (int j = 0; j < featureMediaArr.length(); j++) {
                                                JSONObject objectImg = featureMediaArr.getJSONObject(j);
                                                img = objectImg.getString("source_url");
                                            }
                                        }
                                    }
                                }
                                newslist.add(new EvenmentDataBean(id, title, formattedDate,/*unlike,*/img, category));
                            }
                            newsByIDAdapter.notifyDataSetChanged();
                        }
                        bindCategoryAdapternews();
                        Log.e("Cat size : ", newslist.size() + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SearchActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCategoryAdapternews() {

        if (newslist.size() > 0) {
            llNoDataFound.setVisibility(View.GONE);
            rvNews.setVisibility(View.VISIBLE);
            newsByIDAdapter = new NewsBySearchAdapter(SearchActivity.this, newslist);
            newsByIDAdapter.setOnNewsidSelectedListner(this);
            rvNews.setAdapter(newsByIDAdapter);
        } else {
            llNoDataFound.setVisibility(View.VISIBLE);
            rvNews.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackBtn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void setOnNewsidSelatedListner(int position, EvenmentDataBean evenmentDataBean) {
        newsId = newslist.get(position).getId();

        Intent intent = new Intent(SearchActivity.this, NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        startActivity(intent);
    }

}
