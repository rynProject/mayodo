package com.mayodo.news.allFeaturesNews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.mayodo.news.Evenements.EvenmentDataBean;
import com.mayodo.news.NewsDetailActivity;

import com.mayodo.news.R;
import com.mayodo.news.retrofit.RestClient;
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

public class AllFeaturesActivity extends AppCompatActivity implements View.OnClickListener, AllFeaturenewsAdapter.NewsItemClickListener, AllFeaturenewsAdapter.OnLoadMoreListener {
    ProgressDialog progressDialog;
    AllFeaturenewsAdapter newsByIDAdapter;
    ArrayList<EvenmentDataBean> newslist = new ArrayList<EvenmentDataBean>();
    RecyclerView rvNews;
    LinearLayout llNoDataFound;
    Toolbar toolbar;
    AppBarLayout AppBar;
    String newsId;
    String catId;
    String parentId;
    ImageView ivBackBtn;
    public static TextView tvToolbarTitle;
    private int per_page = 1;
    SharedObjects sharedObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_features);
        sharedObjects = new SharedObjects(AllFeaturesActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }

        initView();
        setColorTheme();
        ProgressDialogSetup();
        getNewsByCatID(per_page);

    }

    private void initView() {
//        toolbar = (android.widget.Toolbar) findViewById(R.id.toolbar);
        AppBar = findViewById(R.id.AppBar);
        ivBackBtn = findViewById(R.id.ivBackBtn);
        ivBackBtn.setOnClickListener(this);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
//        setSupportActionBar(toolbar);
        rvNews = (RecyclerView) findViewById(R.id.rvNews);
        llNoDataFound = findViewById(R.id.llNoDataFound);
    }
    private void setColorTheme() {
        toolbar.setBackgroundColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivBackBtn.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        tvToolbarTitle.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
    }
    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(AllFeaturesActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    private void getNewsByCatID(final int per_page) {
        if (per_page == 1) {
            progressDialog.show();
        }
        Call<JsonElement> call = RestClient.post().getAllLatestNews(catId, per_page);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (per_page == 1) {
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject json2 = jsonArr.getJSONObject(i);
                                String id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                String title = objtitle.getString("rendered");
                                String dateString = json2.getString("date");
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
                                String category="";
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
                                String img = "";
                                if (ObjEmbeded.has("wp:featuredmedia")) {
                                    JSONArray featureMediaArr = ObjEmbeded.getJSONArray("wp:featuredmedia");
                                    if (featureMediaArr.length() > 0) {
                                        for (int j = 0; j < featureMediaArr.length(); j++) {
                                            JSONObject objectImg = featureMediaArr.getJSONObject(j);
                                            img = objectImg.getString("source_url");
                                        }
                                    }
                                }

                                newslist.add(new EvenmentDataBean(id, title, formattedDate, /*unlike,*/ img, category));
                                bindCategoryAdapter();
                            }
                        } else {
                            newslist.remove(newslist.size() - 1);
                            newsByIDAdapter.notifyItemRemoved(newslist.size());
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject json2 = jsonArr.getJSONObject(i);
                                String id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                String title = objtitle.getString("rendered");
                                String dateString = json2.getString("date");
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
                                String category="";
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
                                String img = "";
                                if (ObjEmbeded.has("wp:featuredmedia")) {
                                    JSONArray featureMediaArr = ObjEmbeded.getJSONArray("wp:featuredmedia");
                                    if (featureMediaArr.length() > 0) {
                                        for (int j = 0; j < featureMediaArr.length(); j++) {
                                            JSONObject objectImg = featureMediaArr.getJSONObject(j);
                                            img = objectImg.getString("source_url");
                                        }
                                    }
                                }
                                newslist.add(new EvenmentDataBean(id, title, formattedDate,/* unlike,*/ img, category));
                                newsByIDAdapter.notifyDataSetChanged();
                                newsByIDAdapter.setLoaded();
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        newslist.remove(newslist.size() - 1);
                        newsByIDAdapter.notifyItemRemoved(newslist.size());
                        Toast.makeText(AllFeaturesActivity.this, "No recored available", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("LoginRes", t.toString());
            }
        });
    }

    private void bindCategoryAdapter() {

        if (newslist.size() > 0) {
            llNoDataFound.setVisibility(View.GONE);
            rvNews.setVisibility(View.VISIBLE);
            newsByIDAdapter = new AllFeaturenewsAdapter(AllFeaturesActivity.this, newslist, rvNews);
            newsByIDAdapter.setOnLoadMoreListener(this);
            newsByIDAdapter.setOnNewsItemClickListener(this);
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
    public void OnNewItemClickListener(EvenmentDataBean bean, int position) {
        newsId = newslist.get(position).getId();
        Intent intent = new Intent(AllFeaturesActivity.this, NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        startActivity(intent);

    }

    @Override
    public void onLoadMore() {
        progressDialog.dismiss();
        Log.e("haint", "Load More");
        if (!newsByIDAdapter.isLoading) {
            newslist.add(null);
            newsByIDAdapter.notifyDataSetChanged();
            per_page++;
            getNewsByCatID(per_page);
        }
    }
}
