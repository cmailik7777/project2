package com.example.matias;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static com.example.matias.Prinyat.SAVED_BAR;
import static com.example.matias.Login.MY_PREFS_NAME;

public class PrinyatBar extends AppCompatActivity {

    String res1;
    SharedPreferences savePref1;
    String res2;
    public static final String SAVED_QR = "saved_text";

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otgrusit_qr);


        AlertDialog.Builder builder = new AlertDialog.Builder(PrinyatBar.this);
        builder.setTitle("Внимание!")
                .setMessage("Отсканируйте QR код!")
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                scannerView.setResultHandler(new PrinyatBar.ZXingScannerResultHandler());
                                scannerView.startCamera();

                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }


    private class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {
        @Override
        public void handleResult(final Result result) {
            final String resultCode = result.getText();
            //Toast.makeText(QRCode.this, resultCode, Toast.LENGTH_LONG).show();
            res2 = resultCode + "|";

//            AlertDialog.Builder builder = new AlertDialog.Builder(PrinyatBar.this);
//            builder.setTitle("QRCode распознан!")
//                    .setMessage(resultCode + " \nВерно?")
//                    .setCancelable(false)
//                    .setNegativeButton("Нет",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    scannerView = new ZXingScannerView(PrinyatBar.this);
//                                    scannerView.setResultHandler(new PrinyatBar.ZXingScannerResultHandler());
//
//                                    setContentView(scannerView);
//                                    scannerView.startCamera();
//                                    dialog.dismiss();
//                                }
//                            })
//                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                                    SharedPreferences savepref1 = PreferenceManager.getDefaultSharedPreferences(this);
//                                    SharedPreferences.Editor ed = savepref1.edit();
//                                    /* sKey is an array */
//                                    ed.putInt("Status_size", res2.size());
//
//                                    for (int i = 0; i < res2.size(); i++) {
//                                        ed.remove("Status_" + i);
//                                        ed.putString("Status_" + i, res2.get(i));
//                                    }
//                                    return;
//                                    ed.apply();


                            Perehod();


//                            SharedPreferences savepref = getSharedPreferences(SAVED_BAR, MODE_PRIVATE);
//                            res1 = savepref.getString("res1", null);
//                            Log.e("json", "--------------------------------------------------------------------------------------------- " + res1);
//
//
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();

            scannerView.stopCamera();

        }


    }

    public void Perehod( ) {
        final ArrayList<String> results = new ArrayList<String>();
        results.add(res1);
        results.add(res2);

        AlertDialog.Builder builder = new AlertDialog.Builder(PrinyatBar.this);
        builder.setTitle("Внимание!")
                .setMessage("Совершить еще одну операцию?")
                .setCancelable(false)
                .setNegativeButton("Нет",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(PrinyatBar.this, Zapros.class);
                                intent.putStringArrayListExtra("results",results);
                                startActivity(intent);
                                finish();
                            }
                        })
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Intent intent = new Intent(PrinyatBar.this, Prinyat.class);
                        startActivity(intent);
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        scannerView.stopCamera();
    }


}

