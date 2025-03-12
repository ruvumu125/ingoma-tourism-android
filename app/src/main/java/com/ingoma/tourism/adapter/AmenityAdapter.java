package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.Amenity;

import java.util.ArrayList;
import java.util.List;
import com.ingoma.tourism.R;

public class AmenityAdapter extends RecyclerView.Adapter<AmenityAdapter.ViewHolder> {
    private Context context;
    private List<Amenity> amenityList;
    private List<Amenity> selectedAmenities;

    public AmenityAdapter(Context context, List<Amenity> amenityList) {
        this.context = context;
        this.amenityList = amenityList;
        this.selectedAmenities = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_amenity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Amenity amenity = amenityList.get(position);
        holder.amenityName.setText(amenity.getName());

        // Handle checkbox selection
        holder.checkBoxAmenity.setOnCheckedChangeListener(null); // Reset listener to prevent unintended triggers
        holder.checkBoxAmenity.setChecked(selectedAmenities.contains(amenity));

        holder.checkBoxAmenity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedAmenities.contains(amenity)) {
                    selectedAmenities.add(amenity);
                }
            } else {
                selectedAmenities.remove(amenity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return amenityList.size();
    }

    // Function to clear data
    public void clearData() {
        amenityList.clear();
        notifyDataSetChanged();
    }

    public List<Amenity> getSelectedAmenities() {
        return selectedAmenities;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxAmenity;
        TextView amenityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxAmenity = itemView.findViewById(R.id.checkBoxAmenity);
            amenityName = itemView.findViewById(R.id.tvAmenityName);
        }
    }
}
