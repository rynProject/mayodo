package com.mayodo.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.mayodo.news.R;

/**
 * Created by Sunil on 23-Oct-16.
 */
public class SharedObjects extends MultiDexApplication {

    public static Context context;
    public PreferencesEditor preferencesEditor = new PreferencesEditor();
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreference;
    public static String PREF_NAME = "WpnewsPref";
    public static Context getContext() {
        return context;
    }

    public SharedObjects(Context context) {
        this.context = context;sharedPreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();

        initializeStetho();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void initializeStetho() {
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(context);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context));
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }

    public void setColor(String header_color, String footer_color, String primary_color) {

        editor.putString(AppConstants.KEY_HEADERCOLOR,header_color);
        editor.putString(AppConstants.KEY_FOOTERCOLOR,footer_color);
        editor.putString(AppConstants.KEY_PRIMARYCOLOR,primary_color);
        editor.commit();
        editor.apply();
    }
    public void setLogo(String logo) {

        editor.putString(AppConstants.KEY_LOGO,logo);
    }
    public String getLogo() {
        return   sharedPreference.getString(AppConstants.KEY_LOGO, String.valueOf(R.drawable.splash));//#FFFFFF

    }
    public String getHeaderColor() {
        return   sharedPreference.getString(AppConstants.KEY_HEADERCOLOR,"#FFFFFF");//#FFFFFF

    }
    public String getFooterColor() {
        return sharedPreference.getString(AppConstants.KEY_FOOTERCOLOR,"#98C9FF");//#FFFFFF
    }
    public String getPrimaryColor() {
        return  sharedPreference.getString(AppConstants.KEY_PRIMARYCOLOR,"#007aff");//#007aff
    }

    public class PreferencesEditor {

        public void setPreference(String key, String value) {
            SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putString(key, value);
            editor.apply();
            editor.commit();
        }

        public String getPreference(String key) {
            try {
                SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
                return sharedPreference.getString(key, "");
            } catch (Exception exception) {
                return "";
            }
        }



        public void setBoolean(String key, boolean value) {
            SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }

        public Boolean getBoolean(String key) {
            try {
                SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
                return sharedPreference.getBoolean(key, true);
            } catch (Exception exception) {
                return false;
            }
        }

    }
}
