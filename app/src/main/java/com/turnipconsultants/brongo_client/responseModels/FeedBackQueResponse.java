package com.turnipconsultants.brongo_client.responseModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by mohit on 30-01-2018.
 */

@Data
public class FeedBackQueResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("message")
    private String message;

    @Data
    public class DataEntity implements Serializable {
        private String meaning;

        private float rating;

        private List<String> comment;
    }
}
