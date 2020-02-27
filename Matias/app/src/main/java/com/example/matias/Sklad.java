package com.example.matias;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.matias.R;
import com.google.android.material.snackbar.Snackbar;

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

import static com.example.matias.Login.MY_PREFS_NAME;

public class Sklad extends AppCompatActivity {


 ProgressDialog progressDialog;
 ArrayList<DataModelSklad> dataModelSklad;
 ListView listView;
 private CustomAdapterModelSklad adapter;
 private Zaprosse zapros;


 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.sclad_activity);

  listView = findViewById(R.id.list);

  final String[] art = new String[1];


   SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
   String pin = prefs.getString("pin", null);

   zapros = new Zaprosse();
   zapros.start(pin);
   Log.e(" pin ", pin + " ");

   try {
    zapros.join();
   } catch (InterruptedException ie) {
    Log.e("pass 0", ie.getMessage());
   }

   String result = zapros.getResult();
   try {

    dataModelSklad = new ArrayList<>();

    JSONObject jsonObject = new JSONObject(result);
    JSONArray jsonArray = jsonObject.getJSONArray("res");
    int j = 1;
    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
     String ids = jsonArray.getJSONObject(i).getString("id");
     if (ids.isEmpty()) {
      ids = "Пусто";
     }
     Log.e("id ", ids);

     String nums = jsonArray.getJSONObject(i).getString("num");
     if (nums.isEmpty()) {
      nums = "Пусто";
     }
     Log.e("num ", nums);
     String dates = jsonArray.getJSONObject(i).getString("date");
     if (dates.isEmpty()) {
      dates = "Пусто";
     }
     Log.e("date ", dates);

     String numbanks = jsonArray.getJSONObject(i).getString("numbank");
     if (numbanks.isEmpty()) {
      numbanks = "Пусто";
     }
     Log.e("numbank ", numbanks);

     final String finalids = ids;
     final String finalnums = nums;
     final String finaldates = dates;
     final String finalnumbanks = numbanks;

     runOnUiThread(new Runnable() {
      @Override
      public void run() {
       dataModelSklad.add(new DataModelSklad(finalids + "", finalnums + "", finaldates + "", finalnumbanks + ""));
       adapter = new CustomAdapterModelSklad(dataModelSklad, getApplicationContext());

       listView.setAdapter(adapter);

      }
     });

     j = j + 1;
    }


    Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
   } catch (Exception e) {
    Log.e("Fail 4", e.toString());
   }

  }


 public class Zaprosse extends Thread {


  String PinS;

  InputStream is = null;
  String result = null;
  String line = null;

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public void run() {


    ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

    NameValuerPair.add(new BasicNameValuePair("pin", PinS));


    try {
     HttpClient httpClient = new DefaultHttpClient();
     HttpPost httpPost = new HttpPost("http://gps-monitor.kz/TESTMatias/stock.php?");
     httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
     HttpResponse resArr = httpClient.execute(httpPost);
     HttpEntity entity = resArr.getEntity();
     is = entity.getContent();
     Log.e("pass 1", "connection succes");
    } catch (Exception e) {
     Log.e("Fail 1", e.toString());
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
     Log.e("pass 2", "connection succes " + result);
    } catch (Exception e) {
     Log.e("Fail 2", e.toString());
    }
    try {
     Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
    } catch (Exception e) {
     Log.e("Fail 3", e.toString());
    }
   }


  public void start(String pin) {

   this.PinS = pin;
   this.start();
  }

  public String getResult() {
   return result;
  }
 }


 @Override
 protected void onStop() {
  super.onStop();
  Log.e("-----Sklad-----", "onStop");
 }
}
