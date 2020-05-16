package com.mayodo.news;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import androidx.core.app.Fragment;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.MobileAds;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mayodo.news.Evenements.AdapterEventment;
import com.mayodo.news.Evenements.EvenmentDataBean;
import com.mayodo.news.Home.CategoriesBean;
import com.mayodo.news.Home.MyAdapter;

import com.mayodo.news.retrofit.RestClient;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.gson.JsonElement;
//import com.mayodo.news.utils.AdMob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlldataFragment extends Fragment implements MyAdapter.OnCategorySelectedListner, AdapterEventment.OnNewsidSelectedListner/*, AdapterEventment.OnLoadMoreListener*/ {

    TextView selectedText;
    RecyclerView rvcate, rvalldata;
    private MyAdapter myAdapter;
    private ArrayList<CategoriesBean> categorylist = new ArrayList<>();
    private AdapterEventment adapterEventment;
    private ArrayList<EvenmentDataBean> eventmentlist = new ArrayList<>();
    ProgressDialog progressDialog;
    String catid;
    String catidfromdrawer = "";
    int positionFromDrawer = 0;
    int pager;
    String newsId;
    String selectedId;
//    private AdView mAdView;
    private int per_page = 1;
    LinearLayout llNoDataFound;
//    AdMob admob;

    public AlldataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alldata, container, false);
        Initialization(v);
        return v;
    }

    private void Initialization(View v) {
        ProgressDialogSetup();
        Bundle bundle = getArguments();
        selectedId = bundle.getString("cId");
        selectedText = v.findViewById(R.id.settextitemall);
        rvalldata = (RecyclerView) v.findViewById(R.id.rvalldata);
        rvcate = (RecyclerView) v.findViewById(R.id.rvalcat);
        myAdapter = new MyAdapter(getActivity(), categorylist);
        rvcate.setAdapter(myAdapter);
        myAdapter.setOnCategorySelectedListner(this);
//        mAdView = v.findViewById(R.id.adView1);
        llNoDataFound = v.findViewById(R.id.llNoDataFound);

         getAdsVisibility();

        Category();
    }
/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (admob != null) {
            admob.stopRepeatingTask();
        }
    }*/

    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            catidfromdrawer = bundle.getString("id");
            positionFromDrawer = bundle.getInt("position");
        }
    }

    private void getAdsVisibility() {
        Call<JsonElement> call = RestClient.get().getAdsVisibility();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("AdsVisibilityList", response.body().toString());
                if (response.isSuccessful()) {
                    try {

                        JSONObject obj = null;
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int visibility = jsonObject.getInt("visibility_setting");

//                        if (visibility == 1) {
//                            admob = new AdMob(getActivity(), mAdView);
//                            admob.requestAdMob();
//                           /* if (jsonArray.length() > 0) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    obj = jsonArray.getJSONObject(i);
//                                    String id = obj.getString("id");
//                                    //String name = obj.getString("category");
//                                    String image = obj.getString("image");
//
//                                }
//                            }*/
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getActivity(), "Ads gagal ditampilkan", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Category() {
        progressDialog.show();
        Call<JsonElement> call1 = RestClient.post().category();
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                Log.e("Cat", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {
                            categorylist.clear();
                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject json2 = jsonArr.getJSONObject(i);
                                if (json2.getString("parent").equals("0")) {
                                    String name = String.valueOf(Html.fromHtml(json2.getString("name")));
                                    String id = json2.getString("id");
                                    String pid = json2.getString("parent");
                                    categorylist.add(new CategoriesBean(id, name, pid, false));
                                }
                            }
                        }
                        bindCategoryAdapter();
                        Log.e("Cat size : ", categorylist.size() + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getNewsId(final int per_page) {
        progressDialog.show();
        Call<JsonElement> call1 = RestClient.post().getNewsByCatID(catid);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
//                Log.e("News", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        /*  if (per_page == 1) {*/
                        if (jsonArr.length() > 0) {
                            eventmentlist.clear();
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject json2 = jsonArr.getJSONObject(i);
                                String id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                String title = objtitle.getString("rendered");
                                String category = "";
                                String date = json2.getString("date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                DateFormat targetFormat = new SimpleDateFormat("MMMM dd yyyy");
                                String formattedDate = null;
                                Date convertedDate = new Date();
                                try {
                                    convertedDate = dateFormat.parse(date);
                                    System.out.println(date);
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

                                eventmentlist.add(new EvenmentDataBean(id, title, formattedDate,/*unlike,*/ img, category));
                                bindCategoryAdapternews();
                            }

                        } else {
                            Toast.makeText(getActivity(), "No recored available", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("Cat size : ", eventmentlist.size() + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindCategoryAdapter() {
        if (categorylist.size() > 0) {

            catid = categorylist.get(0).getId();
            for (int i = 0; i < categorylist.size(); i++) {
                if (categorylist.get(i).getId().equals(selectedId)) {
                    categorylist.get(i).setSelected(true);
                    selectedText.setText(categorylist.get(i).getName());
                    catid = categorylist.get(i).getId();
                } else {
                    categorylist.get(i).setSelected(false);
                }
            }

        } else {

            catid = catidfromdrawer;
            categorylist.get(positionFromDrawer).setSelected(true);
            selectedText.setText(Html.fromHtml(categorylist.get(positionFromDrawer).getName()));
        }
        myAdapter.notifyDataSetChanged();


        getNewsId(per_page);

    }

    private void bindCategoryAdapternews() {
        if (eventmentlist.size() > 0) {
            adapterEventment = new AdapterEventment(getActivity(), eventmentlist, rvalldata);
            adapterEventment.setOnNewsidSelectedListner(this);
            rvalldata.setAdapter(adapterEventment);
        }
    }

    @Override
    public void setOnCategorySelatedListner(int position, CategoriesBean dataBean) {

        for (int i = 0; i < categorylist.size(); i++) {
            categorylist.get(i).setSelected(false);
        }
        myAdapter.notifyDataSetChanged();
        catid = dataBean.getId();
        selectedText.setText(dataBean.getName());
        getNewsId(per_page);
        dataBean.setSelected(true);
    }

    @Override
    public void setOnNewsidSelatedListner(int position, EvenmentDataBean evenmentDataBean) {
        newsId = eventmentlist.get(position).getId();

        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        startActivity(intent);
    }
}
