package com.example.stockinginventory;

public class HistoryNote {
    private String id;
    private String name;
    private String number;
    private String reason;
    private int amount;
    private String date;
    private String item;

    public HistoryNote(){
        //No args here
    }

    public HistoryNote(String id,String name, String number, String reason, int amount, String date, String item){
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.name = name;
        this.number = number;
        this.reason = reason;
        this.item = item;
    }

    public String  getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
