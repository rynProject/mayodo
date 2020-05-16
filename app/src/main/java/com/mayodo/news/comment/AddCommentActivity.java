package com.mayodo.news.comment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.mayodo.news.Home.HomePageActivity;

import com.mayodo.news.R;
import com.mayodo.news.retrofit.RestClient;
import com.google.gson.JsonElement;
import com.mayodo.news.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCommentActivity extends AppCompatActivity {
    EditText edtComment;
    EditText edtname;
    EditText edtemail;
    Button btnComment;
    ProgressDialog progressDialog;
    private ImageView mIvBackBtn;
    private TextView logo;
    String newsId;
    String title;
    TextView tvtitle;
    TextView tvToolbarTitle;
    Toolbar toolbar;
    ImageView ivBackBtn;
    SharedObjects sharedObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        sharedObjects=new SharedObjects(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }
        ProgressDialogSetup();
        initView();
    }
    private void initView(){
        mIvBackBtn = (ImageView) findViewById(R.id.ivBackBtn);
        logo = (TextView) findViewById(R.id.tvToolbarTitle);
        edtComment = findViewById(R.id.edt_comment);
        edtname = findViewById(R.id.edt_name);
        edtemail = findViewById(R.id.edt_email);
        btnComment = findViewById(R.id.btn_comment);
        tvtitle = findViewById(R.id.tv_title);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        toolbar =  findViewById(R.id.toolbar);
        ivBackBtn = findViewById(R.id.ivBackBtn);
        Intent intent = getIntent();
        if (intent != null) {

            newsId = intent.getStringExtra("NewsID");
            title = intent.getStringExtra("TITLE");
        }

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddCommentActivity.this, HomePageActivity.class);
                startActivity(i);
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateField();
            }
        });
        mIvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvToolbarTitle.setText(title);
        setColorTheme();
    }
    private void setColorTheme() {
        btnComment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        toolbar.setBackgroundColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivBackBtn.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        tvToolbarTitle.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
    }
    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(AddCommentActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    public void addComment() {
        String comment_author = edtname.getText().toString().trim();

        String comment_author_email = edtemail.getText().toString().trim();

        String comment_content = edtComment.getText().toString().trim();

        String user_id = "user_id";

        progressDialog.show();
        Call<JsonElement> call = RestClient.post().addComment(newsId, comment_author, comment_author_email, comment_content, user_id);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equals("1")) {

                            edtComment.setText("");
                            edtname.setText("");
                            edtemail.setText("");
                            Toast.makeText(AddCommentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(AddCommentActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void ValidateField() {
        if (!validFullname()) {
            return;
        }
        if (!validateUsername()) {
            return;
        }
        if (!validBrifmsg()) {
            return;
        }
        addComment();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean validateUsername() {
        String username = edtemail.getText().toString().trim();

        if (username.isEmpty()) {

            edtemail.setError(getResources().getString(R.string.errUsernameRequired));
            requestFocus(edtemail);
            return false;
        } else if (edtemail.getText().length() < 3 || edtemail.getText().length() > 50) {

            edtemail.setError(getResources().getString(R.string.errUsernamelength1));
            requestFocus(edtemail);
            //  requestFocus(mEdtUsername);
            return false;
        } else if (!isEmailValid(username)) {
            edtemail.setError(getResources().getString(R.string.pleaseentervalidemailaddress));
            requestFocus(edtemail);
            return false;
        }
        return true;
    }

    private boolean validFullname() {
        String username = edtname.getText().toString().trim();

        if (username.isEmpty()) {

            edtname.setError(getResources().getString(R.string.fullnamefieldisrequired));
            requestFocus(edtname);
            return false;
        } else if (edtname.getText().length() < 3 || edtname.getText().length() > 30) {

            edtname.setError(getResources().getString(R.string.errUsernamelength));
            requestFocus(edtname);
            //  requestFocus(mEdtUsername);
            return false;
        }
        return true;
    }

    private boolean validBrifmsg() {
        String username = edtComment.getText().toString().trim();

        if (username.isEmpty()) {

            edtComment.setError(getResources().getString(R.string.messageisrequired));
            edtComment.requestFocus();
            return false;
        } else if (edtComment.getText().length() < 3 || edtComment.getText().length() > 150) {

            edtComment.setError(getResources().getString(R.string.errUsernamelength2));
            edtComment.requestFocus();
            //  requestFocus(mEdtUsername);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            AddCommentActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
