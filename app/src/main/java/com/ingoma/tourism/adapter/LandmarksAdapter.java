package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.Landmark;

import java.util.List;
import com.ingoma.tourism.R;

public class LandmarksAdapter extends RecyclerView.Adapter<LandmarksAdapter.ViewHolder> {
    private List<Landmark> landmarks;

    public LandmarksAdapter(List<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_nearby_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Landmark landmark = landmarks.get(position);
        holder.tvName.setText(landmark.getName());
        holder.tvDistance.setText(landmark.getDistance());
    }

    @Override
    public int getItemCount() {
        return landmarks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDistance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.location);
            tvDistance = itemView.findViewById(R.id.distance);
        }
    }
}
