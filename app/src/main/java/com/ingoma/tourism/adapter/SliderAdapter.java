package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.HotelModel;
import com.smarteist.autoimageslider.SliderViewAdapter;
import java.util.List;
import com.ingoma.tourism.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {

    private Context context;
    private List<String> imageUrls;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick();
    }

    public SliderAdapter(Context context, List<String> imageUrls, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder holder, int position) {

        Glide.with(context)
                .load(imageUrls.get(position))
                .placeholder(R.drawable.hotel_place_holder)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> onItemClickListener.onItemClick());
    }

    @Override
    public int getCount() {
        return imageUrls.size(); // Number of images in the slider
    }

    static class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImageView);
        }
    }
}
