package com.example.myapplication.model;

public class Transaction {
    private double amount;
    private String category;
    private String note;
    private String date;

    // 构造方法
    public Transaction(double amount, String category, String note, String date) {
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.date = date;
    }

    // Getter 和 Setter 方法
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}