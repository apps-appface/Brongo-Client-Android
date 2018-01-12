package com.turnipconsultants.brongo_client.models;

import java.util.ArrayList;

/**
 * Created by Pankaj on 15-12-2017.
 */

public class BudgetRangeModel {
    ArrayList<Double> doubleArrayList = new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();

    public BudgetRangeModel(ArrayList<Double> doubleArrayList, ArrayList<String> stringArrayList) {
        this.doubleArrayList = doubleArrayList;
        this.stringArrayList = stringArrayList;
    }

    public ArrayList<Double> getDoubleArrayList() {
        return doubleArrayList;
    }

    public ArrayList<String> getStringArrayList() {
        return stringArrayList;
    }
}
