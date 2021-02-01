package com.cvte.taobaounion.ui.adapter;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cvte.taobaounion.R;
import com.cvte.taobaounion.model.domain.OnSaleContent;

import java.util.ArrayList;
import java.util.List;


public class OnSalePageAdapter extends RecyclerView.Adapter<OnSalePageAdapter.InnerHolder> {

    private List<OnSaleContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sale, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public void setData(OnSaleContent content) {
        this.mData.clear();
        this.mData.addAll(content.getData().
                getTbk_dg_optimus_material_response().
                getResult_list().
                getMap_data());
        notifyDataSetChanged();

    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
