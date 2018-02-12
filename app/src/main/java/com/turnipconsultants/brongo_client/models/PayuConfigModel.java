package com.turnipconsultants.brongo_client.models;

import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;

import lombok.Data;

/**
 * Created by mohit on 08-02-2018.
 */

@Data
public class PayuConfigModel {
     private PayuConfig payuConfig;
    private PaymentParams paymentParams;
    private PayuHashes payuHashes;

}
