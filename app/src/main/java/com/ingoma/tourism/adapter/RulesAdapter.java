package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.ingoma.tourism.R;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.RulesViewHolder> {

    private List<String> rulesList;

    public RulesAdapter(List<String> rulesList) {
        this.rulesList = rulesList;
    }

    @NonNull
    @Override
    public RulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_rule, parent, false);
        return new RulesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RulesViewHolder holder, int position) {
        holder.tvRule.setText(rulesList.get(position));
    }

    @Override
    public int getItemCount() {
        return rulesList.size();
    }

    public static class RulesViewHolder extends RecyclerView.ViewHolder {
        TextView tvRule;

        public RulesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRule = itemView.findViewById(R.id.tvRule);
        }
    }
}

