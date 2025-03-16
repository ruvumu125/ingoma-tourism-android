package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.GuestHouseVariant;

import java.util.List;
import com.ingoma.tourism.R;
import com.ingoma.tourism.model.Plan;
import com.ingoma.tourism.model.RoomHotel;

public class GuestHouseVariantAdapter extends RecyclerView.Adapter<GuestHouseVariantAdapter.GuestHouseVariantViewHolder>{

    private List<GuestHouseVariant> guestHouseVariants;
    private int selectedPosition = -1;
    private GuestHouseVariant selectedGuestHouseVariant = null;
    private OnGuestHouseVariantSelectedListener guestHouseVariantSelectedListener;

    public interface OnGuestHouseVariantSelectedListener {
        void onGuestHouseVariantSelected(GuestHouseVariant guestHouseVariant);
    }

    public GuestHouseVariantAdapter(List<GuestHouseVariant> guestHouseVariants,OnGuestHouseVariantSelectedListener listener) {
        this.guestHouseVariants = guestHouseVariants;
        this.guestHouseVariantSelectedListener=listener;
    }

    @Override
    public GuestHouseVariantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guest_house_tarification, parent, false);
        return new GuestHouseVariantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GuestHouseVariantViewHolder holder, int position) {
        GuestHouseVariant variant = guestHouseVariants.get(position);
        holder.tvPlanName.setText(variant.getVariant());
        holder.tvPlanPrice.setText(String.valueOf(variant.getPrice()));
        holder.tvCurrency.setText(variant.getCurrency());

        // Set the radio button state based on selectedPosition
        holder.cb_room.setChecked(position == selectedPosition);

        // Handle checkbox selection
        holder.lytRoomParentInfo.setOnClickListener(v -> {
            guestHouseVariantSelectedListener.onGuestHouseVariantSelected(variant);
            selectedPosition = position; // Update selected position
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return guestHouseVariants.size();
    }

    public static class GuestHouseVariantViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvPlanPrice,tvCurrency;
        private TextView tvPlanName;
        private CheckBox cb_room;
        private LinearLayout lytRoomParentInfo;

        public GuestHouseVariantViewHolder(View view) {
            super(view);
            tvPlanName = itemView.findViewById(R.id.tvParentRoomHeadingGuestHouse);
            tvPlanPrice = itemView.findViewById(R.id.tv_price_guest_house);
            tvCurrency = itemView.findViewById(R.id.tv_bdt_price_guest_house);
            cb_room = itemView.findViewById(R.id.cb_room_guest_house);
            lytRoomParentInfo = itemView.findViewById(R.id.lytRoomParentInfoGuestHouse);
        }
    }
}
