package com.turnipconsultants.brongo_client.others;

import com.turnipconsultants.brongo_client.responseModels.FeedBackQueResponse;
import com.turnipconsultants.brongo_client.responseModels.PastRequirementResponse;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.DeviceInfoInputModel;
import com.turnipconsultants.brongo_client.models.OtpInputModel;
import com.turnipconsultants.brongo_client.models.PaymentHashModel;
import com.turnipconsultants.brongo_client.models.SellYourProperty.PaymentHashResponseModel;
import com.turnipconsultants.brongo_client.models.SignUpInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.responseModels.AcceptedBrokersResponseModel;
import com.turnipconsultants.brongo_client.responseModels.AllRequirementsResponse;
import com.turnipconsultants.brongo_client.responseModels.BrokerRatingResponseModel;
import com.turnipconsultants.brongo_client.responseModels.BrokersCountModel;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.PaymentSubscriptionResponse;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;
import com.turnipconsultants.brongo_client.responseModels.DeviceInfo;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.KnowlarityApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.NotificationResponseModel;
import com.turnipconsultants.brongo_client.responseModels.OtpResponseModel;
import com.turnipconsultants.brongo_client.responseModels.ProfileResponseModel;
import com.turnipconsultants.brongo_client.responseModels.QuestionsResponseModel;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponse;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponseModel;
import com.turnipconsultants.brongo_client.responseModels.SignUpResponseModel;
import com.turnipconsultants.brongo_client.responseModels.TimeLineResponseModel;
import com.turnipconsultants.brongo_client.responseModels.TokenResponseModel;
import com.turnipconsultants.brongo_client.responseModels.UserExistResponseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitAPIs {
    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/signup")
    Call<SignUpResponseModel> getUserSignUp(@Body SignUpInputModel signUpInputModel);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/OTPVerification")
    Call<OtpResponseModel> getOtpVerified(@Body OtpInputModel otpInputModel);

    @GET("/Brongo/client/checkUserExistance")
    Call<UserExistResponseModel> getLoginOtp(@Query("userId") String userId, @Query("loginType") String loginType);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/tokenGenerator")
    Call<TokenResponseModel> getTokenGenerated(@Body TokenInputModel tokenInputModel);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/buyProperty")
    Call<PropertyTransactionResponseModel> getBuyAProperty(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body Object buyPropInputModel);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/postByTenant")
    Call<PropertyTransactionResponseModel> getRentAProperty(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body Object rentAPropertyResidential);

    @Multipart
    @POST("/Brongo/client/sellProperty")
    Call<PropertyTransactionResponseModel> getSellYourProperty(
            @Header("accessToken") String accessToken,
            @Header("platform") String platform,
            @Header("deviceId") String deviceId,

            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part propertyPicture1,
            @Part MultipartBody.Part propertyPicture2,
            @Part MultipartBody.Part propertyPicture3);

    @Multipart
    @POST("/Brongo/client/postByOwner")
    Call<PropertyTransactionResponseModel> getRentYourProperty(
            @Header("accessToken") String accessToken,
            @Header("platform") String platform,
            @Header("deviceId") String deviceId,

            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part propertyPicture1,
            @Part MultipartBody.Part propertyPicture2,
            @Part MultipartBody.Part propertyPicture3);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/deviceInfo")
    Call<DeviceInfo> sendDeviceInfo(@Body DeviceInfoInputModel model);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/acceptedBrokers")
    Call<AcceptedBrokersResponseModel> getAcceptedBrokerDetails(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);


    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/acceptingBroker")
    Call<GeneralApiResponseModel> getConnectedBroker(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);


    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/getBrokerRating")
    Call<BrokerRatingResponseModel> getBrokerRating(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);

    @GET("/Brongo/client/call")
    Call<KnowlarityApiResponseModel> getKnowlarityCall(@Query("from") String from, @Query("to") String to);

//    @GET("/Brongo/client/activeLeads")
//    Call<PastRequirementResponse> getPastRequirementData(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("onlyConnected") boolean onlyConnected);

    @GET("/Brongo/client/activeDeals")
    Call<PastRequirementResponse> getPastRequirementData(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("dealType") String dealType);

//    @GET("/Brongo/client/activeLeads")
//    Call<SecondLandingResponseModel> getPostedLeadsData(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("onlyConnected") String onlyConnected);

//    @GET("/Brongo/client/activeLeads")
//    Call<SecondLandingResponse> getPostedLeadsData2(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("onlyConnected") String onlyConnected);

    @GET("/Brongo/client/activeDeals")
    Call<SecondLandingResponse> getPostedLeadsData2(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("dealType") String dealType);


    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/fetchLeadStatus")
    Call<TimeLineResponseModel> fetchLeadStatus(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/challengeStatus")
    Call<GeneralApiResponseModel> challengeStatus(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);

    @GET("/Brongo/client/BrongoQuestions")
    Call<QuestionsResponseModel> getBrongoQuestions(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("questionsType") String questionsType);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/dropLead")
    Call<GeneralApiResponseModel> getLeadDrop(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);


    @Multipart
    @POST("/Brongo/client/updateClientProfile")
    Call<GeneralApiResponseModel> updateClientProfile(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Part("firstName") RequestBody firstName, @Part("mobileNo") RequestBody mobileNo, @Part("lastName") RequestBody lastName, @Part("emailId") RequestBody emailId, @Part("gender") RequestBody gender, @Part("alternateMobileNo") RequestBody alternateMobileNo, @Part("hideProfilePic") RequestBody hideProfilePic, @Part MultipartBody.Part profileImage);


    @GET("/Brongo/client/getClientProfile")
    Call<ProfileResponseModel> getClientProfile(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo);


    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/cancleDealSearch")
    Call<GeneralApiResponseModel> getCancleDealSearch(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);

    @GET("/Brongo/client/getNotifications")
    Call<NotificationResponseModel> getNotifications(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("from") String from, @Query("size") String size);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/connectToNewBroker")
    Call<GeneralApiResponseModel> getConnectToNewBroker(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel accepBrokInputModel);


    @GET("/Brongo/client/brokersCount")
    Call<BrokersCountModel> getBrokersCount(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/payment")
    Call<PaymentHashResponseModel> getPaymentHash(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body PaymentHashModel paymentHashModel);

    @GET("/Brongo/client/readNotification")
    Call<ResponseBody> SetNotificationRead(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo, @Query("id") String id);

    @GET("/Brongo/client/callBack")
    Call<ResponseBody> CallBack(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("from") String from, @Query("to") String to);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/brokerRating")
    Call<GeneralApiResponseModel> postBrokerFeedBack(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel inputModel);

    @GET("/Brongo/client/fetchMicroMarkets")
    Call<FetchMicroMarketResponse> getMicroMarketDetails(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/fetchProperty")
    Call<AllRequirementsResponse> getAllRequirementDetails(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body AcceptedBrokersInputModel inputModel);

    @GET("/Brongo/client/feedbackQue")
    Call<FeedBackQueResponse> getFeedBackQue(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/addMicroMarket")
    Call<GeneralApiResponseModel> postMicroMarketGoogle(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body Object buyLandModel);

    @GET("/Brongo/client/getMyPlan")
    Call<PaymentSubscriptionResponse> getMyPlanSubscription(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Query("mobileNo") String mobileNo);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/connectToNewBroker")
    Call<GeneralApiResponseModel> connectToNewBroker(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body Object inputModel);

    @Headers({"Accept: application/json"})
    @POST("/Brongo/client/unSubscribe")
    Call<GeneralApiResponseModel> unsubscribeCall(@Header("accessToken") String accessToken, @Header("platform") String platform, @Header("deviceId") String deviceId, @Body Object inputModel);

}

