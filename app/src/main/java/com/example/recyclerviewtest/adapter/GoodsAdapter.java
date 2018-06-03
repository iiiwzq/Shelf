package com.example.recyclerviewtest.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recyclerviewtest.Activity.ShowGoodsActivity;
import com.example.recyclerviewtest.myclass.Goods;
import com.example.recyclerviewtest.R;

import java.util.List;

/**
 * Created by lenovo on 2018/5/19.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    private Context mContext;

    private List<Goods> mGoodsList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        //        View GoodsView;
        CardView cardView;
        TextView GoodsName;
        TextView GoodsClassification;
        TextView GoodsPrice;
        ImageView GoodsImage;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            GoodsName = (TextView) view.findViewById(R.id.goodsName);
            GoodsClassification = (TextView) view.findViewById(R.id.classification);
            GoodsPrice = (TextView) view.findViewById(R.id.price);
            GoodsImage = (ImageView) view.findViewById(R.id.goods_image);
        }
    }
    public GoodsAdapter(List<Goods> GoodsList){
        mGoodsList = GoodsList;
    }

    @Override
    public GoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.goods_item,parent,false);
        final GoodsAdapter.ViewHolder holder = new GoodsAdapter.ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Goods goods = mGoodsList.get(position);
                Intent intent = new Intent(mContext, ShowGoodsActivity.class);
                intent.putExtra(ShowGoodsActivity.GOODS_NAME,goods.getGoodsName());
                intent.putExtra(ShowGoodsActivity.PRICE,goods.getPrice());
                intent.putExtra(ShowGoodsActivity.IMAGE,goods.getImage());
                intent.putExtra(ShowGoodsActivity.GOODS_ID,goods.getID());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(GoodsAdapter.ViewHolder holder, int position){
        Goods Goods = mGoodsList.get(position);
        holder.GoodsName.setText(Goods.getGoodsName());
        holder.GoodsClassification.setText(Goods.getClassification());
        holder.GoodsPrice.setText(Goods.getPrice());
        Glide.with(mContext).load(Goods.getImage()).into(holder.GoodsImage);
    }
    @Override
    public int getItemCount(){
        return mGoodsList == null ? 0 : mGoodsList.size();
    }
}
