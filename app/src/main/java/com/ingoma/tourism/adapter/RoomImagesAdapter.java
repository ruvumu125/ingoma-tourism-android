package com.ingoma.tourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ingoma.tourism.PagerGalleryActivity;
import com.ingoma.tourism.R;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.PropertyImage;
import com.ingoma.tourism.model.RoomImage;

import java.util.ArrayList;
import java.util.List;

public class RoomImagesAdapter extends RecyclerView.Adapter<RoomImagesAdapter.PhotosViewHolder>{

    private List<RoomImage> photos;
    private Context context;
    private String room_name;

    public RoomImagesAdapter(Context context,List<RoomImage> photos,String room_name) {
        this.photos = photos;
        this.context=context;
        this.room_name=room_name;
    }

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_pictures, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosViewHolder holder, int position) {
        RoomImage image = photos.get(position);

        String baseUrl = Constant.BASE_URL + "api/v1/room-image/";
        String fullImageUrl=baseUrl + image.getImageUrl();


        // Load image with Glide
        Glide.with(holder.itemView.getContext())
                .load(fullImageUrl)
                .placeholder(R.drawable.hotel_place_holder)
                .into(holder.gi_photo_gallery);

        // Pass all image URLs to the pager
        ArrayList<String> imageUrls = new ArrayList<>();
        for (RoomImage img : photos) {
            imageUrls.add(img.getImageUrl());
        }


        holder.gi_photo_gallery.setOnClickListener(view -> {

            Intent intent = new Intent(context, PagerGalleryActivity.class);
            intent.putStringArrayListExtra("imageUrls",new ArrayList<>(imageUrls) );
            intent.putExtra("property_or_room_name",room_name);
            intent.putExtra("picture_type","room_picture");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotosViewHolder extends RecyclerView.ViewHolder {
        ImageView gi_photo_gallery;

        public PhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            gi_photo_gallery = itemView.findViewById(R.id.gi_photo_gallery);
        }
    }
}
