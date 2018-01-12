package com.turnipconsultants.brongo_client.Listener;

/**
 * Created by Pankaj on 07-12-2017.
 */

public interface CommissionListenerFactory {
    interface BuyCommissionListener {
        void SetCommission(String commissionValue);

        void Accept();
    }

    interface RentCommissionListener {
        void SetAdvanceRentMonths(String commissionValue);

        void Accept();
    }

    interface SellCommissionListener {
        void Accept();
    }

    interface RentOutCommissionListener {
        void Accept();
    }
}
