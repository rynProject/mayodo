package com.mayodo.news.Contactus;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import retrofit2.Response;

/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class ContactusFragment extends Fragment implements View.OnClickListener {
    private EditText mEdtFullName;
    private EditText mEdtEmail;
    private EditText mEdtMsg;
    private Button mBtnSend;
    ProgressDialog progressDialog;
    SharedObjects sharedObjects;

    public ContactusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contactus, container, false);
        sharedObjects=new SharedObjects(getActivity());
        ProgressDialogSetup();
        initView(v);
        return v;
    }

    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    private void initView(@NonNull final View itemView) {
        mEdtFullName = (EditText) itemView.findViewById(R.id.edtFullName);
        mEdtEmail = (EditText) itemView.findViewById(R.id.edtEmail);
        mEdtMsg = (EditText) itemView.findViewById(R.id.edtMsg);
        mBtnSend = (Button) itemView.findViewById(R.id.btnSend);
        mBtnSend.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        mBtnSend.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                ValidateField();
                break;
            default:
                break;
        }
    }

    private void Contactus() {
        String conname = mEdtFullName.getText().toString();
        String conemail = mEdtEmail.getText().toString();
        String conmessage = mEdtMsg.getText().toString();

        progressDialog.show();
        Call<JsonElement> call = RestClient.post().contactus(conname, conemail, conmessage);
        call.enqueue(new retrofit2.Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                Log.e("Contact us: ", response.body().toString());
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equals("1")) {
                            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), HomePageActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
        Contactus();
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
        String username = mEdtEmail.getText().toString().trim();

        if (username.isEmpty()) {

            mEdtEmail.setError(getResources().getString(R.string.errUsernameRequired));
            requestFocus(mEdtEmail);
            return false;
        } else if (mEdtEmail.getText().length() < 3 || mEdtEmail.getText().length() > 50) {

            mEdtEmail.setError(getResources().getString(R.string.errUsernamelength1));
            requestFocus(mEdtEmail);
            //  requestFocus(mEdtUsername);
            return false;
        } else if (!isEmailValid(username)) {
            mEdtEmail.setError(getResources().getString(R.string.pleaseentervalidemailaddress));
            requestFocus(mEdtEmail);
            return false;
        }
        return true;
    }

    private boolean validFullname() {
        String username = mEdtFullName.getText().toString().trim();

        if (username.isEmpty()) {

            mEdtFullName.setError(getResources().getString(R.string.fullnamefieldisrequired));
            requestFocus(mEdtFullName);
            return false;
        } else if (mEdtFullName.getText().length() < 3 || mEdtFullName.getText().length() > 30) {

            mEdtFullName.setError(getResources().getString(R.string.errUsernamelength));
            requestFocus(mEdtFullName);
            //  requestFocus(mEdtUsername);
            return false;
        }
        return true;
    }

    private boolean validBrifmsg() {
        String username = mEdtMsg.getText().toString().trim();

        if (username.isEmpty()) {

            mEdtMsg.setError(getResources().getString(R.string.messageisrequired));
            mEdtMsg.requestFocus();
            return false;
        } else if (mEdtMsg.getText().length() < 3 || mEdtMsg.getText().length() > 150) {

            mEdtMsg.setError(getResources().getString(R.string.errUsernamelength2));
            mEdtMsg.requestFocus();
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
