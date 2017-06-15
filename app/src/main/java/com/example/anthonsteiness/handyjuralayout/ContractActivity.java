package com.example.anthonsteiness.handyjuralayout;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContractActivity extends AppCompatActivity {

    Button slutBtn;
    Button aftaleBtn;
    Button stdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        slutBtn = (Button) findViewById(R.id.btnSlutseddel);
        aftaleBtn = (Button) findViewById(R.id.btnAftaleseddel);
        stdBtn = (Button) findViewById(R.id.btnStandardByggeri);

        slutBtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           String pdf = "https://firebasestorage.googleapis.com/v0/b/handyjura-cdde0.appspot.com/o/Contracts%2FSlutseddel_bil.pdf?alt=media&token=c3c51e93-b180-4892-becd-dcec08c00c89";
                                           //String doc = "http://docs.google.com/viewer?url=";
                                           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                                           startActivity(browserIntent);
                                       }

                                   }
        );

        aftaleBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             String pdf = "https://firebasestorage.googleapis.com/v0/b/handyjura-cdde0.appspot.com/o/Contracts%2FAftaleseddel.pdf?alt=media&token=6bf50bd4-8f52-4693-87fc-9657283dd780";
                                             //String doc = "http://docs.google.com/viewer?url=";
                                             Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                                             startActivity(browserIntent);
                                         }

                                     }
        );

        stdBtn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String pdf = "https://firebasestorage.googleapis.com/v0/b/handyjura-cdde0.appspot.com/o/Contracts%2FStandardkontrakt_byggeri.pdf?alt=media&token=2c8660d1-8651-4aa7-b637-e612159b9572";
                                          //String doc = "http://docs.google.com/viewer?url=";
                                          Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                                          startActivity(browserIntent);
                                      }

                                  }
        );

    }


}
