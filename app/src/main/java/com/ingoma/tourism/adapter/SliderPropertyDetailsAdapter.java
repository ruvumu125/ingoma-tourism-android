package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.PropertyImage;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;
import com.ingoma.tourism.R;

public class SliderPropertyDetailsAdapter extends SliderViewAdapter<SliderPropertyDetailsAdapter.SliderViewHolder> {


    private final List<PropertyImage> imageUrls;
    private Context context;

    public SliderPropertyDetailsAdapter(Context context,List<PropertyImage> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {

        String baseUrl = Constant.BASE_URL + "api/v1/property-image/";
        String fullImageUrl = baseUrl+ imageUrls.get(position).getImageUrl();

        Glide.with(context)
                .load(fullImageUrl)
                .placeholder(R.drawable.hotel_place_holder)
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    public static class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

