package com.br.pagpeg.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.br.pagpeg.BuildConfig;
import com.br.pagpeg.R;
import com.br.pagpeg.model.CreditCard;
import com.br.pagpeg.retrofit.RetrofitService;
import com.br.pagpeg.retrofit.ServiceGenerator;
import com.br.pagpeg.retrofit.model.ClientPay;
import com.br.pagpeg.retrofit.model.PaymentToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import movile.com.creditcardguide.ActionOnPayListener;
import movile.com.creditcardguide.CreditCardFragment;
import movile.com.creditcardguide.model.CreditCardPaymentMethod;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PaymentMethod;
import movile.com.creditcardguide.model.PurchaseOption;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 23/07/16.
 */

public class AddCreditCardActivity extends AppCompatActivity implements ActionOnPayListener {

    private View view;
    private CreditCardFragment creditCardFragment;
    private DatabaseReference mDatabase;
    private String id_client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_credit_card);

        id_client = getIntent().getStringExtra("id_client");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbarMainActivity =(Toolbar)this.findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Novo cartão");

        creditCardFragment = (CreditCardFragment) getFragmentManager().findFragmentById(R.id.frgCreditCard);

        creditCardFragment.setPagesOrder(CreditCardFragment.Step.FLAG, CreditCardFragment.Step.NUMBER,
                CreditCardFragment.Step.EXPIRE_DATE, CreditCardFragment.Step.CVV, CreditCardFragment.Step.NAME);

        creditCardFragment.setListPurchaseOptions(getList());

    }

    private List<PurchaseOption> getList() {
        List<PurchaseOption> list = new ArrayList<>();
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.MASTERCARD, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.VISACREDITO, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AMEX, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.PAYPAL, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.DINERS, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.NUBANK, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AURA, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.ELO, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.HIPERCARD, 0));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.OTHER, 6));

        return list;
    }

    @Override
    public void onChangedPage(CreditCardFragment.Step page) {

    }

    @Override
    public void onComplete(CreditCardPaymentMethod purchaseOption, boolean saveCard) {
        paymentTokenRegister(purchaseOption);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        creditCardFragment.backPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void saveCreditCard(String uid,CreditCard creditCard){

        String key = mDatabase.child("credit_card").push().getKey();
        mDatabase.child("credit_card").child(uid).child(key).setValue(creditCard);

    }

    private void paymentTokenRegister(CreditCardPaymentMethod cc){

        final CreditCard creditCard = new CreditCard(cc.getCreditCardName(),String.valueOf(cc.getExpireMonth()) + "/"
                + String.valueOf(cc.getExpireYear()),cc.getCreditCardNumber(),
                cc.getSecurityCode(),cc.getIssuerCode().name(),"",cc.getIssuerCode().getIconId(),false);

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<PaymentToken> call = service.paymentTokenRegister(BuildConfig.ACCOUNT_ID_IUGU,"credit_card",true,creditCard.getCc_number(),creditCard.getCc_security_number(),
                "BRUNO","T L CORREA",String.valueOf(cc.getExpireMonth()),String.valueOf(cc.getExpireYear()));

        call.enqueue(new Callback<PaymentToken>() {
            @Override
            public void onResponse(Call<PaymentToken> call, Response<PaymentToken> response) {

                if(response.isSuccessful()){

                    PaymentToken paymentToken = response.body();
                    creditCard.setDisplay_number(paymentToken.getExtra_info().getDisplay_number());
                    Log.i("PaymentToken", paymentToken.getId());

                    clientPaymentRegister(creditCard,paymentToken.getId());

                }else{

                    Toast.makeText(getApplicationContext(),"Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    ResponseBody errorBody = response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<PaymentToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT);
            }
        });

    }

    private void clientPaymentRegister(final CreditCard creditCard, String token){

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<ClientPay> call = service.clientPaymentRegister(id_client,token,"Cartão de crédito");

        call.enqueue(new Callback<ClientPay>() {
            @Override
            public void onResponse(Call<ClientPay> call, Response<ClientPay> response) {

                if(response.isSuccessful()){

                    ClientPay clientPay = response.body();
                    Log.i("ClientPay", clientPay.getId());
                    creditCard.setToken(clientPay.getId());

                    saveCreditCard(FirebaseAuth.getInstance().getCurrentUser().getUid(),creditCard);

                }else{

                    Toast.makeText(getApplicationContext(),"Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    ResponseBody errorBody = response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<ClientPay> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT);
            }
        });

    }
}
