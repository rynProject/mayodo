package com.mayodo.news;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.support.design.widget.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.mayodo.news.Home.HomePageActivity;

import com.mayodo.news.comment.AddCommentActivity;
import com.mayodo.news.comment.CommentActivity;
import com.mayodo.news.retrofit.RestClient;
import com.mayodo.news.utils.AppConstants;
import com.mayodo.news.utils.SharedObjects;
import com.mayodo.news.utils.WebHelper;
import com.google.gson.JsonElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener, CoustomlistAdapter.OnRelatedNewsSelectedListner {
    String NewsID;
    private ImageView mIvBackBtn;
    private AppBarLayout mAppBar;
    private ImageView mImgMain;
    private TextView mTvCat;
    private TextView mTvTitle;
    private TextView mTvPubDate;
    private TextView mTxtcomment;
    private TextView mTxtshare;
    private WebView mWebView;
    String title;
    String like;
    String unlike;
    String comment;
    String description;
    ProgressDialog progressDialog;
    String newsId, parentId;
    String relatedListId[];
    private RecyclerView rvCustom;
    private ArrayList<CustomDataBean> customtlist = new ArrayList<>();
    CoustomlistAdapter coustomlistAdapter;
    private ImageView playbtn;
    private ImageView logo;
    LinearLayout llMain;
    private CircleImageView imgFacbook;
    private CircleImageView imgTwitter;
    private CircleImageView imgPin;
    private CircleImageView imgLinkded;
    private CircleImageView imgGoogle;
    TextView tvrelated;
    String video_url;
    String link;
    String name;
    String featured_image_link;
    String category;
    String titlenews;
    String commentcount;
    TextView btnadd, btnview;
    NestedScrollView nestedScrollView;
    private static final int SPLASH_TIME_OUT = 500;
    Handler mHandler;

//    android.support.v7.widget.Toolbar toolbar;
    ImageView ivBackBtn;
    ImageView ivDate;
    ImageView ivComment;
    ImageView ivShare;
    public static TextView tvToolbarTitle;
    SharedObjects sharedObjects;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        sharedObjects = new SharedObjects(NewsDetailActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }
       nestedScrollView=findViewById(R.id.scrollview);

        initView();
        rvCustom = (RecyclerView) findViewById(R.id.related);
        rvCustom.setLayoutManager(new LinearLayoutManager(this));
        ProgressDialogSetup();
        NewsID = getIntent().getStringExtra(AppConstants.NEWS_ID);
        Intent intent = getIntent();
        if (intent != null) {
            newsId = intent.getStringExtra("NewsID");
            parentId = intent.getStringExtra("CATEGORY_IDS");
        }

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsDetailActivity.this, AddCommentActivity.class);
                intent.putExtra("NewsID", newsId);
                intent.putExtra("TITLE", titlenews);
                startActivity(intent);
            }
        });
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsDetailActivity.this, CommentActivity.class);
                intent.putExtra("NewsID", newsId);
                intent.putExtra("TITLE", titlenews);
                startActivity(intent);
            }
        });

        setColorTheme();
    }
    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(NewsDetailActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    private void initView() {
        toolbar =  findViewById(R.id.toolbar);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        mIvBackBtn = (ImageView) findViewById(R.id.ivBackBtn);
        ivDate = (ImageView) findViewById(R.id.ivDate);
        ivComment = (ImageView) findViewById(R.id.ivComment);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        mIvBackBtn.setOnClickListener(this);
        mAppBar = (AppBarLayout) findViewById(R.id.AppBar);
        mImgMain = (ImageView) findViewById(R.id.imgMain);
        //logo = (ImageView) findViewById(R.id.ivLogo);
        mTvCat = (TextView) findViewById(R.id.tvCat);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvPubDate = (TextView) findViewById(R.id.tvPubDate);
        llMain = findViewById(R.id.llMain);
        mTxtcomment = (TextView) findViewById(R.id.txtcomment);
        mTxtcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, CommentActivity.class);
                intent.putExtra("NewsID", newsId);
                intent.putExtra("TITLE", titlenews);
                startActivity(intent);
            }
        });
        mTxtshare = (TextView) findViewById(R.id.txtshare);
        tvrelated = (TextView) findViewById(R.id.related1);
        btnadd = findViewById(R.id.btn_add);
        btnview = findViewById(R.id.btn_view);
        mWebView = (WebView) findViewById(R.id.webView);
