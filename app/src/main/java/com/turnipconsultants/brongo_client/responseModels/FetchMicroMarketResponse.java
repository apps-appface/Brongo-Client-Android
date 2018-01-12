package com.turnipconsultants.brongo_client.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;


/**
 * Created by mohit on 11-01-2018.
 */

@Data
public class FetchMicroMarketResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("message")
    private String message;

    @Data
    public class DataEntity {
        private String microMarketId;
        private String name;
        private String city;
        private String state;
        private MarketLocation marketLocation;
        private int brokersCount;
        private boolean trending;

        @Data
        public class MarketLocation {
            private String type;
            private List<String> coordinates;
        }
    }

}
