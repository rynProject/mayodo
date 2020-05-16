package com.mayodo.news.Setting;


import android.content.Context;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import androidx.core.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mayodo.news.R;
//import com.mayodo.news.utils.SharedObjects;
//import com.onesignal.OneSignal;

public class SettingFragment extends Fragment implements View.OnClickListener {
//    SharedObjects sharedObjects;
    private TextView mTxtpushnotify;
    private Switch mPushswitch;
    private Switch pushswitchfcm;
    private LinearLayout mLinpush;
    private TextView mAboutus;
    private ImageView mImgabouterrow;
    private LinearLayout mLinabout;
    private TextView mFaq;
    private boolean activityStarted = false;
    public static Context context;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(v);
        return v;
    }

    private void initView(@NonNull final View itemView) {
//        sharedObjects = new SharedObjects(getActivity());
        mTxtpushnotify = (TextView) itemView.findViewById(R.id.txtpushnotify);
        mTxtpushnotify.setOnClickListener(this);
        mPushswitch = (Switch) itemView.findViewById(R.id.pushswitch);
        pushswitchfcm = (Switch) itemView.findViewById(R.id.pushswitchfcm);
        mPushswitch.setOnClickListener(this);
        pushswitchfcm.setOnClickListener(this);
        mLinpush = (LinearLayout) itemView.findViewById(R.id.linpush);
        mAboutus = (TextView) itemView.findViewById(R.id.aboutus);
        mImgabouterrow = (ImageView) itemView.findViewById(R.id.imgabouterrow);
        mImgabouterrow.setOnClickListener(this);
        mLinabout = (LinearLayout) itemView.findViewById(R.id.linabout);

        mLinabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutFragment cameraImportFragment = new AboutFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, cameraImportFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


//        String showNotification = sharedObjects.preferencesEditor.getPreference("Notification");
//        if (showNotification.equalsIgnoreCase("yes")) {
//            mPushswitch.setChecked(true);
//        } else {
//            mPushswitch.setChecked(false);
//        }
//
//        mPushswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    OneSignal.setSubscription(true);
//                    if (activityStarted
//                            && getActivity().getIntent() != null
//                            && (getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) != 0) {
//                        getActivity().finish();
//                        return;
//                    }
//                    activityStarted = true;
//                    sharedObjects.preferencesEditor.setPreference("Notification", "yes");
//                } else {
//                    OneSignal.setSubscription(false);
//                    sharedObjects.preferencesEditor.setPreference("Notification", "no");
//                }
//            }
//        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtpushnotify:
                // TODO 18/03/06
                break;
            case R.id.imgabouterrow:
                // TODO 18/03/06
                break;
           /* case R.id.faqerrow:
                // TODO 18/03/06
                break;*/
            default:
                break;
        }
    }
}
