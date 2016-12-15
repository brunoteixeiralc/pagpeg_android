package com.br.pagpeg.retrofit;

import com.br.pagpeg.BuildConfig;
import com.br.pagpeg.retrofit.model.Charge;
import com.br.pagpeg.retrofit.model.Client;
import com.br.pagpeg.retrofit.model.ClientPay;
import com.br.pagpeg.retrofit.model.PaymentToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by brunolemgruber on 14/12/16.
 */


public interface RetrofitService {

    @Headers("Authorization:Basic " + BuildConfig.IUGU_KEY)
    @FormUrlEncoded
    @POST("customers")
    Call<Client> clientRegister(@Field("email") String email, @Field("name") String name);

    @Headers("Authorization:Basic " + BuildConfig.IUGU_KEY)
    @FormUrlEncoded
    @POST("payment_token")
    Call<PaymentToken> paymentTokenRegister(@Field("account_id") String account_id,@Field("method") String method,@Field("test") boolean test,@Field("data[number]") String number,@Field("data[verification_value]") String verification_value
    ,@Field("data[first_name]") String first_name,@Field("data[last_name]") String last_name,@Field("data[month]") String month,@Field("data[year]") String year);

    @Headers("Authorization:Basic " + BuildConfig.IUGU_KEY)
    @FormUrlEncoded
    @POST("customers/{id_client}/payment_methods")
    Call<ClientPay> clientPaymentRegister(@Path("id_client") String id_client, @Field("token") String token, @Field("description") String description);

    @Headers("Authorization:Basic " + BuildConfig.IUGU_KEY)
    @FormUrlEncoded
    @POST("charge")
    Call<Charge> charge(@Field("customer_payment_method_id") String customer_payment_method_id, @Field("email") String email, @Field("items[][description]") String description,
                        @Field("items[][quantity]") String quantity,@Field("items[][price_cents]") String price_cents);

}
