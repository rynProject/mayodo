package com.mayodo.news.Evenements;

import android.content.Context;
import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mayodo.news.R;
import com.mayodo.news.utils.SharedObjects;


import java.util.ArrayList;

public class AdapterHomeListCat extends RecyclerView.Adapter<AdapterHomeListCat.RecyclerVH> {
    Context c;
    private LayoutInflater inflater;
    ArrayList<EvenmentDataBean> arrEvenementList;
    OnNewsidSelectedListner onNewsidSelectedListner;
    SharedObjects sharedObjects;
    public AdapterHomeListCat(Context c, ArrayList<EvenmentDataBean> spacecrafts) {
        this.c = c;
        this.arrEvenementList = spacecrafts;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnNewsidSelectedListner(OnNewsidSelectedListner onNewsidSelectedListner) {
        this.onNewsidSelectedListner = onNewsidSelectedListner;
    }

    public interface OnNewsidSelectedListner {
        void setOnNewsidSelatedListner(int position, EvenmentDataBean evenmentDataBean);
    }

    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.custom_evenments, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerVH holder, final int position) {
        holder.txttitle.setText(Html.fromHtml(arrEvenementList.get(position).getTitle()));
        holder.txttime.setText(arrEvenementList.get(position).getDate());
        holder.textcat.setText(Html.fromHtml(arrEvenementList.get(position).getCategory_arr()));
        holder.textcat.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
        if (!arrEvenementList.get(position).getFeatured_image_link().equals("")) {
            Glide.with(c).load(arrEvenementList.get(position).getFeatured_image_link())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.bg_img)
                    .error(R.drawable.bg_img)
                    .override(200, 200)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivevenment);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //  private RecyclerView rvevenment;
            @Override
            public void onClick(View view) {
                onNewsidSelectedListner.setOnNewsidSelatedListner(position, arrEvenementList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrEvenementList.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder {
        TextView txttitle, txttime, txtcomment, textcat;
        ImageView ivevenment;

        public RecyclerVH(View itemView) {
            super(itemView);
            sharedObjects = new SharedObjects(c);
            txttitle = (TextView) itemView.findViewById(R.id.txttitle);
            txttime = (TextView) itemView.findViewById(R.id.texttime);
            txtcomment = (TextView) itemView.findViewById(R.id.txtcomment);
            textcat = (TextView) itemView.findViewById(R.id.txt_cat);
            ivevenment = (ImageView) itemView.findViewById((R.id.imgevn));
        }
    }
}