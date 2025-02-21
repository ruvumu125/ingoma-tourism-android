package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.Plan;

import java.util.List;
import com.ingoma.tourism.R;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<Plan> planList;

    public PlanAdapter(List<Plan> planList) {
        this.planList = planList;
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
        holder.tvPlanName.setText(plan.getName());
        holder.tvPlanDetails.setText(plan.getDetails());
        holder.tvPlanPrice.setText("$" + plan.getPrice());
        if (position==0){
            holder.cb_room.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanName,tvPlanDetails, tvPlanPrice;
        CheckBox cb_room;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvParentRoomHeading);
            tvPlanDetails = itemView.findViewById(R.id.mlv_goods_detail_description);
            tvPlanPrice = itemView.findViewById(R.id.tv_price);
            cb_room = itemView.findViewById(R.id.cb_room);
        }
    }
}
