package com.mayodo.news.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentTransaction;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.mayodo.news.R;
import com.mayodo.news.retrofit.RestClient;
import com.mayodo.news.utils.SharedObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements ItemClickListener<NavigationDrawerModel>{
    RecyclerView rvNavigation;
    private SubCatAdapter mNavigationDrawerAdapter;
    View view;
    LinearLayout ll_allcat;
    TextView char_nm;
    TextView tv_navigation;
    ProgressDialog progressDialog;
    String id;
    String catname;
    ArrayList<SubCategoriesModel> CatArrayList = new ArrayList<>();
    ArrayList<SubCategoriesModel.SubCatModel> subCatArrayList = new ArrayList<>();
    String parentId;
    SubCategoriesModel subCategoriesModel;
    SubCategoriesModel.SubCatModel subCatModel;
    SharedObjects sharedObjects;
    public CategoriesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        sharedObjects=new SharedObjects(getActivity());
        ProgressDialogSetup();
        Initialization();
        Category();
        return view;
    }
    private void Initialization(){

        ll_allcat = view.findViewById(R.id.ll_allcat);
        char_nm = view.findViewById(R.id.char_nm);
        tv_navigation = view.findViewById(R.id.tv_navigation);
        ll_allcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), ListCategoriesActivity.class);
                startActivity(intent);

            }
        });
        ll_allcat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        char_nm.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        tv_navigation.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        HomePageActivity.tvToolbarTitle.setText("Categories");
        rvNavigation = (RecyclerView) view.findViewById(R.id.rv_navigation);
    }
    private void Category() {
        progressDialog.show();
        Call<JsonElement> call1 = RestClient.post().category();
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                try {
                    fetchCategoryResponse(response.body().toString());
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

    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }
    private void fetchCategoryResponse(String jsonRes) throws JSONException {
        ll_allcat.setVisibility(View.VISIBLE);
        JSONArray jsonArr = new JSONArray(jsonRes);

        if (jsonArr.length() > 0) {
            CatArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonCatObject = jsonArr.getJSONObject(i);

                if (jsonCatObject.getString("parent").equals("0")) {
                    subCategoriesModel = new SubCategoriesModel();
                    subCategoriesModel.setCatID(jsonCatObject.getString("id"));
                    subCategoriesModel.setCatName(String.valueOf(Html.fromHtml(jsonCatObject.getString("name"))));
                    subCategoriesModel.setParent(jsonCatObject.getString("parent"));
                    catname = String.valueOf(Html.fromHtml(jsonCatObject.getString("name")));
                    String firstChar = String.valueOf(catname.charAt(0));
                    subCategoriesModel.setCatFirstChar(firstChar);
                    CatArrayList.add(subCategoriesModel);
                }
            }

            for (int j = 0; j < jsonArr.length(); j++) {
                JSONObject jsonCatObject = jsonArr.getJSONObject(j);
                for (int k = 0; k < CatArrayList.size(); k++) {
                    subCatArrayList = new ArrayList<>();
                    if (CatArrayList.get(k).getCatID().equals(jsonCatObject.getString("parent"))) {
                        subCategoriesModel = new SubCategoriesModel();
                        subCatModel = subCategoriesModel.new SubCatModel();
                        subCatModel.setSubCatID(jsonCatObject.getString("id"));
                        subCatModel.setSubCatName(String.valueOf(Html.fromHtml(jsonCatObject.getString("name"))));
                        parentId = jsonCatObject.getString("parent");
                        subCatModel.setParentCatID(parentId);
                        String Subcatname = String.valueOf(Html.fromHtml(jsonCatObject.getString("name")));
                        String SubCatfirstChar = String.valueOf(Subcatname.charAt(0));
                        subCatModel.setSubCatFirstChar(SubCatfirstChar);
                        subCatArrayList = CatArrayList.get(k).getSubCatArrList();
                        subCatArrayList.add(subCatModel);
                        CatArrayList.get(k).setSubCatArrList(subCatArrayList);
                    }
                }
            }

            bindCategoryAdapter();
        }
    }

    private void bindCategoryAdapter() {
        if (CatArrayList.size() > 0) {

            rvNavigation.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvNavigation.setAdapter(mNavigationDrawerAdapter);
            mNavigationDrawerAdapter = new SubCatAdapter(getActivity(), CatArrayList,this);

            rvNavigation.setAdapter(mNavigationDrawerAdapter);
            mNavigationDrawerAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "No item found", Toast.LENGTH_SHORT).show();

        }
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


    @Override
    public void onItemClicked(int position, SubCategoriesModel model) {
        if (model.getSubCatArrList().size()>0) {
            Bundle bundle = new Bundle();
            bundle.putString("CATEGORY_ID", model.getCatID());
            bundle.putString("CATEGORY_NM", model.getCatName());
            SubCategoriesFragment subCategoriesFragment = new SubCategoriesFragment();
            subCategoriesFragment.setArguments(bundle);
            replaceFragment(subCategoriesFragment, R.id.fl_container, true);

        } else {
            Intent intent = new Intent(getActivity(), ListCategoriesActivity.class);
            intent.putExtra("CATEGORY_ID", model.getCatID());
            intent.putExtra("CATEGORY_NM", model.getCatName());
            startActivity(intent);

        }
    }
}
