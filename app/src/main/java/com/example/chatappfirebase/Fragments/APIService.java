package com.example.chatappfirebase.Fragments;

import com.example.chatappfirebase.Notifications.MyResponse;
import com.example.chatappfirebase.Notifications.Sender;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAVPGc50Q:APA91bFXbjIB0fhLtnr59skOZa2iYwWHPl-dPircmbHe5cjIwx_xPNR2ncHwq9ZgH5FAfGt7JaaE84U7cyNJU__C8258uYnSPK4Fk3OWkNuyrsk_EDPCcpxs9iiMhdUjgvuNAf1g-k4c"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
