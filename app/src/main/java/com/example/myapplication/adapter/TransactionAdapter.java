package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.Transaction;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    public List<Transaction> transactionList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public void updateList(List<Transaction> newList) {
        this.transactionList = newList;
        notifyDataSetChanged();
    }

    // 根据分类获取对应的图标
    private int getCategoryIcon(String category) {
        if (category == null) return R.drawable.ic_launcher_foreground;
        
        switch (category) {
            case "餐饮":
                return android.R.drawable.ic_menu_myplaces;
            case "交通":
                return android.R.drawable.ic_menu_directions;
            case "购物":
                return android.R.drawable.ic_menu_agenda;
            case "娱乐":
                return android.R.drawable.ic_menu_slideshow;
            default:
                return android.R.drawable.ic_menu_help;
        }
    }

    // ViewHolder 内部类
    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView tvTitle;
        private TextView tvAmount;
        private TextView tvDate;
        private TextView tvType;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvType = itemView.findViewById(R.id.tv_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(position);
                        return true;
                    }
                    return false;
                }
            });
        }

        public void bind(Transaction transaction) {
            // 设置图标
            ivIcon.setImageResource(getCategoryIcon(transaction.getCategory()));
            
            // 设置标题（使用分类作为标题）
            tvTitle.setText(transaction.getCategory());
            
            // 设置金额
            tvAmount.setText(String.format("-¥%.2f", transaction.getAmount()));
            tvAmount.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            
            // 设置日期
            tvDate.setText(transaction.getDate());
            
            // 设置收支类型
            tvType.setText("支出");
        }
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}