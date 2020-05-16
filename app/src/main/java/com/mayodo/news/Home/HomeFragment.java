package com.mayodo.news.Home;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentTransaction;
//import androidx.core.view.ViewPager;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonElement;
import com.mayodo.news.AlldataFragment;
import com.mayodo.news.Evenements.AdapterHomeListCat;
import com.mayodo.news.Evenements.AdapterLatestnews;
import com.mayodo.news.Evenements.EvenmentDataBean;
import com.mayodo.news.NewsDetailActivity;
import com.mayodo.news.R;
import com.mayodo.news.allFeaturesNews.AllFeaturesActivity;
import com.mayodo.news.retrofit.RestClient;
//import com.mayodo.news.utils.AdMob;
import com.mayodo.news.utils.SharedObjects;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SlidingImage_Adapter.OnNewsSelectedListner, AdapterHomeListCat.OnNewsidSelectedListner, AdapterLatestnews.OnNewsidSelectedListner1, MyAdapter.OnCategorySelectedListner {

    TextView setitem, txtalldata, txtFeaturAll, txt_all_latest_news;
    private PopupWindow mPopupWindow;
    private RecyclerView rv;
    private RecyclerView rv_latest_news;
    //   private ArrayList arraylist;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.bg_img, R.drawable.bg_img, R.drawable.bg_img, R.drawable.bg_img};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private RecyclerView rvevenment;
    private MyAdapter myAdapter;
    private ArrayList<CategoriesBean> categorylist = new ArrayList<>();
    // private AdapterEventment adapterEventment;
    private AdapterHomeListCat adapterEventment;
    private AdapterLatestnews adapterLatestnews;
    private ArrayList<EvenmentDataBean> eventmentlist = new ArrayList<>();
    private ArrayList<EvenmentDataBean> latestnewslist = new ArrayList<>();
    ArrayList<EvenmentDataBean> featurNewsList = new ArrayList<>();
    private ArrayList<NavigationDrawerModel> navlist = new ArrayList<>();
    ProgressDialog progressDialog;
    String catid;
    String newsId;
    TextView tvdatanotfound;
    TextView tvToolbarTitle;
    ImageView hideimg;
    SharedObjects sharedObjects;
    Context mContext;
    private static boolean activityStarted;
//    private AdView mAdView;

    public HomeFragment() {

        // Required empty public constructor
    }

//    AdMob admob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedObjects = new SharedObjects(getActivity());

        mContext = getActivity();
        tvdatanotfound = view.findViewById(R.id.tvdatanotfound);
        rv = (RecyclerView) view.findViewById(R.id.rv1);
        rv_latest_news = (RecyclerView) view.findViewById(R.id.rv_latest_news);
        setitem = view.findViewById(R.id.settextitem);
        rvevenment = (RecyclerView) view.findViewById(R.id.rvevnments);
        txtalldata = view.findViewById(R.id.txtalldata);
        txtFeaturAll = view.findViewById(R.id.allFeature);
        txt_all_latest_news = view.findViewById(R.id.txt_all_latest_news);
        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        mPager.setPadding(10, 0, 90, 0);
        mPager.setPageMargin(5);
//        mAdView = view.findViewById(R.id.adView);

       //getAdsVisibility();
        setColorTheme();

        return view;
    }

 /*   @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (admob != null) {
            admob.stopRepeatingTask();
        }
    }
*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Initialization();
    }
//    private void getAdsVisibility() {
//        Call<JsonElement> call = RestClient.get().getAdsVisibility();
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Log.e("AdsVisibilityList", response.body().toString());
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject obj = null;
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        int visibility = jsonObject.getInt("visibility_setting");
//
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
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                admob = new AdMob(getActivity(), mAdView);
//                admob.requestAdMob();
//
//            }
//        });
//    }
    private void setColorTheme() {
        txtFeaturAll.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        txtalldata.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        txt_all_latest_news.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
    }

    private void Initialization() {
        ProgressDialogSetup();
        sharedObjects = new SharedObjects(getActivity());
        myAdapter = new MyAdapter(getActivity(), categorylist);
        rv.setAdapter(myAdapter);
        myAdapter.setOnCategorySelectedListner(this);

        Category();
        featuresNews();
        Latestnews();
        txtalldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("cId", catid);
                AlldataFragment alldataFragment = new AlldataFragment();
                alldataFragment.setArguments(bundle);
                replaceFragment(alldataFragment, R.id.fl_container, true);


            }
        });
        txt_all_latest_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListCategoriesActivity.class);
                startActivity(i);
            }
        });
        txtFeaturAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AllFeaturesActivity.class);
                startActivity(i);
            }
        });
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        NUM_PAGES = IMAGES.length;

    }


    public void replaceFragment(Fragment fragment, int containerId, boolean isAddedToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        if (isAddedToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }


    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
    }


    private void featuresNews() {
        //progressDialog.show();

        Call<JsonElement> call1 = RestClient.post().getFeatureNews();
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //  progressDialog.dismiss();
                featurNewsList.clear();
                Log.e("FeatureNews", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject json2 = jsonArr.getJSONObject(i);

                                String id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                String title = objtitle.getString("rendered");
                                String category = null;
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

                                featurNewsList.add(new EvenmentDataBean(id, title, formattedDate, img, category));
                            }
                            Log.e("featurNewsList size : ", featurNewsList.size() + "");
                        }
                        bindPagerAdapter();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Latestnews() {

        Call<JsonElement> call1 = RestClient.post().getFeatureNews();
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                // progressDialog.dismiss();
                latestnewslist.clear();
                Log.e("FeatureNews", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {

                            int length = jsonArr.length();
                            if (length < 15)
                                length = jsonArr.length();
                            else
                                length = 15;

                            for (int i = 0; i < length; i++) {

                                String id;
                                String title;
                                String formattedDate;
                                String img = "";
                                String category = "";

                                JSONObject json2 = jsonArr.getJSONObject(i);
                                id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                title = objtitle.getString("rendered");
                                String date = json2.getString("date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                DateFormat targetFormat = new SimpleDateFormat("MMMM dd yyyy");
                                formattedDate = null;
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

                                if (ObjEmbeded.has("wp:featuredmedia")) {
                                    JSONArray featureMediaArr = ObjEmbeded.getJSONArray("wp:featuredmedia");
                                    if (featureMediaArr.length() > 0) {
                                        for (int j = 0; j < featureMediaArr.length(); j++) {
                                            JSONObject objectImg = featureMediaArr.getJSONObject(j);
                                            img = objectImg.getString("source_url");
                                        }
                                    }
                                }

                                latestnewslist.add(new EvenmentDataBean(id, title, formattedDate, img, category));

                            }
                            Log.e("featurNewsList size : ", latestnewslist.size() + "");
                        }
                        bindLatestAdapternews();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindPagerAdapter() {

        if (featurNewsList != null) {
            mPager.setAdapter(new SlidingImage_Adapter(mContext, ImagesArray, featurNewsList, this));

        } else {
            mPager.setVisibility(View.GONE);
        }

    }

    private void Category() {
        // progressDialog.show();
        Call<JsonElement> call1 = RestClient.post().category();
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //progressDialog.dismiss();
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
                //  progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCategoryAdapternews() {

        if (eventmentlist.size() > 0) {
            adapterEventment = new AdapterHomeListCat(mContext, eventmentlist);
            adapterEventment.setOnNewsidSelectedListner(this);
            rvevenment.setAdapter(adapterEventment);
        }

    }

    private void bindLatestAdapternews() {

        if (latestnewslist.size() > 0) {
            adapterLatestnews = new AdapterLatestnews(mContext, latestnewslist);
            adapterLatestnews.setOnNewsidSelectedListner1(this);
            rv_latest_news.setAdapter(adapterLatestnews);
            //rv_latest_news.setAdapter(adapterEventment);
        }

    }

    private void bindCategoryAdapter() {

        if (categorylist.size() > 0) {
            catid = categorylist.get(0).getId();
            categorylist.get(0).setSelected(true);
        } else {
        }
        myAdapter.notifyDataSetChanged();
        setitem.setText(Html.fromHtml(categorylist.get(0).getName()));
        getNewsId();
    }


    private void getNewsId() {
        Call<JsonElement> call1 = RestClient.post().getNewsByCatID(catid);
        call1.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //   progressDialog.dismiss();
                Log.e("News", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {
                            eventmentlist.clear();
                            int length = jsonArr.length();
                            if (length < 5) {
                                length = jsonArr.length();
                            } else {
                                length = 5;
                            }
                            for (int i = 0; i < length; i++) {


                                String id;
                                String title;
                                String formattedDate = null;
                                String img = "";
                                String category = "";


                                JSONObject json2 = jsonArr.getJSONObject(i);
                                id = json2.getString("id");
                                JSONObject objtitle = json2.getJSONObject("title");
                                title = objtitle.getString("rendered");
                                String dateString = json2.getString("date").trim();
                                System.out.println(dateString);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                DateFormat targetFormat = new SimpleDateFormat("MMMM dd yyyy");

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

                                eventmentlist.add(new EvenmentDataBean(id, title, formattedDate,/*unlike,*/img, category));
                            }
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
                //  progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void setOnCategorySelatedListner(int position, CategoriesBean dataBean) {
        if (categorylist.size() < 0) {

            rvevenment.setVisibility(View.GONE);
            tvdatanotfound.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < categorylist.size(); i++) {
            categorylist.get(i).setSelected(false);
        }
        if (categorylist.size() > 0) {
            myAdapter.notifyDataSetChanged();
            catid = dataBean.getId();
            setitem.setText(dataBean.getName());
            getNewsId();
            dataBean.setSelected(true);
        } else {

            rvevenment.setVisibility(View.GONE);
            tvdatanotfound.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setOnNewsSelectedListner(int position, EvenmentDataBean evenmentDataBean) {
        newsId = featurNewsList.get(position).getId();
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        intent.putExtra("CATEGORY_IDS", catid);
        startActivity(intent);

    }

    @Override
    public void setOnNewsSelatedListner(int position, EvenmentDataBean evenmentDataBean) {
        newsId = featurNewsList.get(position).getId();

        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        intent.putExtra("CATEGORY_IDS", catid);
        startActivity(intent);
    }

    @Override
    public void setOnNewsidSelatedListner(int position, EvenmentDataBean evenmentDataBean) {
        newsId = eventmentlist.get(position).getId();

        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        intent.putExtra("CATEGORY_IDS", catid);
        startActivity(intent);


    }

    @Override
    public void setOnNewsidSelatedListner1(int position, EvenmentDataBean evenmentDataBean) {
        newsId = latestnewslist.get(position).getId();

        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("NewsID", newsId);
        intent.putExtra("CATEGORY_IDS", catid);
        startActivity(intent);

    }
}
