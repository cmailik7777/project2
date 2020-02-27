package com.example.matias;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.matias.Login.MY_PREFS_NAME;

public class Prinyat extends AppCompatActivity {

    private ZXingScannerView scannerView;

    SharedPreferences savePref;

    public static final String SAVED_BAR = "saved_text";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otgrusit_qr);
        AlertDialog.Builder builder = new AlertDialog.Builder(Prinyat.this);
        builder.setTitle("Внимание!")
                .setMessage("Отсканируйте Штрих код!")
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {



        scannerView.setResultHandler(new Prinyat.ZXingScannerResultHandler());
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
final String res1 = resultCode + " " +"|";




            AlertDialog.Builder builder = new AlertDialog.Builder(Prinyat.this);
            builder.setTitle("Штрих код распознан!")
                    .setMessage(resultCode + " \nВерно?")
                    .setCancelable(false)
                    .setNegativeButton("Нет",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    scannerView = new ZXingScannerView(Prinyat.this);
                                    scannerView.setResultHandler(new Prinyat.ZXingScannerResultHandler());

                                    setContentView(scannerView);
                                    scannerView.startCamera();
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SharedPreferences savepref = getSharedPreferences(SAVED_BAR, Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed = savepref.edit();
                            ed.putString("res1", res1);
                            ed.apply();

                            Intent intent = new Intent(Prinyat.this, PrinyatBar.class);
                            startActivity(intent);


                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            scannerView.stopCamera();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
        Log.e("-----OTGRUS-----", "onPause");

    }

}



