package com.mayodo.news.Home;

import android.content.Context;
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

import java.util.List;


public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.ViewHolder> {
    Context mContext;
    private List<SubCategoriesModel> mList;
    private List<SubCategoriesModel> mSubList;
    private CategoriesFragment itemClickListener;
    SharedObjects sharedObjects;
    public SubCatAdapter(Context mContext, List<SubCategoriesModel> mList, CategoriesFragment itemClickListener) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SubCategoriesModel mNavigationDrawerModel = mList.get(position);
        holder.tvNavigation.setText(mNavigationDrawerModel.getCatName());
        holder.char_nm.setText(mNavigationDrawerModel.getCatFirstChar());
        holder.char_nm.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        holder.tvNavigation.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        holder.llbg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        holder.llNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClicked(holder.getAdapterPosition(), mNavigationDrawerModel);
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
            sharedObjects = new SharedObjects(mContext);
            //ivNavigation=itemView.findViewById(R.id.iv_navigation);
            tvNavigation = itemView.findViewById(R.id.tv_navigation);
            llNavigation = itemView.findViewById(R.id.ll_navigation);
            llbg = itemView.findViewById(R.id.llbg);
            char_nm = itemView.findViewById(R.id.char_nm);
        }
    }
}
