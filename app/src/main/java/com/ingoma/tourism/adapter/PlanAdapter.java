package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.Plan;

import java.util.List;
import com.ingoma.tourism.R;
import com.ingoma.tourism.model.RoomHotel;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<Plan> planList;
    private RoomHotel room;
    private RoomAdapter roomAdapter; // Reference to RoomAdapter


    public PlanAdapter(List<Plan> planList, RoomHotel room, RoomAdapter roomAdapter) {
        this.planList = planList;
        this.room = room;
        this.roomAdapter = roomAdapter;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.tvPlanName.setText(plan.getPlanType());
        holder.tvPlanDetails.setText(plan.getPlanType());
        holder.tvPlanPrice.setText(plan.getPrice());
        holder.tvCurrency.setText(plan.getCurrency());

        // Set checkbox state based on RoomAdapter's selected plan
        holder.cb_room.setChecked(plan == room.getSelectedPlan());

        // Handle checkbox selection
        holder.lytRoomParentInfo.setOnClickListener(v -> {
            roomAdapter.setSelectedPlan(room, plan); // Update global selection
        });

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvPlanPrice,tvCurrency;
        private TextView tvPlanName,tvPlanDetails;
        private CheckBox cb_room;
        private LinearLayout lytRoomParentInfo;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvParentRoomHeading);
            tvPlanDetails = itemView.findViewById(R.id.mlv_goods_detail_description);
            tvPlanPrice = itemView.findViewById(R.id.tv_price);
            tvCurrency = itemView.findViewById(R.id.tv_bdt_price);
            cb_room = itemView.findViewById(R.id.cb_room);
            lytRoomParentInfo = itemView.findViewById(R.id.lytRoomParentInfo);
        }
    }
}
