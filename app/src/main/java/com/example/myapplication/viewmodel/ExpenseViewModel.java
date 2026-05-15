package com.example.myapplication.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.myapplication.model.Transaction;
import com.example.myapplication.repository.TransactionRepository;
import java.util.List;

public class ExpenseViewModel extends ViewModel {
    private TransactionRepository repository;

    public ExpenseViewModel() {
        repository = new TransactionRepository();
    }

    // 获取消费记录列表
    public List<Transaction> getTransactionList() {
        return repository.getAllTransactions();
    }

    // 计算总消费金额
    public double getTotalAmount() {
        return repository.getTotalExpense();
    }

    // 添加消费记录
    public void addTransaction(Transaction transaction) {
        repository.addTransaction(transaction);
    }

    // 删除消费记录
    public void deleteTransaction(int position) {
        repository.deleteTransaction(position);
    }
}