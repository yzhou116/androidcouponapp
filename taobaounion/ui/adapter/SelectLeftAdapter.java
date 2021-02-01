package com.cvte.taobaounion.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cvte.taobaounion.R;
import com.cvte.taobaounion.model.domain.SelectPageCategory;

import java.util.ArrayList;
import java.util.List;

public class SelectLeftAdapter extends RecyclerView.Adapter<SelectLeftAdapter.InnerHolder> {
    List<SelectPageCategory.DataBean> mData = new ArrayList<>();
    private int mCurrentPosition = 0;
    private onLeftItemclickListener mLeftItemClickListener = null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView left_title = holder.itemView.findViewById(R.id.left_tv);
        SelectPageCategory.DataBean dataBean = mData.get(position);
        if (mCurrentPosition == position) {
            left_title.setBackgroundColor(Color.parseColor("#EFEEEE"));
        } else {
            left_title.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        left_title.setText(dataBean.getFavorites_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeftItemClickListener != null) {
                    if (mCurrentPosition != position) {
                        mCurrentPosition = position;
                        mLeftItemClickListener.onItemClick(dataBean);
                        notifyDataSetChanged();
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectPageCategory selectPageCategory) {
        List<SelectPageCategory.DataBean> data = selectPageCategory.getData();
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size() > 0) {
            mLeftItemClickListener.onItemClick(mData.get(mCurrentPosition));
        }
    }


    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setonLeftItemClickListener(onLeftItemclickListener listener) {

        this.mLeftItemClickListener = listener;
    }


    public interface onLeftItemclickListener {
        void onItemClick(SelectPageCategory.DataBean item);
    }
}
