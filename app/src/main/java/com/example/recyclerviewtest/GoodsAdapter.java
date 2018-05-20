package com.example.recyclerviewtest;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 2018/5/19.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    
    private List<Goods> mGoodsList;

static class ViewHolder extends RecyclerView.ViewHolder{
    //        View GoodsView;
    TextView GoodsName;
    TextView GoodsClassification;
    TextView GoodsPrice;

    public ViewHolder(View view){
        super(view);
//            GoodsView=view;
        GoodsName = (TextView) view.findViewById(R.id.goodsName);
        GoodsClassification = (TextView) view.findViewById(R.id.classification);
        GoodsPrice = (TextView) view.findViewById(R.id.price);

    }
}
    public GoodsAdapter(List<Goods> GoodsList){
        mGoodsList=GoodsList;
    }

    @Override
    public GoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_item,parent,false);
        final GoodsAdapter.ViewHolder holder = new GoodsAdapter.ViewHolder(view);

//        holder.GoodsView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                int position = holder.getAdapterPosition();
//                Goods Goods = mGoodsList.get(position);
//                Toast.makeText(v.getContext(),"you clicked view "+Goods.getName(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.GoodsImage.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                int position = holder.getAdapterPosition();
//                Goods Goods = mGoodsList.get(position);
//                Toast.makeText(v.getContext(),"you clicked image "+Goods.getName(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(GoodsAdapter.ViewHolder holder, int position){
        Goods Goods = mGoodsList.get(position);
        holder.GoodsName.setText(Goods.getGoodsName());
        holder.GoodsClassification.setText(Goods.getClassification());
        holder.GoodsPrice.setText(Goods.getPrice());
    }
    @Override
    public int getItemCount(){
        return mGoodsList.size();
    }
}
