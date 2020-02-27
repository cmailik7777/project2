package com.example.matias;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matias.service.NetworkService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private EditText pin;
    private ProgressDialog progressDialog;
    private Zapros zapros;
    private Toast toast;
    public String PinS;
    public static String name;
    private String PostFP,PostIP,PostOP;


    private String PostErrorS,PostFS,PostIS,PostOS;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pin = findViewById(R.id.pin);


    }

    public void OnClick(View view) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                PinS = pin.getText().toString();
                zapros = new Zapros();
                zapros.start(PinS);

                try {
                    zapros.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                PostErrorS = zapros.resPostErrorP();

                if (PostErrorS.equals("empty")){

                }else{

                    PostFS = zapros.resPostF();
                    PostIS = zapros.resPostI();
                    PostOS = zapros.resPostO();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                            editor.putString("pin", pin.getText().toString());
                            editor.apply();

                            progressDialog.dismiss();
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("")
                                    .setMessage("Добро пожаловать, \n")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    Intent intent = new Intent(Login.this, Menu.class);
                                                    startActivity(intent);

                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });

                }





            }
        }).start();






    }

    public class Zapros extends Thread {


        String PinS;

        String PostFP = "";
        String PostIP = "";
        String PostOP = "";

        String PostErrorP = "";

        InputStream is = null;
        String result = null;
        String line = null;


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run(){


            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(6);

            NameValuerPair.add(new BasicNameValuePair("pin",PinS));




            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/TESTMatias/MatiasVhod.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();

//                NetworkService.executeRequest();
//                Log.e("pass 1", "connection succes");
            } catch (Exception e){
//                Log.e("Fail 1", e.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Ошибка!")
                                .setMessage("Ошибка подключения! Пожалуйста попробуйте позже.")
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

            try{

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();

                Log.e("pass 2", "connection succes" + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Ошибка!")
                                .setMessage("Ошибка подключения! " +
                                        "Пожалуйста попробуйте позже.")
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
                PostErrorP = jsonObject.getString("ERROR");
                if (PostErrorP.equals("empty")){ // не правильный пин ИЛИ моб
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Неправильный пароль",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    PostFP = jsonObject.getString("fam");
                    if (PostFP.equals("")){
                        PostFP = "";
                    }
                    Log.e("json", "Результат: " + PostFP);

                    PostIP = jsonObject.getString("name");
                    if (PostIP.equals("")){
                        PostIP = "";
                    }
                    Log.e("json", "Результат: " + PostFP);

                    PostOP = jsonObject.getString("otch");
                    if (PostOP.equals("")){
                        PostOP = "";
                    }
                    Log.e("json", "Результат: " + PostOP);


                }


            } catch (Exception e){
                Log.e("Fail 3", e.toString());
            }





        }

        public void start(String pinp){

            this.PinS = pinp;

            this.start();

        }

        public String resPostF (){
            return PostFP;
        }
        public String resPostI (){
            return PostIP;
        }
        public String resPostO (){
            return PostOP;
        }

        public String resPostErrorP (){
            return PostErrorP;
        }

    }


}


class Student {
    String name;
    String surname;
    int age;
}