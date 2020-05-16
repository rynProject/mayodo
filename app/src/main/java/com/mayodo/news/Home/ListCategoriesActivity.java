package com.mayodo.news.Home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mayodo.news.Evenements.AdapterEventment;
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

public class ListCategoriesActivity extends AppCompatActivity implements AdapterEventment.OnNewsidSelectedListner, AdapterEventment.OnLoadMoreListener {
    ArrayList<EvenmentDataBean> eventmentlist = new ArrayList<>();
    private AdapterEventment adapterEventment;
    RecyclerView rvevenment;
    ProgressDialog progressDialog;
    String newsId;
    String id;
    String CatID;
    String CatName;
    ImageView ivBackBtn;
    public static TextView tvToolbarTitle;
    private int per_page = 1;
    LinearLayout llNoDataFound;
    Toolbar toolbar;
    SharedObjects sharedObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);
        sharedObjects = new SharedObjects(ListCategoriesActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }
        initView();

        setColorTheme();
    }

    private void initView() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ProgressDialogSetup();
        rvevenment = findViewById(R.id.rvevnments);
        ivBackBtn = findViewById(R.id.ivBackBtn);
        llNoDataFound = findViewById(R.id.llNoDataFound);
        ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        CatID = intent.getStringExtra("CATEGORY_ID");
        CatName = intent.getStringExtra("CATEGORY_NM");
        tvToolbarTitle.setText(CatName);
        getNewsId(CatID, per_page);
        if (CatID == null) {
            tvToolbarTitle.setText("Latest News");
        }
    }
    private void setColorTheme() {
        toolbar.setBackgroundColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivBackBtn.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        tvToolbarTitle.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
    }

    private void getNewsId(String catID, final int per_page) {
        if (per_page == 1) {

        }
        Call<JsonElement> call1 = RestClient.post().getAllLatestNews(CatID, per_page);
        call1.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (per_page == 1) {
                            if (jsonArr.length() > 0) {
                                eventmentlist.clear();
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    String category = null;
                                    JSONObject json2 = jsonArr.getJSONObject(i);
                                    String id = json2.getString("id");
                                    JSONObject objtitle = json2.getJSONObject("title");
                                    String title = objtitle.getString("rendered");
                                    String dateString = json2.getString("date").trim();
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

                                    eventmentlist.add(new EvenmentDataBean(id, title, formattedDate, img, category));
                                    bindCategoryAdapternews();
                                }
                            } else {
                                Toast.makeText(ListCategoriesActivity.this, "No recored available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            eventmentlist.remove(eventmentlist.size() - 1);
                            adapterEventment.notifyItemRemoved(eventmentlist.size());
                            for (int i = 0; i < jsonArr.length(); i++) {
                                String category = null;
                                JSONObject json2 = jsonArr.getJSONObject(i);
                                String id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                String title = objtitle.getString("rendered");
                                String dateString = json2.getString("date").trim();
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

                                eventmentlist.add(new EvenmentDataBean(id, title, formattedDate, img, category));
                                adapterEventment.notifyDataSetChanged();
                                adapterEventment.setLoaded();
                            }

                        }

                        Log.e("Cat size : ", eventmentlist.size() + "");
                    } else {
                        eventmentlist.remove(eventmentlist.size() - 1);
                        adapterEventment.notifyItemRemoved(eventmentlist.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(ListCategoriesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCategoryAdapternews() {

        if (eventmentlist.size() > 0) {
            llNoDataFound.setVisibility(View.GONE);
            rvevenment.setVisibility(View.VISIBLE);
            adapterEventment = new AdapterEventment(ListCategoriesActivity.this, eventmentlist, rvevenment);
            adapterEventment.setOnLoadMoreListener(this);
            adapterEventment.setOnNewsidSelectedListner(this);
            rvevenment.setAdapter(adapterEventment);
        } else {
            llNoDataFound.setVisibility(View.VISIBLE);
            rvevenment.setVisibility(View.GONE);
            Toast.makeText(ListCategoriesActivity.this, "No item found", Toast.LENGTH_SHORT).show();
        }


    }

    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(ListCategoriesActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    public void setOnNewsidSelatedListner(int position, EvenmentDataBean evenmentDataBean) {
        newsId = eventmentlist.get(position).getId();
        Intent intent = new Intent(ListCategoriesActivity.this, NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        intent.putExtra("CATEGORY_IDS", CatID);
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
       // progressDialog.dismiss();
        Log.e("haint", "Load More");
        if (!adapterEventment.isLoading) {
            eventmentlist.add(null);
            adapterEventment.notifyDataSetChanged();
            per_page++;
            getNewsId(CatID, per_page);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
