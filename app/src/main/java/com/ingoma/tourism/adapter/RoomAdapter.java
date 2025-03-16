package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.Plan;
import com.ingoma.tourism.model.RoomHotel;
import com.ingoma.tourism.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<RoomHotel> roomList;

    private RoomHotel selectedRoom = null;
    private Plan selectedPlan = null;
    private OnPlanSelectedListener planSelectedListener;

    // Interface to notify Activity
    public interface OnPlanSelectedListener {
        void onPlanSelected(Plan plan, RoomHotel room);
    }

    public RoomAdapter(Context context, List<RoomHotel> roomList, OnPlanSelectedListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.planSelectedListener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_hotel, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {

        RoomHotel room = roomList.get(position);

        //guest
        String guest="";
        if (Integer.valueOf(room.getMaxGuests())>1){
            guest=room.getMaxGuests()+" visiteurs";
        }
        else{
            guest=room.getMaxGuests()+" visiteur";
        }
        holder.tvRoomName.setText(room.getTypeName());
        holder.tvRoomCapacity.setText(guest);
        holder.tvRoomBedType.setText(room.getBedType());
        holder.tvRoomSurface.setText(room.getRoomSize() + " m²");

        // Count the number of images
        int imageCount = room.getImages() != null ? room.getImages().size() : 0;
        holder.tvNumberPictures.setText(String.valueOf(imageCount));

        String baseUrl = Constant.BASE_URL + "api/v1/room-image/";
        String fullImageUrl = baseUrl+ room.getImages().get(position).getImageUrl();

        Glide.with(context)
                .load(fullImageUrl) // Load first image
                .placeholder(R.drawable.hotel_place_holder) // Add a placeholder image
                .into(holder.ivRoomImage);

        // Setup PlanAdapter for room plans
        holder.rvPlans.setLayoutManager(new LinearLayoutManager(context));
        holder.rvPlans.setAdapter(new PlanAdapter(room.getPlans(),room,this));




    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    // Method to set selected plan globally
    public void setSelectedPlan(RoomHotel room, Plan plan) {
        if (selectedRoom != null && selectedPlan != null) {
            selectedRoom.setSelectedPlan(null);
        }

        selectedRoom = room;
        selectedPlan = plan;
        room.setSelectedPlan(plan);

        // Notify the activity about the selected plan
        if (planSelectedListener != null) {
            planSelectedListener.onPlanSelected(plan,room);
        }

        notifyDataSetChanged();
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

