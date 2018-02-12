package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;

import lombok.Data;

/**
 * Created by mohit on 06-02-2018.
 */

@Data
public class PaymentSubscriptionResponse {
    private String message;
    private int statusCode;
    private List<DataEntity> data;


    @Data
    public class DataEntity{
        private List<SubPlans> subPlans;

        private List<String> conditions;

        private int requestsLeft;

        private String name;

        private String image;

        private float rating;

        @Data
        public class SubPlans{
            private String amount;

            private String condition;

            private double offer;

            private double priceForReq;

            private double amoutToPay;

            private String subId;

        }
    }
}
