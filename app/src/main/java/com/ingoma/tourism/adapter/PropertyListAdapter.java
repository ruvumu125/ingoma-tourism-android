package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.PropertyList;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import com.ingoma.tourism.R;

public class PropertyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<PropertyList> hotelList;
    private boolean isLoading;

    private static final int ITEM_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(PropertyList property);
    }

    public PropertyListAdapter(Context context, List<PropertyList> hotelList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.hotelList = hotelList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == hotelList.size()) ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_list, parent, false);
            return new HotelViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotelViewHolder) {
            PropertyList hotel = hotelList.get(position);
            HotelViewHolder hotelViewHolder = (HotelViewHolder) holder;

            hotelViewHolder.hotelName.setText(hotel.getName());
            hotelViewHolder.hotelType.setText(hotel.getHotelType());
            hotelViewHolder.hotelAddress.setText(hotel.getAddress());
            hotelViewHolder.hotelPrice.setText(String.valueOf(hotel.getMinPrice()));
            hotelViewHolder.hotelCurrency.setText(hotel.getCurrency());
            hotelViewHolder.hotelRating.setText(hotel.getCurrency());

            if (!hotel.getImages().isEmpty()) {

                List<String> fullImageUrls = new ArrayList<>();
                String baseUrl = Constant.BASE_URL + "api/v1/property-image/";

                for (String imageName : hotel.getImages()) {
                    fullImageUrls.add(baseUrl + imageName);
                }

                SliderAdapter sliderAdapter = new SliderAdapter(context, fullImageUrls,() -> {
                    onItemClickListener.onItemClick(hotel);
                });
                hotelViewHolder.imageSlider.setSliderAdapter(sliderAdapter);
                hotelViewHolder.imageSlider.setAutoCycle(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? hotelList.size() + 1 : hotelList.size();
    }

    public void addHotels(List<PropertyList> newHotels) {
        int startPosition = hotelList.size();
        hotelList.addAll(newHotels);
        notifyItemRangeInserted(startPosition, newHotels.size());
    }

    public void showLoading() {
        isLoading = true;
        notifyItemInserted(hotelList.size());
    }

    public void hideLoading() {
        isLoading = false;
        notifyItemRemoved(hotelList.size());
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {

        private SliderView imageSlider;
        private AppCompatTextView hotelType,hotelName,hotelAddress,hotelPrice,hotelCurrency,hotelRating;
        private ConstraintLayout main_card;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSlider = itemView.findViewById(R.id.view_pager);
            hotelName = itemView.findViewById(R.id.tv_hotel_name_yudita);
            hotelType = itemView.findViewById(R.id.tv_hotel_type);
            hotelAddress = itemView.findViewById(R.id.tv_hotel_address);
            hotelPrice = itemView.findViewById(R.id.tv_price);
            hotelCurrency = itemView.findViewById(R.id.tv_bdt_price);
            hotelRating = itemView.findViewById(R.id.rating_bar);
            main_card = itemView.findViewById(R.id.main_layout);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}

