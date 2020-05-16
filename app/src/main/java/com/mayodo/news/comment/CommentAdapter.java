package com.mayodo.news.comment;

import android.content.Context;
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

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.RecyclerVH> {
    Context c;
    private LayoutInflater inflater;
    List<CommentBean> commentBeans;
    SharedObjects sharedObjects;

    public CommentAdapter(Context c, List<CommentBean> commentBeans) {
        this.c = c;
        this.commentBeans = commentBeans;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.raw_comment, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerVH holder, int position) {
        holder.txttitle.setText(Html.fromHtml(commentBeans.get(position).getTitle()).toString().trim());
        holder.txtname.setText(commentBeans.get(position).getAuthor_name());
        holder.txtemail.setText(commentBeans.get(position).getDate());
        holder.txtname.setTextColor(Color.parseColor(sharedObjects.getPrimaryColor()));
    }

    @Override
    public int getItemCount() {
        return commentBeans.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder {
        TextView txttitle;
        TextView txtname;
        TextView txtemail;

        public RecyclerVH(View itemView) {
            super(itemView);
            sharedObjects=new SharedObjects(c);
            txttitle = (TextView) itemView.findViewById(R.id.title);
            txtname = (TextView) itemView.findViewById(R.id.name);
            txtemail = (TextView) itemView.findViewById(R.id.email);

        }
    }
}