//        logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(NewsDetailActivity.this, HomePageActivity.class);
//                startActivity(i);
//            }
//        });
        playbtn = findViewById(R.id.playbtn);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, YoutubeActivity.class);
                intent.putExtra("video_url", video_url);
                startActivity(intent);
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.getSettings().setDefaultFontSize(16);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        mTxtshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = link;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
        });
        imgFacbook = (CircleImageView) findViewById(R.id.imgFacbook);
        imgFacbook.setOnClickListener(this);
        imgTwitter = (CircleImageView) findViewById(R.id.imgTwitter);
        imgTwitter.setOnClickListener(this);
        imgPin = (CircleImageView) findViewById(R.id.imgPin);
        imgPin.setOnClickListener(this);
        imgLinkded = (CircleImageView) findViewById(R.id.imgLinkded);
        imgLinkded.setOnClickListener(this);
        imgGoogle = (CircleImageView) findViewById(R.id.imgGoogle);
        imgGoogle.setOnClickListener(this);
    }

    private void setColorTheme() {
        toolbar.setBackgroundColor(Color.parseColor(sharedObjects.getHeaderColor()));
        mIvBackBtn.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        tvToolbarTitle.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));

        mTvCat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        btnadd.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        btnview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        mTvCat.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        btnadd.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        btnview.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        mTvPubDate.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        mTxtcomment.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        mTxtshare.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivDate.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        ivComment.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        ivShare.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
    }
    @Override
    public void onClick(View v) {
        Intent browserIntent;
        switch (v.getId()) {

            case R.id.ivBackBtn:
                // TODO 18/03/16
                onBackPressed();
                break;

            case R.id.imgFacbook:
                String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + link;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                startActivity(browserIntent);
                break;
            case R.id.imgTwitter:
                String twittername = "https://twitter.com/intent/tweet?via=shawqealaghbre&text=";
                String twitterUrl = "&url=" + link;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twittername + twitterUrl));
                startActivity(browserIntent);
                break;
            case R.id.imgPin:
                String pinUrl = "https://pinterest.com/pin/create/button/?url=" + link;
                String media = "&media=" + mImgMain;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pinUrl + media));
                startActivity(browserIntent);
                break;
            case R.id.imgLinkded:
                String LinkedUrl = "http://www.linkedin.com/shareArticle?mini=true&url=" + link;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(LinkedUrl/*+LinkedSummery*/));
                startActivity(browserIntent);
                break;
            case R.id.imgGoogle:
                String GplusUrl = "https://plus.google.com/share?url=" + link;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GplusUrl));
                startActivity(browserIntent);
                break;
            default:
                break;
        }
    }

    private void getNewsById(String parentId, final String newsIdne) {
        progressDialog.show();
        getRelatedByNewsID(parentId);
        Call<JsonElement> call1 = RestClient.post().getNewsByNewsID(newsIdne);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    try {
                        if (response.isSuccessful()) {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.length() > 0) {
                                newsId = jsonObject.getString(("id"));
                                JSONObject title = jsonObject.getJSONObject("title");
                                titlenews = String.valueOf((Html.fromHtml(title.getString("rendered"))));
                                mTvTitle.setText(titlenews);
                                // tvtitle.setText(titlenews);
                                String date = jsonObject.getString("date");
                                link = jsonObject.getString("link");
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
                                mTvPubDate.setText(formattedDate);


                                JSONObject ObjEmbeded = jsonObject.getJSONObject("_embedded");
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
                                            featured_image_link = objectImg.getString("source_url");
                                        }
                                    }
                                }

                                if (ObjEmbeded.has("replies")) {
                                    JSONArray repliesArr = ObjEmbeded.getJSONArray("replies");
                                    if(repliesArr.length()>0) {
                                        JSONArray repliesSubArr = repliesArr.getJSONArray(0);
                                        if(repliesSubArr.length()>0){
                                            commentcount = String.valueOf(repliesSubArr.length());
                                        }else{
                                            commentcount = "0";
                                        }
                                    }
                                }
                                mTxtcomment.setText(commentcount);
                                JSONObject content = jsonObject.getJSONObject("content");
                                Document doc = (Document) Jsoup.parse(content.getString("rendered"));
                                String html = WebHelper.docToBetterHTML(doc, NewsDetailActivity.this);
                                String link = "" + newsId + "/" + title;
                                mWebView.loadDataWithBaseURL(link, html, "text/html", "utf-8", "");
                                mTvCat.setText(category);
                                Glide.with(NewsDetailActivity.this).load(featured_image_link)
                                        .thumbnail(0.5f)
                                        .placeholder(R.drawable.bg_img)
                                        .error(R.drawable.bg_img)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(mImgMain);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        String errRess = response.errorBody().source().buffer().toString();
                        Log.e("ErrRes403String", "- " + errRess);
                        JSONObject jsonObject = new JSONObject(response.errorBody().source().buffer().readUtf8());
                        if (jsonObject.length() > 0) {
                            String id = jsonObject.getString(("id"));
                            newsId = id;
                            JSONObject title = jsonObject.getJSONObject("title");
                            titlenews = String.valueOf((Html.fromHtml(title.getString("rendered"))));
                            mTvTitle.setText(titlenews);
                            String date = jsonObject.getString("date");
                            link = jsonObject.getString("link");
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
                            mTvPubDate.setText(formattedDate);
                            commentcount = jsonObject.getString("comment_count");
                            mTxtcomment.setText(commentcount);

                            video_url = jsonObject.getString("video_url");
                            JSONObject content = jsonObject.getJSONObject("content");

                            Document doc = (Document) Jsoup.parse(content.getString("rendered"));
                            String html = WebHelper.docToBetterHTML(doc, NewsDetailActivity.this);
                            String link = "" + id + "/" + title;
                            mWebView.loadDataWithBaseURL(link, html, "text/html", "utf-8", "");
                            JSONArray array = jsonObject.getJSONArray("category_arr");
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject cat = array.getJSONObject(j);
                                category = String.valueOf(Html.fromHtml(cat.getString("name")));
                            }
                            String id1 = null;
                            String titlenew = null;
                            mTvCat.setText(category);
                            featured_image_link = jsonObject.getString("featured_image_link");
                            Glide.with(NewsDetailActivity.this).load(featured_image_link)
                                    .thumbnail(0.5f)
                                    .placeholder(R.drawable.bg_img)
                                    .error(R.drawable.bg_img)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(mImgMain);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(NewsDetailActivity.this, "no dataaaaa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(NewsDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getRelatedByNewsID(String parentId) {
        relatedListId = new String[4];
        final String[] title = new String[4];
        Call<JsonElement> call1 = RestClient.post().getNewsByCatID(parentId);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("News", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArr = new JSONArray(response.body().toString());
                        if (jsonArr.length() > 0) {
                            customtlist.clear();
                            int length = jsonArr.length();
                            if (length < 3)
                                length = jsonArr.length();
                            else
                                length = 3;
                            // int count = 0;
                            for (int i = 0; i < length; i++) {
                                JSONObject json2 = jsonArr.getJSONObject(i);
                                JSONObject objtitle = json2.getJSONObject("title");
                                CustomDataBean customDataBean = new CustomDataBean();
                                customDataBean.setId(json2.getString("id"));
                                customDataBean.setPost_title(objtitle.getString("rendered"));
                                customDataBean.setCount(String.valueOf(i + 1));
                                customtlist.add(customDataBean);
                            }
                        }
                        bindRelatedCategoryAdapternews();
                        Log.e("Cat size : ", customtlist.size() + "");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                nestedScrollView.scrollTo(0, 0);
                            }
                        }, SPLASH_TIME_OUT);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(NewsDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindRelatedCategoryAdapternews() {
        if (customtlist.size() > 0) {
            coustomlistAdapter = new CoustomlistAdapter(NewsDetailActivity.this, customtlist);
            coustomlistAdapter.setRelatedNewsSelectedListner(this);
            rvCustom.setAdapter(coustomlistAdapter);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void setOnRelatedNewsSelectedListner(int position, CustomDataBean customDataBean) {
        getNewsById(parentId, customDataBean.getId());

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewsById(parentId, newsId);

    }
}
