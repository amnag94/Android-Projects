package com.example.ameyadeepaknagnur.bitcurrency;

// Can be used to create objects to store maintain transactions with ease
public class Transaction {

    String date, tid;
    double price, amount;
    int type;

    public Transaction() {
        date = "";
        tid = "";
        price = 0.0;
        amount = 0.0;
        type = -1;
    }

}
