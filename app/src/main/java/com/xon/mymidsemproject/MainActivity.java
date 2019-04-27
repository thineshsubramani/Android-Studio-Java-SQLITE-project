package com.xon.mymidsemproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {
    public static SQLiteHelper helper;
    EditText ed3,ed4;
    TextView tvid;
    Spinner sp1;
    Button btnsave,btnUpdate, imgeUpload;
    ImageView imageView;



    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private boolean hasImageChanged = false;
    Bitmap thumbnail;
    ImageView profileImages;
    Button update,deleteAcc;
    TextView welcome,fullname,phone1,emailAdd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new SQLiteHelper(this, "UserDB.sqlite", null, 1);
        helper.queryData("CREATE TABLE IF NOT EXISTS USER(email VARCHAR PRIMARY KEY , name VARCHAR, phone VARCHAR, password VARCHAR, image BLOB)");


        welcome = findViewById(R.id.wcname);
        fullname = findViewById(R.id.name2);
        phone1 = findViewById(R.id.contact2);
        emailAdd = findViewById(R.id.email2);
        profileImages = findViewById(R.id.profile);
        update = findViewById(R.id.updbtn);
        deleteAcc = findViewById(R.id.deleteAccount);

                Intent sendData = getIntent();
                final String getEmail = sendData.getStringExtra("Email");

              Intent updateBack = getIntent();
               final String updatedVersion = updateBack.getStringExtra("updEmail");


                Cursor cursor = MainActivity.helper.getData(getEmail);
                cursor.moveToFirst();
                final String email = cursor.getString(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String password = cursor.getString(3);
                profileImages.setImageBitmap(helper.getImage(email));

                welcome.setText(email);
               fullname.setText(name);
               emailAdd.setText(email);
               phone1.setText(phone);


            deleteAcc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    confirmDialog(getEmail);
                    }
            });


            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendData2 = new Intent(getApplicationContext(),UpdateActivity.class);
                    sendData2.putExtra("Email2",getEmail);
                    startActivity(sendData2);
                }
            });



    }
    private void confirmDialog(final String email2) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.confirmDelete));

        alertDialogBuilder
                .setMessage(this.getResources().getString(R.string.confirmDeleteMsge))
                .setCancelable(false)
                .setPositiveButton(this.getResources().getString(R.string.yesbutton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helper.deleteData(email2);
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(this.getResources().getString(R.string.nobutton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
