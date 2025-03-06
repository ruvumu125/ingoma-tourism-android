package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.ingoma.tourism.R;
import com.ingoma.tourism.model.LocationSearch;

public class LocationSearchAdapter extends RecyclerView.Adapter<LocationSearchAdapter.ViewHolder> {

    private List<LocationSearch> searchResults = new ArrayList<>();
    private OnCityClickListener listener;

    public interface OnCityClickListener {
        void onCityClick(LocationSearch city);
    }

    public void setSearchResults(List<LocationSearch> results, OnCityClickListener listener) {
        this.searchResults = results;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationSearch result = searchResults.get(position);
        holder.cityName.setText(result.getName());

        if (result.getType().equals("city")) {

            String nbProperty="";
            if (!result.isHotel()){

                if (result.getPropertyCount()>1){
                    nbProperty=result.getPropertyCount()+" maisons de passage";
                }
                else {
                    nbProperty=result.getPropertyCount()+" maison de passage";
                }
            }
            else{

                if (result.getPropertyCount()>1){
                    nbProperty=result.getPropertyCount()+" hôtels";
                }
                else {
                    nbProperty=result.getPropertyCount()+" hôtel";
                }

            }

            holder.propertiesCount.setText(nbProperty);
            holder.cityImage.setImageResource(R.drawable.ic_htl_has_location);
        } else {
            holder.propertiesCount.setText(result.getAddress());
            holder.cityImage.setImageResource(R.drawable.recent_search_hotel);
        }
        holder.itemView.setOnClickListener(v -> listener.onCityClick(result));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName, propertiesCount;
        ImageView cityImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.tv_city_name);
            propertiesCount = itemView.findViewById(R.id.tv_properties_count);
            cityImage = itemView.findViewById(R.id.img_city);
        }
    }

}
