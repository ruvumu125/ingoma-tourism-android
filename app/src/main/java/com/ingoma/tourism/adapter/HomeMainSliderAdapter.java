package com.ingoma.tourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.Advertisement;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;
import com.ingoma.tourism.R;

public class HomeMainSliderAdapter extends SliderViewAdapter<HomeMainSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<Advertisement> mSliderItems = new ArrayList<>();

    public HomeMainSliderAdapter(Context context) {
        this.context = context;
    }
    public void renewItems(List<Advertisement> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }
    public void addItem(Advertisement sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertisement, null);
        return new SliderAdapterVH(inflate);
    }
    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        final Advertisement sliderItem = mSliderItems.get(position);

        String baseUrl = Constant.BASE_URL + "api/v1/advertisement-image/";
        String fullImageUrl = baseUrl+ sliderItem.getFileData();

        Glide.with(context)
                .load(fullImageUrl)
                .placeholder(R.drawable.hotel_place_holder)
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String websiteUrl = sliderItem.getAdvertisementUrl();

                // Check if the URL is not null or empty
                if (websiteUrl != null && !websiteUrl.isEmpty()) {
                    // Create an Intent to open the URL in the browser
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                    context.startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        // View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.ivAdvertisement);
            //this.itemView = itemView;
        }
    }

}
