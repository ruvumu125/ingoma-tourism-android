package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ingoma.tourism.model.Room;
import com.ingoma.tourism.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText(room.getName());
        holder.tvRoomCapacity.setText("Capacity: " + room.getCapacity() + " people");
        holder.tvRoomBedType.setText("Bed: " + room.getBedType());
        holder.tvRoomSurface.setText("Surface: " + room.getSurface() + "m²");

        Glide.with(context).load(room.getImageUrl()).into(holder.ivRoomImage);

        // Set up nested RecyclerView for plans
        holder.rvPlans.setLayoutManager(new LinearLayoutManager(context));
        holder.rvPlans.setAdapter(new PlanAdapter(room.getPlans()));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvNumberPictures, tvRoomCapacity, tvRoomBedType, tvRoomSurface;
        AppCompatImageView ivRoomImage;
        RecyclerView rvPlans;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tv_room_name);
            tvNumberPictures = itemView.findViewById(R.id.picture_numbers);
            tvRoomCapacity = itemView.findViewById(R.id.tv_guest_count);
            tvRoomBedType = itemView.findViewById(R.id.tv_bed_type);
            tvRoomSurface = itemView.findViewById(R.id.tv_room_surface);
            ivRoomImage = itemView.findViewById(R.id.imageView);
            rvPlans = itemView.findViewById(R.id.recyclerPolicies);
        }
    }
}

