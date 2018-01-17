package com.turnipconsultants.brongo_client.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by mohit on 15-01-2018.
 */

@Data
public class AllRequirementsResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("message")
    private String message;

    @Data
    public class DataEntity {
        private List<Content> content;

        private String postingType;

        private String propertyType;

        private String propertyId;

        private List<String> images;

        @Data
        public class Content {
            private String content;

            private String title;
        }
    }
}
