package com.turnipconsultants.brongo_client.others.Constants;

/**
 * Created by mohit on 28-09-2017.
 */

public class AppConstants {
    public static final String PREF_NAME = "BRONGO";
    public static final String DB_NAME = "greendao_brongo.db";
    public static final String FIREBASE_TOKEN = "firebase_token";
    public static final String APPLOZIC_PASSWORD = "Brongo_Client";
    public static final String BOT_ID = "BrongoChatBot";

    public static class POPULAR_LOCATIONS {
        public static final String BANGALORE[] = {"Marathahalli", "Hennur", "Whitefield", "Thanisandra", "Sarjapur Outer Ring Rd"};
    }

    public static class PROPERTY {
        public static final String PROPERTY_TYPE = "PROPERTY_TYPE";
        public static final int BUY_A_PROPERTY = 1;
        public static final int RENT_A_PROPERTY = 2;
        public static final int SELL_YOUR_PROPERTY = 3;
        public static final int RENT_YOUR_PROPERTY = 4;
    }

    public static class SUBPROPERTY {
        public static final String SUB_PROPERTY_TYPE = "SUB_PROPERTY_TYPE";
        public static final int BUY_A_LAND_PROPERTY = 1;
        public static final int SELL_A_LAND_PROPERTY = 2;
    }

    public static class PREFS {
        public static final String LOGGED_IN = "loggedIn";

        public static final String USER_FIRST_NAME = "firstName";
        public static final String USER_LAST_NAME = "lastName";
        public static final String USER_EMAIL = "email";
        public static final String USER_MOBILE_NO = "mobileNo";
        public static final String USER_RATING = "rating";
        public static final String USER_PROFILE_PIC_FILE = "profilePicFile";

        public static final String CHAT_COUNT = "CHAT_COUNT";
        public static final String NOTI_COUNT = "NOTI_COUNT";
        public static final String BROKER_COUNT = "BROKER_COUNT";
        public static final String IS_SECOND = "isSecondLand";
    }

    public static class POSTING_TYPE {
        public static final String BUY_A_PROPERTY = "BUY";
        public static final String RENT_A_PROPERTY = "RENT";
        public static final String SELL_A_PROPERTY = "SELL";
        public static final String RENT_YOUR_PROPERTY = "RENT_OUT";
    }


    public static class SUB_PROPERTY_TYPE {
        public static final String COMMERCIAL = "COMMERCIAL_ZONE";
        public static final String RESIDENTIAL = "RESIDENTIAL_ZONE";
    }

    public static class VISIBILITY_PAGE {
        public static final String GET_CONNECTED_ACTIVITY = "GET_CONNECTED_ACTIVITY";
        public static final String SECOND_TIME_ACTIVITY = "SECOND_TIME_ACTIVITY";
    }

    public static class LAT_LONG {
        public static final double KORAMANGLA[] = {12.928006, 77.626871};
        public static final double HSR_LAYOUT[] = {12.908920, 77.647763};
        public static final double MARATHALLI[] = {12.959428, 77.697737};
        public static final double BOMMANHALLI[] = {12.898406, 77.617919};
        public static final double ELECTRONIC_CITY[] = {12.841183, 77.675914};
        public static final double MARATHAHALLI[] = {12.959172, 77.697419};
        public static final double SARJAPUR[] = {12.9336758, 77.6886244};
        public static final double WHITEFIELD[] = {12.9677766, 77.7430503};
        public static final double THANISANDRA[] = {13.0557365, 77.623226};
        public static final double HENNUR[] = {13.0370654, 77.6443206};

    }

    public static class SPECIFIC_PUSH {
        public static final String NOTIFICATION_TYPE = "NotiType";              // Key to filter notification type.

        public static final String ACCEPTED_BROKERS = "BROKER_ACCEPT";
        public static final String LEADS_UPDATE = "LEADS_UPDATE";
    }

