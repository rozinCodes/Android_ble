package com.waltonbd.smartscale.api;


import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.models.Measurement;
import com.waltonbd.smartscale.models.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIRequests {

    @POST("api/auth/signin")
    Call<User> login(@Body UserLogin login);

    @GET("api/auth/measurements/get")
    Call<List<Measurement>> getMeasurementById(@Header("Authorization") String token,
                                               @QueryMap Map<String, Long> params);

    @DELETE("api/auth/measurements/{id}")
    Call<BaseResponse> deleteMeasurement(@Header("Authorization") String token,
                                         @Path("id") long id);

    @POST("api/auth/userExists")
    Call<BaseResponse> isUserExists(@Body UserRegistration username);

    @POST("api/auth/signup")
    Call<User> registration(@Body UserRegistration register);

    @POST("api/auth/userExists")
    Call<BaseResponse> verifyUser(@Body ForgetPassword username);

    @POST("api/auth/sendotp")
    Call<BaseResponse> sendOtp(@Body ForgetPassword username);

    @POST("api/auth/verifyOtp")
    Call<BaseResponse> verifyOtp(@Body ForgetPassword otp);

    @POST("api/auth/setPassword")
    Call<BaseResponse> forgotPassword(@Body ForgetPassword password);

    @Multipart
    @POST("api/auth/uploadProfilePic2/{id}")
    Call<BaseResponse> uploadPicture(@Header("Authorization") String token,
                                     @Path("id") long id,
                                     @Part MultipartBody.Part image);

    @POST("api/auth/guest")
    Call<User> addMember(@Header("Authorization") String token,
                         @Body UserProfile user);

    @DELETE("api/auth/guest/{id}")
    Call<BaseResponse> deleteMember(@Header("Authorization") String token,
                                    @Path("id") long id);

    @PUT("api/auth/user/{id}")
    Call<BaseResponse> updateProfile(@Header("Authorization") String token,
                                     @Path("id") long id,
                                     @Body UserProfile user);

    @POST("api/auth/changePassword")
    Call<BaseResponse> changePassword(@Header("Authorization") String token,
                                      @Body ChangePassword password);

    @POST("api/auth/goal")
    Call<ResponseBody> setGoat(@Header("Authorization") String token,
                               @Body SetGoal goal);

    @POST("api/auth/contacts")
    Call<BaseResponse> sendFeedback(@Body Feedback feedback);

    @GET("api/auth/unassigned/{id}")
    Call<ResponseBody> getUnassignedData(@Header("Authorization") String token,
                                         @Path("id") long id);

    @HTTP(method = "DELETE", path = "api/auth/unassigned/delete", hasBody = true)
    Call<BaseResponse> deleteUnassignedData(@Header("Authorization") String token,
                                            @Body UnassignedRequest unassignedRequest);


    @POST("api/auth/measurements")
    Call<BaseResponse> sendMeasurementData(@Header("Authorization") String token,
                                           @Body Measurement data);
}
