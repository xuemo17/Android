package com.example.myapplication.repository;

import com.example.myapplication.model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private List<Transaction> transactionList;

    public TransactionRepository() {
        transactionList = new ArrayList<>();
    }

    // 添加消费记录
    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    // 删除消费记录
    public void deleteTransaction(int position) {
        if (position >= 0 && position < transactionList.size()) {
            transactionList.remove(position);
        }
    }

    // 获取所有消费记录
    public List<Transaction> getAllTransactions() {
        return transactionList;
    }

    // 计算总消费金额
    public double getTotalExpense() {
        double total = 0;
        for (Transaction transaction : transactionList) {
            total += transaction.getAmount();
        }
        return total;
    }
}