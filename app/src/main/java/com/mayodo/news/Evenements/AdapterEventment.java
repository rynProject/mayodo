package com.mayodo.news.Evenements;

import android.content.Context;
import android.graphics.Color;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mayodo.news.R;
import com.mayodo.news.utils.SharedObjects;

import java.util.ArrayList;

public class AdapterEventment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context c;
    private LayoutInflater inflater;
    ArrayList<EvenmentDataBean> arrEvenementList;
    OnNewsidSelectedListner onNewsidSelectedListner;
    RecyclerView mRecyclerView;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    public boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    SharedObjects sharedObjects;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public AdapterEventment(Context c, ArrayList<EvenmentDataBean> spacecrafts, RecyclerView recyclerView) {
        this.c = c;
        this.arrEvenementList = spacecrafts;
        this.mRecyclerView = recyclerView;
        inflater = LayoutInflater.from(c);
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return arrEvenementList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnNewsidSelectedListner(OnNewsidSelectedListner onNewsidSelectedListner) {

        this.onNewsidSelectedListner = onNewsidSelectedListner;
    }

    public interface OnNewsidSelectedListner {
        void setOnNewsidSelatedListner(int position, EvenmentDataBean evenmentDataBean);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_evenments, parent, false);
            return new MyViewholder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewholder) {
            final MyViewholder myHolder = (MyViewholder) holder;
            myHolder.txttitle.setText(Html.fromHtml(arrEvenementList.get(position).getTitle()));
            myHolder.txttime.setText(arrEvenementList.get(position).getDate());
            myHolder.textcat.setText(Html.fromHtml(arrEvenementList.get(position).getCategory_arr()));
            myHolder.textcat.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
            if (!arrEvenementList.get(position).getFeatured_image_link().equals("")) {
                Glide.with(c).load(arrEvenementList.get(position).getFeatured_image_link())
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.bg_img)
                        .error(R.drawable.bg_img)
                        .override(200, 200)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myHolder.ivevenment);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                //  private RecyclerView rvevenment;
                @Override
                public void onClick(View view) {
                    onNewsidSelectedListner.setOnNewsidSelatedListner(position, arrEvenementList.get(position));
                }
            });
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return arrEvenementList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView txttitle, txttime, txtcomment, textcat;
        ImageView ivevenment;

        public MyViewholder(View itemView) {
            super(itemView);
            sharedObjects = new SharedObjects(c);
            txttitle = (TextView) itemView.findViewById(R.id.txttitle);
            txttime = (TextView) itemView.findViewById(R.id.texttime);
            txtcomment = (TextView) itemView.findViewById(R.id.txtcomment);
            textcat = (TextView) itemView.findViewById(R.id.txt_cat);
            ivevenment = (ImageView) itemView.findViewById((R.id.imgevn));
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }
}