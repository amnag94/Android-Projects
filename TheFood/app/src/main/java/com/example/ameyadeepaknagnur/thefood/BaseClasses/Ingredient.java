package com.example.ameyadeepaknagnur.thefood.BaseClasses;

public class Ingredient {

    public static String ID = "id";
    public static String MEASURE = "measure";
    public static String QUANTITY = "quantity";

    public int id;
    public String name;
    public String measure;
    public double quantity;

    public Ingredient(int id, String name, String measure, double quantity) {
        this.id = id;
        this.name = name;
        this.measure = measure;
        this.quantity = quantity;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getMeasure() {
        return this.measure;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public boolean setId(int id) {
        this.id = id;

        return true;
    }

    public boolean setName(String name) {
        this.name = name;

        return true;
    }

    public boolean setMeasure(String measure) {
        this.measure = measure;

        return true;
    }

    public boolean setQuantity(double quantity) {
        this.quantity = quantity;

        return true;
    }

}
