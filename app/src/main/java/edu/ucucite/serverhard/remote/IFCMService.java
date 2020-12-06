package edu.ucucite.serverhard.remote;

import edu.ucucite.serverhard.model.FCMResponse;
import edu.ucucite.serverhard.model.FCMSendData;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA1sqBI0M:APA91bGvjGNDsTFGUYqXwT0H9_jvOp1GKCwoypw_mHnjhXp-qSa908qP8z29DdOxoFNRTXbsSufvZegh95dDTeinkgleHegY0FOOAVd7bLKk9ks9B0ogc8i1-CHZe1M67xJKUTlXQxqo"

    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}

