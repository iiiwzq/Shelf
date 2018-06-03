package com.example.recyclerviewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recyclerviewtest.Activity.ShowGoodsActivity;
import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.myclass.ShoppingCartBean;
import com.example.recyclerviewtest.util.SharedPreferencesUtils;


import java.util.List;

/**
 * Created by lenovo on 2018/5/30.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private boolean isShow = true;//是否显示编辑/完成

    private Context mContext;

    private List<ShoppingCartBean> mShoppingList;

    private ModifyCountInterface modifyCountInterface;

    private CheckInterface checkInterface;

    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }

    public
    static class ViewHolder extends RecyclerView.ViewHolder {
        //        View GoodsView;
        TextView tvCommodityName;
        TextView tvCommodityClass;
        TextView tvCommodityPrice;
        TextView tvCommodityNum;
        ImageView tvCommodityImage;

        TextView sub;
        TextView add;

        CheckBox ckOneChose;


        public ViewHolder(View view) {
            super(view);
            tvCommodityName = (TextView) view.findViewById(R.id.tv_commodity_name);
            tvCommodityClass = (TextView) view.findViewById(R.id.tv_commodity_attr);
            tvCommodityPrice = (TextView) view.findViewById(R.id.tv_commodity_price);
            tvCommodityNum = (TextView) view.findViewById(R.id.tv_commodity_show_num);
            tvCommodityImage = (ImageView) view.findViewById(R.id.iv_show_pic);

            sub = (TextView) view.findViewById(R.id.iv_sub);
            add = (TextView) view.findViewById(R.id.iv_add);

            ckOneChose = (CheckBox) view.findViewById(R.id.ck_choose);
        }
    }


    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public ShoppingCartAdapter(List<ShoppingCartBean> ShoppingList) {
        mShoppingList = ShoppingList;
        notifyDataSetChanged();
    }

    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_shopping_cart, parent, false);
        final ShoppingCartAdapter.ViewHolder holder = new ShoppingCartAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ShoppingCartAdapter.ViewHolder holder, final int position) {
        final ShoppingCartBean Shopping = mShoppingList.get(position);
        holder.tvCommodityName.setText(Shopping.getGoodsName());
        holder.tvCommodityClass.setText(Shopping.getClassification());
        holder.tvCommodityPrice.setText(Shopping.getPrice()+"");
        holder.tvCommodityNum.setText(Shopping.getCount()+"");
        Glide.with(mContext).load(Shopping.getImage()).into(holder.tvCommodityImage);
        holder.ckOneChose.setChecked(Shopping.isChoosed());

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, holder.tvCommodityNum);
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, holder.tvCommodityNum);
            }
        });
        //单选框按钮
        holder.ckOneChose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Shopping.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );


    }

    @Override
    public int getItemCount() {
        return mShoppingList == null ? 0 : mShoppingList.size();
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        void doIncrease(int position, TextView showCountView);

        /**
         * 删减操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        void doDecrease(int position, TextView showCountView);

    }
}
