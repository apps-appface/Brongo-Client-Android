package com.turnipconsultants.brongo_client;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by Pankaj on 01-12-2017.
 */

public class ProvinceBean implements IPickerViewData {
    private long id;
    private String name;
    private String description;
    private Double amount;

    public ProvinceBean(long id, String name, String description, Double amount){
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
