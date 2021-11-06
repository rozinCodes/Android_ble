package com.waltonbd.smartscale.models;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class IndexItem {

    private String name;
    private int image;

    public IndexItem(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return "IndexItem{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexItem indexItem = (IndexItem) o;
        return getImage() == indexItem.getImage() &&
                getName().equals(indexItem.getName());
    }

    public static DiffUtil.ItemCallback<IndexItem> itemCallback = new DiffUtil.ItemCallback<IndexItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull IndexItem oldItem, @NonNull IndexItem newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull IndexItem oldItem, @NonNull IndexItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    // https://stackoverflow.com/a/35809319/5280371
    // https://stackoverflow.com/a/37675475/5280371
    @BindingAdapter({"android:src"})
    public static void setImageResource(ImageView imageView, int resId){
        imageView.setImageResource(resId);
    }
}
