package com.example.matias;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Map;

import static com.example.matias.Login.MY_PREFS_NAME;
import static com.example.matias.Prinyat.SAVED_BAR;
import static com.example.matias.PrinyatBar.SAVED_QR;

public class Zapros extends AppCompatActivity  {
    private Zapross zapross;
    ProgressDialog progressDialog;
    ArrayList<DataZapros> dataZapros;
    private CustomZapros adapter;
    Button otpr;
    ListView listView;
    String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zapros_activity);

        otpr = findViewById(R.id.button_otpr);
        listView = findViewById(R.id.list);
//        SharedPreferences savePref = getSharedPreferences(SAVED_BAR, MODE_PRIVATE);
//        final Map<String, ?>  res2 = savePref.getAll();
//        SharedPreferences savePref1 = getSharedPreferences(SAVED_QR, MODE_PRIVATE);
//        final Map<String, ?> res1 = savePref1.getAll();
//        for (Map.Entry<String, ?> entry : res1.entrySet()) {
//            Log.e("res1", entry.getKey() + ": " + entry.getValue().toString());
//        }
//        for (Map.Entry<String, ?> entry : res2.entrySet()) {
//            Log.e("res1", entry.getKey() + ": " + entry.getValue().toString());
//        }
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                dataZapros.add(new DataZapros(res1 + "", res2 + ""));
//                adapter = new CustomZapros(dataZapros, getApplicationContext());
//
//                listView.setAdapter(adapter);
//            }
//        });



}

            public void Zaprosi(View view) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(Zapros.this);
                    progressDialog.setMessage("Пожалуйста подождите...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {

                    SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    pin = prefsIP.getString("pin", null);


//                    SharedPreferences savePref1 = getSharedPreferences(SAVED_QR, MODE_PRIVATE);
//                    Map<String, ?>   res1 = savePref1.getAll("res1");
//
//
//                    SharedPreferences savePref = getSharedPreferences(SAVED_BAR, MODE_PRIVATE);
//                    Map<String, ?>  res2 = savePref.getString("res2");

                    zapross = new Zapross();
                    zapross.start(pin);

                    try {
                        zapross.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }
                }
            }).start();

        }



    public class Zapross extends Thread {



    String Pin;
    InputStream is = null;
    String result = null;
    String line = null;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {



        ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

        NameValuerPair.add(new BasicNameValuePair("pin", Pin));


//
//        int j = 1;
//        for (int i = 0; i < res1.size() ; i++) {
//            NameValuerPair.add(new BasicNameValuePair("num"+i, (String) res1.get(i)));
//        }
//        int t = 1;
//        for (int i = 0; i < res2.size() ; i++) {
//            NameValuerPair.add(new BasicNameValuePair("numbank"+i, (String) res2.get(i)));
//        }
//
//        j = j + 1;
//        t = t + 1;
//        Log.e("pass 0", Pin);
//        Log.e("pass 0", String.valueOf(res1));
//        Log.e("pass 0", String.valueOf(res2));

        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://gps-monitor.kz/TESTMatias/Prinyat.php");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Zapros.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Zapros.this);
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



            if (res.equals("OK")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Zapros.this);
                        builder.setTitle("Коробка принята!")
                                .setMessage("Совершить еще одну операцию?")
                                .setCancelable(false)
                                .setPositiveButton("Да",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intentP = new Intent(Zapros.this, Prinyat.class);
                                                startActivity(intentP);
                                                finish();
                                            }
                                        })
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intentP = new Intent(Zapros.this, Menu.class);
                                        startActivity(intentP);
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
            if (res.equals("empty")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Zapros.this);
                        builder.setTitle("Ошибка")
                                .setMessage("Такая коробка есть на складе!")
                                .setCancelable(false)
                                .setNegativeButton("Повторить",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intentP = new Intent(Zapros.this, Prinyat.class);
                                                startActivity(intentP);
                                                finish();
                                                dialog.cancel();

                                            }
                                        })
                                .setPositiveButton("Да",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intentP = new Intent(Zapros.this, Menu.class);
                                                startActivity(intentP);
                                                finish();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Zapros.this);
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

    public void start(String pin) {

        this.Pin = pin;
        this.start();
    }



}

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("-----ZAPROS-----", "onPause");
    }

}


