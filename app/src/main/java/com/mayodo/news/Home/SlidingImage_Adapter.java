package com.mayodo.news.Home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Parcelable;
//import androidx.core.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mayodo.news.Evenements.EvenmentDataBean;
import com.mayodo.news.R;
import com.mayodo.news.utils.SharedObjects;


import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private TextView mTvTitle;
    private TextView mTvPubDate;
    private TextView mBtnCategory;
    private TextView tvlike;
    private TextView tvcomment;
    ArrayList<EvenmentDataBean> featurNewslist;
    OnNewsSelectedListner onNewsSelectedListner;
    SharedObjects sharedObjects;

    public SlidingImage_Adapter(Context context, ArrayList<Integer> IMAGES, ArrayList<EvenmentDataBean> featurNewslist, OnNewsSelectedListner onNewsSelectedListner) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.featurNewslist = featurNewslist;
        this.onNewsSelectedListner = onNewsSelectedListner;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnNewsSelectedListner {
        void setOnNewsSelectedListner(int position, EvenmentDataBean evenmentDataBean);

        void setOnNewsSelatedListner(int position, EvenmentDataBean evenmentDataBean);
    }

    @Override
    public int getCount() {
        return featurNewslist.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.viewpagerimg);
        sharedObjects = new SharedObjects(context);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onNewsSelectedListner.setOnNewsSelectedListner(position, featurNewslist.get(position));
            }
        });

        if (context==null){

        }else {
            Glide.with(context).load(featurNewslist.get(position).getFeatured_image_link())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.bg_img)
                    .error(R.drawable.bg_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        mTvTitle = (TextView) imageLayout.findViewById(R.id.tvTitle);
        mTvTitle.setText(Html.fromHtml(featurNewslist.get(position).getTitle()));
        mTvPubDate = (TextView) imageLayout.findViewById(R.id.tvPubDate);
        mTvPubDate.setText(Html.fromHtml(featurNewslist.get(position).getDate()));
        mBtnCategory = (TextView) imageLayout.findViewById(R.id.txtcate);
        mBtnCategory.setText(Html.fromHtml(featurNewslist.get(position).getCategory_arr()));
        mBtnCategory.setText(featurNewslist.get(position).getCategory_arr());
        tvcomment = (TextView) imageLayout.findViewById(R.id.txtcomment);

        mBtnCategory.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        mBtnCategory.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
