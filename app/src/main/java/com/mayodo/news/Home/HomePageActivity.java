package com.mayodo.news.Home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.core.view.GravityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.google.android.gms.ads.InterstitialAd;
import com.mayodo.news.AlldataFragment;
import com.mayodo.news.Contactus.ContactusFragment;
import com.mayodo.news.R;
import com.mayodo.news.Search.SearchActivity;
import com.mayodo.news.Setting.SettingFragment;
import com.mayodo.news.YoutubeActivity;
import com.mayodo.news.utils.AppConstants;
import com.mayodo.news.utils.SharedObjects;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements ItemClickListener<NavigationDrawerModel>, MyAdapter.OnCategorySelectedListner {
    TextView setitem;
    boolean isAdd=true;//if value is false then ads are not display
    boolean doubleBackToExitPressedOnce = false;
    private MyAdapter myAdapter;
    private ArrayList<CategoriesBean> categorylist = new ArrayList<>();
    ProgressDialog progressDialog;
    String catid;
    SharedObjects sharedObjects;
    public static TextView tvToolbarTitle;
    LinearLayout llHome, llLivetv, llCategories, llContactus, llSetting, llFaq, llSharing, llRating;
//    InterstitialAd mInterstitialAd;
    public HomePageActivity() {
    }
    Toolbar toolbar;
    ImageView ivBack, ivHome, ivLive, ivCat, ivContact, ivSetting, ivShare, ivRate, ivLogo;
    TextView logo, tvHome, tvLive, tvCat, tvContact, tvSetting, tvShare, tvRate;
    LinearLayout lldrawer;
    DrawerLayout drawerLayout;
    private List<NavigationDrawerModel> mNavigationList = new ArrayList<>();
    RecyclerView rvNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        sharedObjects = new SharedObjects(HomePageActivity.this);

        ivLogo = findViewById(R.id.ivLogo);

        Glide.with(this).load(sharedObjects.getLogo())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.bg_img)
                .error(R.drawable.bg_img)
                .override(200, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivLogo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }

        isNetworkConnectionAvailable();

            loadFragment(new HomeFragment(), R.id.fl_container, true);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText(getString(R.string.app_name));
        drawerLayout = findViewById(R.id.drawerlayout);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        final DrawerLayout finalDrawer = drawerLayout;
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalDrawer.isDrawerVisible(GravityCompat.START)) {
                    finalDrawer.closeDrawer(GravityCompat.START);
                } else {
                    finalDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();
        ivBack = findViewById(R.id.ivBackBtn);
        ivHome = findViewById(R.id.ivHome);
        tvHome = findViewById(R.id.tvHome);
        ivLive = findViewById(R.id.ivLive);
        tvLive = findViewById(R.id.tvLive);
        ivCat = findViewById(R.id.ivCat);
        tvCat = findViewById(R.id.tvCat);
        ivContact = findViewById(R.id.ivContact);
        tvContact = findViewById(R.id.tvContact);
        ivSetting = findViewById(R.id.ivSetting);
        tvSetting = findViewById(R.id.tvSetting);
        ivShare = findViewById(R.id.ivShare);
        tvShare = findViewById(R.id.tvShare);
        ivRate = findViewById(R.id.ivRate);
        tvRate = findViewById(R.id.tvRate);

        logo = findViewById(R.id.tvToolbarTitle);
        rvNavigation = findViewById(R.id.rv_navigation);
        llHome = findViewById(R.id.ll_home);
        llLivetv = findViewById(R.id.ll_livetv);
        llCategories = findViewById(R.id.ll_categories);
        llContactus = findViewById(R.id.ll_contact_us);
        llSetting = findViewById(R.id.ll_setting);
        llFaq = findViewById(R.id.ll_faq);
        llSharing = findViewById(R.id.ll_share);
        llRating = findViewById(R.id.ll_rate);
        lldrawer = findViewById(R.id.lldrawer);

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarTitle.setText(getString(R.string.app_name));
                drawerLayout.closeDrawer(GravityCompat.START);

                    loadFragment(new HomeFragment(), R.id.fl_container, false);
            }
        });
        llLivetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarTitle.setText(getString(R.string.app_name));
                drawerLayout.closeDrawer(GravityCompat.START);
                /*   replaceFragment(new LiveTvFragment(), R.id.fl_container, true);*/
                Intent i = new Intent(HomePageActivity.this, YoutubeActivity.class);
                startActivity(i);
              /*  startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=F-nNTGK0wFw")));
                Log.i("Video", "Video Playing....");*/
            }
        });
        llCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarTitle.setText(getString(R.string.app_name));
                drawerLayout.closeDrawer(GravityCompat.START);

                    loadFragment(new CategoriesFragment(), R.id.fl_container, false);

            }
        });
        llContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarTitle.setText(getString(R.string.app_name));
                drawerLayout.closeDrawer(GravityCompat.START);
                loadFragment(new ContactusFragment(), R.id.fl_container, false);
            }
        });
        llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarTitle.setText(getString(R.string.app_name));
                drawerLayout.closeDrawer(GravityCompat.START);
                loadFragment(new SettingFragment(), R.id.fl_container, false);
            }
        });
        llFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarTitle.setText(getString(R.string.app_name));
                drawerLayout.closeDrawer(GravityCompat.START);
            //    loadFragment(new FAQFragment(), R.id.fl_container, false);
            }
        });
        llSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.example.admin.wpnews.Home");//com.itechnotion.allinone
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        llRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRateDialog();
            }
        });
        ivBack.setImageResource(R.drawable.menuicon);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment(), R.id.fl_container, false);
            }
        });
        initComponent();

        setColorTheme();

    }

    private void setColorTheme() {
        toolbar.setBackgroundColor(Color.parseColor(sharedObjects.getHeaderColor()));
        lldrawer.setBackgroundColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        ivBack.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        tvToolbarTitle.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        ivHome.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        tvHome.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivLive.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        tvLive.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivCat.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        tvCat.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivContact.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        tvContact.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivSetting.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        tvSetting.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivShare.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        tvShare.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        ivRate.setColorFilter(Color.parseColor(sharedObjects.getHeaderColor()));
        tvRate.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));

    }

    public void showRateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this)
                .setTitle("Rate application")
                .setMessage("Please, rate the app at Playstore")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getApplication() != null) {
                            String link = "market://details?id=";
                            try {
                                // play market available
                                getPackageManager()
                                        .getPackageInfo("com.itechnotion.allinone", 0);
                                // not available
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                // should use browser
                                link = "https://play.google.com/store/apps/details?id=";
                            }
                            // starts external action
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(link + HomePageActivity.this.getPackageName())));
                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }

    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView titleView = (TextView) alertDialog.findViewById(android.R.id.message);
        titleView.setGravity(Gravity.CENTER);

    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            //     sharedObjects.getFeaturnews("FeatureNews");
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()), PorterDuff.Mode.SRC_ATOP);
        }
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        ImageView searchIcon = searchView.findViewById(R.id.search_mag_icon);
        searchIcon.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));

        ImageView searchClose = searchView.findViewById(R.id.search_close_btn);
        searchClose.setColorFilter(Color.parseColor(sharedObjects.getPrimaryColor()));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        EditText searchEditText = searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.parseColor(sharedObjects.getFooterColor()));

       searchEditText.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        searchEditText.setDrawingCacheBackgroundColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        searchEditText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("Submit", query);

                (menu.findItem(R.id.action_search)).collapseActionView();
                Intent intent = new Intent(HomePageActivity.this, SearchActivity.class);
                intent.putExtra(AppConstants.KEYWORD, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TextChange", newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void ProgressDialogSetup() {
        if (HomePageActivity.this != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
    }

    private void initComponent() {
        ProgressDialogSetup();
        rvNavigation.setLayoutManager(new LinearLayoutManager(this));
        rvNavigation.setNestedScrollingEnabled(false);
    }

    private void loadFragment(Fragment fragment, int fl_container, boolean b) {
        String backStateName = fragment.getClass().getName();
        FragmentManager fm = getSupportFragmentManager();
        boolean fragmentPopped = fm.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fl_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onItemClicked(int position, SubCategoriesModel model) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            mNavigationList.get(position).setSelected(true);
        Bundle bundle = new Bundle();
        bundle.putString("id", model.getCatID());
        bundle.putInt("position", position);
        AlldataFragment alldataFragment = new AlldataFragment();
        alldataFragment.setArguments(bundle);
        loadFragment(alldataFragment, R.id.fl_container, false);

    }

    @Override
    public void setOnCategorySelatedListner(int position, CategoriesBean dataBean) {
        for (int i = 0; i < categorylist.size(); i++) {
            categorylist.get(i).setSelected(false);
        }
        myAdapter.notifyDataSetChanged();
        catid = dataBean.getId();
        setitem.setText(dataBean.getName());
        dataBean.setSelected(true);

    }

    @Override
    public void onBackPressed() {
        progressDialog.dismiss();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        tvToolbarTitle.setText(getString(R.string.app_name));
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            drawer.closeDrawer(GravityCompat.START);
            finish();
        } else {
            super.onBackPressed();
        }

    }

}
