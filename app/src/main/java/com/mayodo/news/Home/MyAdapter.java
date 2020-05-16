package com.mayodo.news.Home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.mayodo.news.R;
import com.mayodo.news.utils.SharedObjects;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerVH> {
    Context c;
    ArrayList<CategoriesBean> arrCateList = new ArrayList<>();
    OnCategorySelectedListner onCategorySelectedListner;
    CategoriesBean catbean;
    SharedObjects sharedObjects;
    public MyAdapter(Context c, ArrayList<CategoriesBean> spacecrafts1) {
        this.c = c;
        this.arrCateList = spacecrafts1;
    }

    public void setOnCategorySelectedListner(OnCategorySelectedListner onCategorySelectedListner) {

        this.onCategorySelectedListner = onCategorySelectedListner;
    }

    public interface OnCategorySelectedListner {
        void setOnCategorySelatedListner(int position, CategoriesBean dataBean);

    }

    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.homehorizontalitem, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerVH holder, final int position) {
        holder.nameTxt.setText(Html.fromHtml(arrCateList.get(position).getName()));
        if (arrCateList.get(position).isSelected()) {

            holder.nameTxt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
            holder.nameTxt.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        } else {
            holder.nameTxt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getFooterColor())));
            holder.nameTxt.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //  private RecyclerView rvevenment;
            @Override
            public void onClick(View view) {
                onCategorySelectedListner.setOnCategorySelatedListner(position, arrCateList.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrCateList.size();
    }


    public class RecyclerVH extends RecyclerView.ViewHolder {
        TextView nameTxt;

        public RecyclerVH(View itemView) {
            super(itemView);
            sharedObjects = new SharedObjects(c);
            nameTxt = (TextView) itemView.findViewById(R.id.listtext);

        }
    }
}