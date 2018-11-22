package com.smartdesigns.smarthomehci.Utils;


import android.content.Context;

import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.R;
import com.smartdesigns.smarthomehci.backend.RecyclerInterface;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RecyclerViewAdapter<T extends RecyclerInterface & Serializable> extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<T> mData ;
    private static int columns = 2;
    private static List<Integer> colors = new ArrayList<>();
    private static final int IMAGE_SIZE = 90;

    public static List<Integer> getColors(){
        if(colors.isEmpty())
            addColors();
        return colors;
    }

    public RecyclerViewAdapter(Context mContext, List<T> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public static int getColumns() {return columns;};

    public static void setColumns(int columns){
        RecyclerViewAdapter.columns = columns;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        addColors();

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item,parent,false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        TextView tv = (TextView) view.findViewById(R.id.card_view_title);

        ViewGroup.MarginLayoutParams marginParam =  (ViewGroup.MarginLayoutParams) view.findViewById(R.id.card_view).getLayoutParams();

        lp.width = ((parent.getMeasuredWidth()) / columns) - marginParam.bottomMargin * 2;

        tv.getLayoutParams().width = lp.width;
        tv.getLayoutParams().height = lp.width* 25 / 100;
        tv.setTextSize((float) (tv.getTextSize() / (columns /((columns*2.1+10)/10))));

        lp.height = lp.width + tv.getLayoutParams().height;

        ImageView iv = (ImageView) view.findViewById(R.id.card_view_img);
        iv.getLayoutParams().height = (lp.width) * IMAGE_SIZE / 100;
        iv.getLayoutParams().width = (lp.width) * IMAGE_SIZE/100;
        iv.setPadding((lp.width * (100-IMAGE_SIZE)) / 100, 0,0,0);


        view.setLayoutParams(lp);
        return new MyViewHolder(view);
    }

    private static void addColors() {
        if(colors.isEmpty()){
            Context context = Home.getInstance().getApplicationContext();
            colors.add(ContextCompat.getColor(context,R.color.amber));
            colors.add(ContextCompat.getColor(context,R.color.blue));
            colors.add(ContextCompat.getColor(context,R.color.blue_grey));
            colors.add(ContextCompat.getColor(context,R.color.yellow));
            colors.add(ContextCompat.getColor(context,R.color.brown));
            colors.add(ContextCompat.getColor(context,R.color.cyan));
            colors.add(ContextCompat.getColor(context,R.color.deep_orange));
            colors.add(ContextCompat.getColor(context,R.color.deep_purple));
            colors.add(ContextCompat.getColor(context,R.color.green));
            colors.add(ContextCompat.getColor(context,R.color.grey));
            colors.add(ContextCompat.getColor(context,R.color.indigo));
            colors.add(ContextCompat.getColor(context,R.color.light_blue));
            colors.add(ContextCompat.getColor(context,R.color.light_green));
            colors.add(ContextCompat.getColor(context,R.color.lime));
            colors.add(ContextCompat.getColor(context,R.color.orange));
            colors.add(ContextCompat.getColor(context,R.color.pink));
            colors.add(ContextCompat.getColor(context,R.color.purple));
            colors.add(ContextCompat.getColor(context,R.color.red));
            colors.add(ContextCompat.getColor(context,R.color.teal));
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(mData.get(position).getName());
        //holder.img_thumbnail.setImageResource(Integer.parseInt(mData.get(position).getMeta()));

        /*if(true){
            mData.get(position).setBackground(colors.get(ThreadLocalRandom.current().nextInt(0,colors.size())));
        }*/

        holder.cardView.setBackgroundColor(mData.get(position).getBackground());
        holder.cardView.setCardElevation(20);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.get(position).onClickAction(mData.get(position), mContext);
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
