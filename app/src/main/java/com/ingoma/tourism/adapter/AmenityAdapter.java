package com.ingoma.tourism.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.Amenity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ingoma.tourism.R;

public class AmenityAdapter extends RecyclerView.Adapter<AmenityAdapter.ViewHolder> {

    private Context context;
    private List<Amenity> amenityList;

    public AmenityAdapter(Context context, List<Amenity> amenityList) {
        this.context = context;
        this.amenityList = amenityList;
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

        // Set checkbox state based on `selected` field
        holder.checkBoxAmenity.setOnCheckedChangeListener(null); // Prevent unwanted calls
        holder.checkBoxAmenity.setChecked(amenity.isSelected());

        // Handle checkbox selection
        holder.checkBoxAmenity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            amenity.setSelected(isChecked);
        });

        holder.ll_amenity_root.setOnClickListener(v -> {
            boolean newState = !amenity.isSelected();
            amenity.setSelected(newState);
            holder.checkBoxAmenity.setChecked(newState);
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
        List<Amenity> selected = new ArrayList<>();
        for (Amenity amenity : amenityList) {
            if (amenity.isSelected()) {
                selected.add(amenity);
            }
        }
        return selected;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxAmenity;
        TextView amenityName;
        LinearLayout ll_amenity_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxAmenity = itemView.findViewById(R.id.checkBoxAmenity);
            amenityName = itemView.findViewById(R.id.tvAmenityName);
            ll_amenity_root =itemView.findViewById(R.id.ll_amenity_root);
        }
    }


}
