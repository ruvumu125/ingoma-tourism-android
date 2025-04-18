package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ingoma.tourism.R;
import com.ingoma.tourism.model.RoomAmenity;

import java.util.List;

public class RoomAmenitiesAdapter extends RecyclerView.Adapter<RoomAmenitiesAdapter.AmenitiesViewHolder>{

    private List<RoomAmenity> amenities;

    public RoomAmenitiesAdapter(List<RoomAmenity> amenities) {
        this.amenities = amenities;
    }

    @NonNull
    @Override
    public AmenitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_amenity, parent, false);
        return new AmenitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmenitiesViewHolder holder, int position) {
        RoomAmenity amenity = amenities.get(position);
        holder.amenityText.setText(amenity.getName());
        holder.amenityDescription.setText(amenity.getDescription());
    }

    @Override
    public int getItemCount() {
        return amenities.size();
    }

    static class AmenitiesViewHolder extends RecyclerView.ViewHolder {
        TextView amenityText,amenityDescription;

        public AmenitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            amenityText = itemView.findViewById(R.id.roomAmenityText);
            amenityDescription = itemView.findViewById(R.id.roomAmenityDescription);
        }
    }

}
