package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.RoomGuestHouse;
import com.ingoma.tourism.R;
import java.util.List;

public class RoomGuestHouseAdapter extends RecyclerView.Adapter<RoomGuestHouseAdapter.RoomTypeViewHolder>{

    private List<RoomGuestHouse> roomGuestHouses;
    private Context context;

    public RoomGuestHouseAdapter(Context context,List<RoomGuestHouse> roomGuestHouses) {
        this.roomGuestHouses = roomGuestHouses;
        this.context = context;
    }

    @Override
    public RoomTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_guesthouse, parent, false);
        return new RoomTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RoomTypeViewHolder holder, int position) {
        RoomGuestHouse room = roomGuestHouses.get(position);

        //guest
        String guest="";
        if (Integer.valueOf(room.getMax_guests())>1){
            guest=room.getMax_guests()+" personnes";
        }
        else{
            guest=room.getMax_guests()+" personne";
        }

        if (Integer.valueOf(room.getMax_guests())==null){
            holder.ll_guest_count.setVisibility(View.GONE);
        }

        if (room.getBed_type() == null){
            holder.ll_bed_type.setVisibility(View.GONE);
        }
        if (room.getRoom_size() == null){
            holder.ll_room_surface.setVisibility(View.GONE);
        }
        holder.tvRoomName.setText(room.getType_name());
        holder.tvRoomCapacity.setText(guest);
        holder.tvRoomBedType.setText(room.getBed_type());
        holder.tvRoomSurface.setText(room.getRoom_size() + " m²");

        // Count the number of images
        int imageCount = room.getImages() != null ? room.getImages().size() : 0;
        holder.tvNumberPictures.setText(String.valueOf(imageCount));

        String baseUrl = Constant.BASE_URL + "api/v1/room-image/";
        String fullImageUrl = baseUrl+ room.getImages().get(0).getImage_url();

        Glide.with(context)
                .load(fullImageUrl) // Load first image
                .placeholder(R.drawable.hotel_place_holder) // Add a placeholder image
                .into(holder.ivRoomImage);
    }

    @Override
    public int getItemCount() {
        return roomGuestHouses.size();
    }

    public static class RoomTypeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvNumberPictures, tvRoomCapacity, tvRoomBedType, tvRoomSurface;
        AppCompatImageView ivRoomImage;
        LinearLayout ll_guest_count,ll_bed_type,ll_room_surface;

        public RoomTypeViewHolder(View view) {
            super(view);
            tvRoomName = itemView.findViewById(R.id.tv_room_name_guesthouse);
            tvNumberPictures = itemView.findViewById(R.id.picture_numbers_guesthouse);
            tvRoomCapacity = itemView.findViewById(R.id.tv_guest_count_guesthouse);
            tvRoomBedType = itemView.findViewById(R.id.tv_bed_type_guesthouse);
            tvRoomSurface = itemView.findViewById(R.id.tv_room_surface_guesthouse);
            ivRoomImage = itemView.findViewById(R.id.imageView_guesthouse);

            ll_guest_count = itemView.findViewById(R.id.ll_guest_count);
            ll_bed_type = itemView.findViewById(R.id.ll_bed_type);
            ll_room_surface = itemView.findViewById(R.id.ll_room_surface);

        }
    }
}
