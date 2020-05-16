package com.mayodo.news.comment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayodo.news.Home.HomePageActivity;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<CommentBean> commentBeanList;
    ProgressDialog progressDialog;
    String postId;
    EditText edtComment;
    Button btnComment;
    Button btnAdd;
    String parentId;
    String title;
    TextView tvToolbarTitle;
    Toolbar toolbar;
    ImageView ivBackBtn;
    SharedObjects sharedObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        sharedObjects = new SharedObjects(CommentActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }
        ProgressDialogSetup();
        initView();
        showAllComment();
    }

    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        toolbar =  findViewById(R.id.toolbar);
        ivBackBtn = findViewById(R.id.ivBackBtn);
        edtComment = findViewById(R.id.edt_comment);
        btnComment = findViewById(R.id.btn_comment);
        btnAdd = findViewById(R.id.Addbtn);
        recyclerView = findViewById(R.id.rv_comment);
        commentBeanList = new ArrayList<>();
        commentAdapter = new CommentAdapter(CommentActivity.this, commentBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
        tvToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommentActivity.this, HomePageActivity.class);
                startActivity(i);
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra("NewsID");
            parentId = intent.getStringExtra("CATEGORY_IDS");
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CommentActivity.this, AddCommentActivity.class);
                intent.putExtra("NewsID", postId);
                startActivity(intent);

            }
        });
        ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setColorTheme();
    }
    private void setColorTheme() {
        toolbar.setBackgroundColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivBackBtn.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        tvToolbarTitle.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
    }
    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(CommentActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    private void showAllComment() {
        commentBeanList.clear();
        progressDialog.show();
        Call<JsonElement> call1 = RestClient.post().getAllComment(postId);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                Log.e("View All Comment", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json2 = jsonArray.getJSONObject(i);
                                JSONObject jsonObject = json2.getJSONObject("content");
                                String comment = jsonObject.getString("rendered").toString().trim();
                                String author_name = json2.getString("author_name").toString().trim();
                                String date = json2.getString("date").toString().trim();
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

                                commentBeanList.add(new CommentBean(comment, author_name, formattedDate));
                            }
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CommentActivity.this, "No Records Available", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CommentActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

}