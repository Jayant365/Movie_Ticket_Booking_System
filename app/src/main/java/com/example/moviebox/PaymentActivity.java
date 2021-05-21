package com.example.moviebox;

import android.app.Activity;
import android.content.Intent;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentActivity extends AppCompatActivity {

    PayPalConfiguration payPalConfiguration; // paypal object from downloaded SDK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView price = findViewById(R.id.textViewPrice);
        TextView pay = findViewById(R.id.textViewPay);

        int noOfTickets;

        Bundle bundle = getIntent().getExtras();

        //Check if there is a movie name
        if (bundle != null)
        {
            //0 for now showing and 1 for upcoming
            noOfTickets = bundle.getInt("noOfTickets");
        }
        else
        {
            noOfTickets = 1;
        }

        final int priceTicket = 10 * noOfTickets;

        pay.setText("You total price : $" + priceTicket);



        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK) //ENVIRONMENT_NO_NETWORK, because app is in development phase
                .clientId("AT1moYlQvscjeViNpK0ILpZHWr2bFnbY5NwxcM8aCBVr5_upmd2n_dHzCqP0p8zt8jWqmi_Z3oHx3kuZ");

        Button buttonPayment = findViewById(R.id.buttonPayment);

        //sandbox acount - sb-0gmjn521314@business.example.com
        //Client id - AT1moYlQvscjeViNpK0ILpZHWr2bFnbY5NwxcM8aCBVr5_upmd2n_dHzCqP0p8zt8jWqmi_Z3oHx3kuZ
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PayPalPayment payPalPayment =
                        new PayPalPayment(new BigDecimal(priceTicket), "CAD", "Movie Ticket",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent =
                        new Intent(PaymentActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class); // .class is payment activity class of paypal

                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                // paypal default keys

                intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payPalPayment);

                startActivityForResult(intent, 0);

            }
        });


    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) { // if payment is successful
            PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    startActivity(new Intent(PaymentActivity.this, PaymentSuccessfulActivity.class));
                    Toast.makeText(this, "Successful Payment", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("payment", "Payment Failure: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted." +
                    " Please see the docs.");
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Can not go back at this point.", Toast.LENGTH_SHORT).show();
    }
}
