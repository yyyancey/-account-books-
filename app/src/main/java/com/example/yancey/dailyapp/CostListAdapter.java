package com.example.yancey.dailyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yancey on 2017/8/24.
 */

public class CostListAdapter extends BaseAdapter{
    private List<CostBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public CostListAdapter(Context context,List<CostBean> list){
        mList=list;
        mContext=context;
        mLayoutInflater=LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder viewHolder;
        if(view==null){
            viewHolder=new viewHolder();
            view=mLayoutInflater.inflate(R.layout.list_item,null);
            viewHolder.mcostTitle=view.findViewById(R.id.tv_title);
            viewHolder.mcostDate=view.findViewById(R.id.tv_date);
            viewHolder.mcostMoney=view.findViewById(R.id.tv_cost);
            view.setTag(viewHolder);
        }else {
            viewHolder=(viewHolder)view.getTag();
        }
        CostBean costBean=mList.get(i);
        viewHolder.mcostTitle.setText(costBean.costTitle);
        viewHolder.mcostMoney.setText(costBean.costMoney);
        viewHolder.mcostDate.setText(costBean.costDate);
        return view;
    }
    private static class viewHolder{
        public TextView mcostTitle;
        public TextView  mcostDate;
        public TextView mcostMoney;
    }
}
