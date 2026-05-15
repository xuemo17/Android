package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private List<RecordItem> recordList;

    public RecordAdapter(Context context, List<RecordItem> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 加载item布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        // 获取数据
        RecordItem item = recordList.get(position);
        
        // 绑定数据到视图
        holder.idTextView.setText("#" + item.getId());
        holder.nameTextView.setText(item.getName());
        holder.amountTextView.setText(String.format("-¥%.2f", item.getAmount()));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    // 数据模型类
    public static class RecordItem {
        private int id;
        private String name;
        private double amount;

        public RecordItem(int id, String name, double amount) {
            this.id = id;
            this.name = name;
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getAmount() {
            return amount;
        }
    }

    // ViewHolder类
    class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nameTextView, amountTextView;

        public RecordViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            amountTextView = itemView.findViewById(R.id.amount_text_view);
        }
    }
}