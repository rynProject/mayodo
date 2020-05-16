package com.mayodo.news.Home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import androidx.core.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.mayodo.news.Evenements.AdapterHomeListCat;
import com.mayodo.news.Evenements.EvenmentDataBean;
import com.mayodo.news.NewsDetailActivity;
import com.mayodo.news.R;
import com.mayodo.news.retrofit.RestClient;

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

/**
 * A simple {@link Fragment} subclass.
 */

public class SubCategoriesFragment extends Fragment implements ItemClickListener<SubCategoriesModel>, AdapterHomeListCat.OnNewsidSelectedListner {
    RecyclerView rvNavigation;
    private SubListAdepater mNavigationDrawerAdapter;
    View view;
    String id;
    String catname;
    String newsId, catid;
    ArrayList<SubCategoriesModel> categorylist = new ArrayList<>();
    String parentId;
    SubCategoriesModel subCategoriesModel;

    private ArrayList<EvenmentDataBean> eventmentlist = new ArrayList<>();
    private AdapterHomeListCat adapterEventment;
    private RecyclerView rvevenment;

    public SubCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_categories, container, false);
        rvevenment = view.findViewById(R.id.rvevnments);
        Bundle bundle = getArguments();
        parentId = bundle.getString("CATEGORY_ID");
        catname = bundle.getString("CATEGORY_NM");
        HomePageActivity.tvToolbarTitle.setText(catname);
        rvNavigation = (RecyclerView) view.findViewById(R.id.rv_navigation);
        Category();
        getNewsId();
        return view;

    }

    private void Category() {
        Call<JsonElement> call1 = RestClient.post().category();
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("Cat", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {
                            categorylist.clear();

                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject json2 = jsonArr.getJSONObject(i);

                                if (json2.getString("parent").equals(parentId)) {
                                    subCategoriesModel = new SubCategoriesModel();
                                    catid = json2.getString("id");
                                    subCategoriesModel.setCatID(json2.getString("id"));
                                    subCategoriesModel.setCatName(String.valueOf(Html.fromHtml(json2.getString("name"))));
                                    subCategoriesModel.setParent(json2.getString("parent"));
                                    catname = String.valueOf(Html.fromHtml(json2.getString("name")));
                                    String firstChar = String.valueOf(catname.charAt(0));
                                    subCategoriesModel.setCatFirstChar(firstChar);
                                    categorylist.add(subCategoriesModel);
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
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCategoryAdapter() {
        if (categorylist.size() > 0) {
            rvNavigation.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvNavigation.setAdapter(mNavigationDrawerAdapter);
            mNavigationDrawerAdapter = new SubListAdepater(getActivity(), categorylist, this);

            rvNavigation.setAdapter(mNavigationDrawerAdapter);
            mNavigationDrawerAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "No item found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getNewsId() {
        final String[] id = new String[6];
        final String[] title = new String[6];
        final String[] formattedDate = new String[6];
        final String[] img = new String[6];
        final String[] category = new String[6];
        Call<JsonElement> call1 = RestClient.post().getNewsByCatID(parentId);
        call1.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("News", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {
                            eventmentlist.clear();
                            int length = jsonArr.length();
                            if (length < 5)
                                length = jsonArr.length();
                            else
                                length = 5;

                            for (int i = 0; i < length; i++) {
                                JSONObject json2 = jsonArr.getJSONObject(i);
                                id[i] = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                title[i] = objtitle.getString("rendered");
                                String dateString = json2.getString("date").trim();
                                System.out.println(dateString);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                DateFormat targetFormat = new SimpleDateFormat("MMMM dd yyyy");
                                formattedDate[i] = null;
                                Date convertedDate = new Date();
                                try {
                                    convertedDate = dateFormat.parse(dateString);
                                    System.out.println(dateString);
                                    formattedDate[i] = targetFormat.format(convertedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.e("date", formattedDate[i]);

                                img[i] = json2.getString("featured_image_link");
                                JSONArray cattitle = json2.getJSONArray("category_arr");
                                for (int j = 0; j < cattitle.length(); j++) {
                                    JSONObject cat = cattitle.getJSONObject(j);
                                    category[i] = String.valueOf(Html.fromHtml(cat.getString("name")));
                                }
                                eventmentlist.add(new EvenmentDataBean(id[i], title[i], formattedDate[i],/*unlike,*/img[i], category[i]));
                            }
                        } else {
                            Toast.makeText(getActivity(), "No recored available", Toast.LENGTH_SHORT).show();
                        }
                        bindCategoryAdapternews();
                        Log.e("Cat size : ", eventmentlist.size() + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindCategoryAdapternews() {

        if (eventmentlist.size() > 0) {
            adapterEventment = new AdapterHomeListCat(getActivity(), eventmentlist);
            adapterEventment.setOnNewsidSelectedListner(this);
            rvevenment.setAdapter(adapterEventment);
        }

    }

    @Override
    public void onItemClicked(int position, SubCategoriesModel model) {
        Intent intent = new Intent(getActivity(), ListCategoriesActivity.class);
        intent.putExtra("CATEGORY_ID", model.getCatID());
        intent.putExtra("CATEGORY_NM", model.getCatName());
        startActivity(intent);
    }


    @Override
    public void setOnNewsidSelatedListner(int position, EvenmentDataBean evenmentDataBean) {
        newsId = eventmentlist.get(position).getId();
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        intent.putExtra("CATEGORY_IDS", parentId);
        startActivity(intent);
    }
}
