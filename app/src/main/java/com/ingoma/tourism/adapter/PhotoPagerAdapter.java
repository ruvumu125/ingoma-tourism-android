package com.ingoma.tourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ingoma.tourism.R;
import com.ingoma.tourism.constant.Constant;
import java.util.List;

public class PhotoPagerAdapter extends RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder> {
    private List<String> photoUrls;
    private String type;

    public PhotoPagerAdapter(List<String> photoUrls,String type) {
        this.photoUrls = photoUrls;
        this.type = type;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pager_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {

        if (type.equals("property_picture")){

            String baseUrl = Constant.BASE_URL + "api/v1/property-image/";
            String fullImageUrl=baseUrl + photoUrls.get(position);

            // Load image with Glide
            Glide.with(holder.itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.hotel_place_holder)
                    .into(holder.imageView);
        }
        else{

            String baseUrl = Constant.BASE_URL + "api/v1/room-image/";
            String fullImageUrl=baseUrl + photoUrls.get(position);

            // Load image with Glide
            Glide.with(holder.itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.hotel_place_holder)
                    .into(holder.imageView);

        }

    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
