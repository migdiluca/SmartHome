package com.smartdesigns.smarthomehci;


import android.content.Context;
import android.content.Intent;

import com.smartdesigns.smarthomehci.backend.RecyclerInterface;
import com.smartdesigns.smarthomehci.backend.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class RecyclerViewAdapter<T extends RecyclerInterface & Serializable> extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<T> mData ;


    public RecyclerViewAdapter(Context mContext, List<T> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(mData.get(position).getName());

        holder.img_thumbnail.setImageResource(Integer.parseInt(mData.get(position).getMeta()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = mData.get(position).getChildFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("Object", mData.get(position));
                fragment.setArguments(arguments);

                Home home = Home.getInstance();
                home.setFragmentWithStack(fragment);


            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.card_view_title) ;
            img_thumbnail = (ImageView) itemView.findViewById(R.id.card_view_img);
            cardView = (CardView) itemView.findViewById(R.id.card_view);


        }
    }


}