    public static class NOTIFICATION_TYPE {
        public static final String BUILDER = "BUILDER";
        public static final String APP = "APP";
        public static final String OFFERS = "OFFERS";
        public static final String GENERAL = "GENERAL";
        public static final String STOP_TIMER = "STOP_TIMER";
        public static final String CLIENT_ACCEPT = "CLIENT_ACCEPT";
        public static final String BROKER_ACCEPT = "BROKER_ACCEPT";
        public static final String LEADS_UPDATE = "LEADS_UPDATE";
        public static final String PAY_U_PAYMENT = "PAY_U_PAYMENT";
        public static final String CALL_BACK = "CALL_BACK";
    }

    public static class PAYMENT_ROW_TYPE {
        public static final int SINGLE_TYPE = 1;
        public static final int MIX_TYPE = 2;
    }

    public static class FB_USER {
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String EMAIL = "email";
        public static final String MOBILE = "mobile";
    }

    public static class BUDGET_RANGE {
        public static final String[] BUY = {"0", "5.0L", "10.0L", "15.0L", "20.0L", "25.0L", "30.0L", "35.0L", "40.0L", "45.0L", "50.0L", "55.0L", "60.0L", "65.0L", "70.0L", "75.0L", "80.0L", "85.0L", "90.0L", "95.0L", "1.0Cr", "1.1Cr", "1.2Cr", "1.3Cr", "1.4Cr", "1.5Cr", "1.6Cr", "1.7Cr", "1.8Cr", "1.9Cr", "2.0Cr", "2.1Cr", "2.2Cr", "2.3Cr", "2.4Cr", "2.5Cr", "2.75Cr", "3.0Cr", "3.25Cr", "3.5Cr", "3.75Cr", "4.0Cr", "4.25Cr", "4.5Cr", "4.75Cr", "5.0Cr", "Above 5Cr"};
        public static final String[] RENT = {"0", "5.0K", "10.0K", "15.0K", "20.0K", "25.0K", "30.0K", "35.0K", "40.0K", "45.0K", "50.0K", "55.0K", "60.0K", "65.0K", "70.0K", "75.0K", "80.0K", "85.0K", "90.0K", "95.0K", "1.0L", "1.1L", "1.2L", "1.3L", "1.4L", "1.5L", "1.6L", "1.7L", "1.8L", "1.9L", "2.0L", "2.1L", "2.2L", "2.3L", "2.4L", "2.5L", "2.75L", "3.0L", "3.25L", "3.5L", "3.75L", "4.0L", "4.25L", "4.5L", "4.75L", "5.0L", "5.5L", "6.0L", "6.5L", "7.0L", "7.5L", "8.0L", "8.5L", "9.0L", "9.5L", "10.0L", "Above 10L"};
        public static final long[] RENT_VALUE = {0, 5000, 10000, 15000, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 55000, 60000, 65000, 70000, 75000, 80000, 85000, 90000, 95000, 100000, 110000, 120000, 130000, 140000, 150000, 160000, 170000, 180000, 190000, 200000, 210000, 220000, 230000, 240000, 250000, 275000, 300000, 325000, 350000, 375000, 400000, 425000, 450000, 475000, 500000, 550000, 600000, 650000, 700000, 750000, 800000, 850000, 900000, 950000, 1000000, 1000000};
    }

    public static class FRAGMENT_TAGS {
        public static final String ACCEPTED_BROKER_LIST = "ACCEPTED_BROKER_LIST";
        public static final String CONNECTION_SUCCESS_FRAGMENT = "CONNECTION_SUCCESS_FRAGMENT";
        public static final String BROKER_REVIEW_FRAGMENT = "BROKER_REVIEW_FRAGMENT";

        public static final String FEEDBACK_STAGE1_FRAGMENT = "FEEDBACK_STAGE1_FRAGMENT";
        public static final String FEEDBACK_STAGE2_FRAGMENT = "FEEDBACK_STAGE2_FRAGMENT";
    }
}
