package com.example.matias;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

//import com.example.matias.Sklad.Sklad;
import com.google.android.material.snackbar.Snackbar;

public class Menu extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private View mLayout;

    private TextView FIO;
   // Login.Getname getname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);


        mLayout = findViewById(R.id.Menu);



//        FIO = findViewById(R.id.FIO);


//        String name = getname.get_Name();
//
//        FIO.setText(name);
    }

public  void  Skladi(View view){
    Intent intentV = new Intent(Menu.this, Sklad.class);
    startActivity(intentV);
}

    public  void  Zaiavka(View view){
        Intent intentS = new Intent(Menu.this, Zaiavka.class);
        startActivity(intentS);
    }

    public void Otgrusiti(View view) {
        showCameraPreview();

    }
    public void Prinuat(View view) {
        showCameraPreviews();
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для исполнения операции с QR кодом, " +
                            "вам необходимо разрешить использовании камеры в приложении . \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(Menu.this,
                                            new String[]{android.Manifest.permission.CAMERA},
                                            PERMISSION_REQUEST_CAMERA);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для исполнения операции с QR кодом, " +
                            "вам необходимо разрешить использовании камеры в приложении . \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(Menu.this,
                                            new String[]{android.Manifest.permission.CAMERA},
                                            PERMISSION_REQUEST_CAMERA);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Использования камеры теперь доступно",
                        Snackbar.LENGTH_SHORT)
                        .show();
                startCamera();
                startsCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Разрешение камеры отклонено",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intentVs = new Intent(Menu.this, Otgrusit.class);
            startActivity(intentVs);

            } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        // END_INCLUDE(startCamera)
    }
    private void showCameraPreviews() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intentVs = new Intent(Menu.this, Prinyat.class);
            startActivity(intentVs);

        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        // END_INCLUDE(startCamera)
    }
    private void startCamera() {
        Intent intentV = new Intent(this, Otgrusit.class);

        startActivity(intentV);

    }
    private void startsCamera() {
        Intent intentVs = new Intent(this, Prinyat.class);
        startActivity(intentVs);
    }
}