package com.example.matias;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class Otgrusit extends AppCompatActivity {

    private ZXingScannerView scannerView;
    private Zapross zapross;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otgrusit_qr);


        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        scannerView.setResultHandler(new ZXingScannerResultHandler());
        scannerView.startCamera();


    }

    private class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {
        @Override
        public void handleResult(final Result result) {
            final String resultCode = result.getText();
            //Toast.makeText(QRCode.this, resultCode, Toast.LENGTH_LONG).show();


            AlertDialog.Builder builder = new AlertDialog.Builder(Otgrusit.this);
            builder.setTitle("QRCode распознан!")
                    .setMessage(resultCode + " \nВерно?")
                    .setCancelable(false)
                    .setNegativeButton("Нет",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    scannerView = new ZXingScannerView(Otgrusit.this);
                                    scannerView.setResultHandler(new ZXingScannerResultHandler());

                                    setContentView(scannerView);
                                    scannerView.startCamera();
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Zaprosik(resultCode);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            scannerView.stopCamera();
        }
    }

    public void Zaprosik(final String resultCode) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(Otgrusit.this);
                progressDialog.setMessage("Пожалуйста подождите...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                                  SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                  String pin = prefs.getString("pin", null);

                zapross = new Zapross();
                zapross.start(resultCode,pin);

                try {
                    zapross.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();

    }

    public class Zapross extends Thread {


        String PinS;
        String ResultCodeS;

        InputStream is = null;
        String result = null;
        String line = null;


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", ResultCodeS));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/TESTMatias/Otgruzit.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "операция прошла успешно");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Otgrusit.this);
                        builder.setTitle("Произошла ошибка")
                                .setMessage(" \n" +
                                        "Соединение с Сервером")
                                .setCancelable(false)
                                .setNegativeButton("ок",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "операция прошла успешно " + result);

            } catch (final Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Otgrusit.this);
                        builder.setTitle("Произошла ошибка!")
                                .setMessage(" \n" +
                                        "Чтения данных")
                                .setCancelable(false)
                                .setNegativeButton("ок",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
            try {


                JSONObject jsonObject = new JSONObject(result);
                final String res = jsonObject.getString("res");
                Log.e("json", "Результат ip: " + res);

                if (res.equals("empty")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Otgrusit.this);
                            builder.setTitle("Ошибка!")
                                    .setMessage("Такой позиции нет на складе")
                                    .setCancelable(false)
                                    .setPositiveButton("Да",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    scannerView = new ZXingScannerView(Otgrusit.this);
                                                    scannerView.setResultHandler(new Otgrusit.ZXingScannerResultHandler());

                                                    setContentView(scannerView);
                                                    scannerView.startCamera();
                                                    dialog.cancel();

                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }

                            if (res.equals("OK")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Otgrusit.this);
                                        builder.setTitle("Отгружено!")
                                                .setMessage("Совершить еще одну операцию?")
                                                .setCancelable(false)
                                                .setPositiveButton("Да",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                scannerView = new ZXingScannerView(Otgrusit.this);
                                                                scannerView.setResultHandler(new Otgrusit.ZXingScannerResultHandler());

                                                                setContentView(scannerView);
                                                                scannerView.startCamera();
                                                                dialog.cancel();

                                                            }
                                                        })
                                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intentP = new Intent(Otgrusit.this, Menu.class);
                                                        startActivity(intentP);
                                                        finish();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            }
                if (res.equals("no")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Otgrusit.this);
                            builder.setTitle("Ошибка")
                                    .setMessage("Ошибка считывания данных!")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    scannerView = new ZXingScannerView(Otgrusit.this);
                                                    scannerView.setResultHandler(new Otgrusit.ZXingScannerResultHandler());

                                                    setContentView(scannerView);
                                                    scannerView.startCamera();
                                                    dialog.cancel();

                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                progressDialog.dismiss();
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Otgrusit.this);
                        builder.setTitle("Произошла ошибка")
                                .setMessage("ошибка\n" + ""
                                )
                                .setCancelable(false)
                                .setNegativeButton("ок",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        }

        public void start(String resultCode,String pin) {

          this.PinS = pin;
            this.ResultCodeS = resultCode;
            this.start();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
        Log.e("-----OTGRUS-----", "onPause");
    }

}

