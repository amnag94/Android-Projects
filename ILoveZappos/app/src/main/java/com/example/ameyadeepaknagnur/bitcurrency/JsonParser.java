package com.example.ameyadeepaknagnur.bitcurrency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public static int transact_no = 0, bid_no = 0, ask_no = 0;
    Transaction [] transactions;
    Bid[] bids;
    Ask[] asks;

    public Transaction[] parse_json_transactions(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        Transaction[] transactions = new Transaction[jsonArray.length()];

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject transact_obj = jsonArray.getJSONObject(index);

            transactions[index] = new Transaction();

            // Add transaction date
            transactions[index].date = transact_obj.getString("date");

            // Add transaction id
            transactions[index].tid = transact_obj.getString("tid");

            // Add transaction amount
            transactions[index].amount = Double.parseDouble(transact_obj.getString("amount"));

            // Add transaction amount
            transactions[index].price = Double.parseDouble(transact_obj.getString("price"));

            // Add transaction amount
            transactions[index].type = Integer.parseInt(transact_obj.getString("type"));

        }

        return transactions;
    }

    public Transaction getTransaction(String json) throws JSONException {
        if (transactions == null) {
            transactions = parse_json_transactions(json);
        }

        if (transact_no < transactions.length) {
            Transaction transaction = transactions[transact_no];
            transact_no++;
            return transaction;
        }
        else {
            return null;
        }
    }

    public Bid[] parse_json_bids(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        JSONArray jsonArray = jsonObject.getJSONArray("bids");

        Bid[] bids = new Bid[jsonArray.length()];

        for (int index = 0; index < jsonArray.length(); index++) {

            JSONArray bid_array = jsonArray.getJSONArray(index);

            bids[index] = new Bid();

            // Add bid value
            bids[index].value = Double.parseDouble(bid_array.getString(0));

            // Add bid amount
            bids[index].amount = Double.parseDouble(bid_array.getString(1));

        }

        return bids;
    }

    public Bid getBid(String json) throws JSONException {
        if (bids == null) {
            bids = parse_json_bids(json);
        }

        if (bid_no < bids.length) {
            Bid bid = bids[bid_no];
            bid_no++;
            return bid;
        }
        else {
            return null;
        }
    }

    public Ask[] parse_json_asks(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);

        JSONArray jsonArray = jsonObject.getJSONArray("asks");

        Ask[] asks = new Ask[jsonArray.length()];

        for (int index = 0; index < jsonArray.length(); index++) {

            JSONArray bid_array = jsonArray.getJSONArray(index);

            asks[index] = new Ask();

            // Add bid value
            asks[index].value = Double.parseDouble(bid_array.getString(0));

            // Add bid amount
            asks[index].amount = Double.parseDouble(bid_array.getString(1));

        }

        return asks;
    }

    public Ask getAsk(String json) throws JSONException {
        if (asks == null) {
            asks = parse_json_asks(json);
        }

        if (ask_no < asks.length) {
            Ask ask = asks[ask_no];
            ask_no++;
            return ask;
        }
        else {
            return null;
        }
    }

    public double getPrice(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);

        double price = Double.parseDouble(jsonObject.getString("last"));

        return price;

    }

}
