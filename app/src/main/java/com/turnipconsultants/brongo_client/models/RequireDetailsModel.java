package com.turnipconsultants.brongo_client.models;

/**
 * Created by mohit on 30-12-2017.
 */

public class RequireDetailsModel {
    private String title;
    private String value;
    private boolean isTagFlow;

    public RequireDetailsModel(String title, String value, boolean isTagFlow) {
        this.title = title;
        this.value = value;
        this.isTagFlow = isTagFlow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isTagFlow() {
        return isTagFlow;
    }

    public void setTagFlow(boolean tagFlow) {
        isTagFlow = tagFlow;
    }
}
