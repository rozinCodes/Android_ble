package com.waltonbd.smartscale.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.IndexRowBinding;
import com.waltonbd.smartscale.models.IndexItem;

public class IndexRlyAdapter extends ListAdapter<IndexItem, IndexRlyAdapter.IndexRlyAdapterViewHolder> {

    private int selectedPosition = 0;
    Context context;
    OnItemClickListener listener;

    public IndexRlyAdapter(OnItemClickListener listener, Context context) {
        super(IndexItem.itemCallback);
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public IndexRlyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IndexRowBinding indexRowBinding = IndexRowBinding.inflate(layoutInflater, parent, false);
        //indexRowBinding.setOnItemClickListener(listener);
        return new IndexRlyAdapterViewHolder(indexRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IndexRlyAdapterViewHolder holder, int position) {
        IndexItem indexItem = getItem(position);

        holder.itemView.setOnClickListener(view -> {
            selectedPosition = position;
            listener.onItemClick(indexItem);
            notifyDataSetChanged();
        });

        // Highlight Item OnClick
        if (selectedPosition == position) {
            holder.indexRowBinding.topImage.setVisibility(View.VISIBLE);
            holder.indexRowBinding.indexImage.setBackgroundResource(R.drawable.index_selected);
            holder.indexRowBinding.indexImage.setColorFilter(ContextCompat.getColor(context, R.color.white));
            holder.indexRowBinding.indexNameTv.setVisibility(View.VISIBLE);
        } else {
            holder.indexRowBinding.topImage.setVisibility(View.INVISIBLE);
            holder.indexRowBinding.indexImage.setBackgroundResource(R.drawable.index_normal);
            holder.indexRowBinding.indexImage.setColorFilter(ContextCompat.getColor(context, R.color.md_grey_500));
            holder.indexRowBinding.indexNameTv.setVisibility(View.GONE);
        }

        holder.indexRowBinding.setIndexData(indexItem);
        holder.indexRowBinding.executePendingBindings();
    }

    static class IndexRlyAdapterViewHolder extends RecyclerView.ViewHolder {

        private final IndexRowBinding indexRowBinding;

        public IndexRlyAdapterViewHolder(@NonNull IndexRowBinding binding) {
            super(binding.getRoot());
            this.indexRowBinding = binding;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(IndexItem IndexItem);
    }
}
