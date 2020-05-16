package com.mayodo.news.Home;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.mayodo.news.R;
import com.mayodo.news.utils.SharedObjects;

import java.util.ArrayList;
import java.util.List;

public class SubListAdepater extends RecyclerView.Adapter<SubListAdepater.ViewHolder>
{
    private Context mContext;
    private List<SubCategoriesModel> mList;
    private ItemClickListener<SubCategoriesModel> itemClickListener;
    SharedObjects sharedObjects;
    public SubListAdepater(Context mContext, ArrayList<SubCategoriesModel> mList, SubCategoriesFragment itemClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_navigation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SubCategoriesModel mSubCategoriesModel = mList.get(position);
        holder.tvNavigation.setText(mSubCategoriesModel.getCatName());
        holder.char_nm.setText(mSubCategoriesModel.getCatFirstChar());
        holder.char_nm.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        holder.tvNavigation.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        // holder.llbg.setBackgroundColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        holder.llbg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        holder.llNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ListCategoriesActivity.class);
                intent.putExtra("CATEGORY_ID", mSubCategoriesModel.getCatID());
                intent.putExtra("CATEGORY_NM", mSubCategoriesModel.getCatName());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNavigation, char_nm;
        LinearLayout llNavigation;
        LinearLayout llbg;
        ViewHolder(View view) {
            super(view);
            tvNavigation = itemView.findViewById(R.id.tv_navigation);
            llNavigation = itemView.findViewById(R.id.ll_navigation);
            char_nm = itemView.findViewById(R.id.char_nm);
            llbg = itemView.findViewById(R.id.llbg);
            sharedObjects = new SharedObjects(mContext);
        }
    }
}


