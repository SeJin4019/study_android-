package com.delivery.mydelivery.register;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

// 회원가입 Api
public interface RegisterApi {

    // 이메일 중복검사
    @GET("/register/registerCk/{email}")
    Call<Boolean> duplicateEmailCk(@Path("email") String email);

    // 인증번호 전송
    @GET("/register/sendAuthNum/{email}")
    Call<String> sendAuthNum(@Path("email") String email);

    // 회원가입
    @POST("/register")
    Call<UserVO> register(@Body UserVO user);
}
