package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.Sort;

import java.util.List;
import com.ingoma.tourism.R;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder>{

    private List<Sort> sorts;
    private OnSortClickListener listener;
    private int selectedPosition = -1;

    public interface OnSortClickListener {
        void onSortClick(Sort sort, int position);
    }

    public SortAdapter(List<Sort> sortList, OnSortClickListener listener) {
        this.sorts = sortList;
        this.listener = listener;

        // Set previously selected item
        for (int i = 0; i < sortList.size(); i++) {
            if (sortList.get(i).isSelected()) {
                selectedPosition = i;
                break;
            }
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_sort, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sort sort = sorts.get(position);
        holder.tvRadioButton.setText(sort.getName());
        holder.radioButton.setChecked(position == selectedPosition);

        holder.radioButton.setOnClickListener(v -> {

            selectedPosition = position;
            listener.onSortClick(sort, position);
            notifyDataSetChanged();
        });
        holder.sortItem.setOnClickListener(v -> {

            selectedPosition = position;
            listener.onSortClick(sort, position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return sorts.size();
    }

    public String getSelectedAmenity() {
        if (selectedPosition != -1) {
            return sorts.get(selectedPosition).getName();
        }
        return "No selection";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatRadioButton radioButton;
        AppCompatTextView tvRadioButton;
        ConstraintLayout sortItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.sortRadio);
            tvRadioButton = itemView.findViewById(R.id.sortLabel);
            sortItem = itemView.findViewById(R.id.sortItem);
        }
    }
}
