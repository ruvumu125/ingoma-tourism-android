package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.ingoma.tourism.R;
import com.ingoma.tourism.model.Rule;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.RulesViewHolder> {

    private List<Rule> rulesList;

    public RulesAdapter(List<Rule> rulesList) {
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

        Rule rule = rulesList.get(position);
        holder.tvRule.setText(rule.getRuleDescription());
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

