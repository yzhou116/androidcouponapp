package com.cvte.taobaounion.ui.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cvte.taobaounion.R;
import com.cvte.taobaounion.model.domain.SelectPageCategory;
import com.cvte.taobaounion.model.domain.SelectedContent;
import com.cvte.taobaounion.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class SelectContentAdapter extends RecyclerView.Adapter<SelectContentAdapter.InnerHolder> {
    private static final String TAG = "SelectContentAdapter";
    private List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mData = new ArrayList<>();


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_right_content, parent, false);
        Log.d(TAG, "mdata.size" + mData.size());
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void setdata(SelectedContent content) {
        if (content.getCode() == Constant.SUCCESS_CODE) {
            if (content.getData() != null) {
                if (content.getData().getTbk_uatm_favorites_item_get_response() != null) {
                    if (content.getData().getTbk_uatm_favorites_item_get_response().getResults() != null) {
                        if (content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item() != null) {
                            List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mList
                                    = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
                            this.mData.clear();
                            this.mData.addAll(mList);
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        }

    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
