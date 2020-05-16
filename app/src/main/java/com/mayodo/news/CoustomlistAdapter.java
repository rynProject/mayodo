package com.mayodo.news;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.mayodo.news.utils.SharedObjects;

import java.util.ArrayList;

public class CoustomlistAdapter extends RecyclerView.Adapter<CoustomlistAdapter.RecyclerVH> {

    Context c;
    private LayoutInflater inflater;
    ArrayList<CustomDataBean> arrCustomlist;
    OnRelatedNewsSelectedListner relatedNewsSelectedListner;

    SharedObjects sharedObjects;
    public CoustomlistAdapter(Context c,ArrayList<CustomDataBean> spacecrafts) {
        this.c = c;
        this.arrCustomlist = spacecrafts;
        inflater = LayoutInflater.from(c);
    }
    public void setRelatedNewsSelectedListner(OnRelatedNewsSelectedListner onRelatedNewsSelectedListner) {
        this.relatedNewsSelectedListner=onRelatedNewsSelectedListner;
    }
    public interface OnRelatedNewsSelectedListner{
        void setOnRelatedNewsSelectedListner(int position, CustomDataBean customDataBean);
    }
    @Override
    public CoustomlistAdapter.RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.newsdetail_coustomlist,parent,false);
        return new CoustomlistAdapter.RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerVH holder,final int position) {
        holder.txttitle.setText(Html.fromHtml(arrCustomlist.get(position).getPost_title()));
        holder.tvCount.setText(arrCustomlist.get(position).getCount());
        holder.tvCount.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sharedObjects.getPrimaryColor())));
        holder.tvCount.setTextColor(Color.parseColor(sharedObjects.getHeaderColor()));

        final CustomDataBean customDataBean=new CustomDataBean();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //  private RecyclerView rvevenment;
            @Override
            public void onClick(View view) {
                relatedNewsSelectedListner.setOnRelatedNewsSelectedListner(position,arrCustomlist.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrCustomlist.size();
    }
    public class RecyclerVH extends RecyclerView.ViewHolder
    {
        LinearLayout ll_related_list;
        TextView txttitle;
        TextView tvCount;

        public RecyclerVH(View itemView) {
            super(itemView);
            sharedObjects=new SharedObjects(c);
            txttitle= (TextView) itemView.findViewById(R.id.title);
            tvCount= (TextView) itemView.findViewById((R.id.tv_count));
        }
    }}
