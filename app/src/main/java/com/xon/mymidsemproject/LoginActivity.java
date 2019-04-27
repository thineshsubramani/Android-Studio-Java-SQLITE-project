package com.xon.mymidsemproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText ed1,ed2;
    Button btnreg, btnlogin;
    CheckBox cbox;
    SharedPreferences pref;
    SQLiteHelper helper;
    public User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


            ed1 = findViewById(R.id.emailL);
            ed2 = findViewById(R.id.passwordL);
            btnreg = findViewById(R.id.button2);
            btnlogin = findViewById(R.id.button);
            cbox = findViewById(R.id.checkBox);


        helper = new SQLiteHelper(this, "UserDB.sqlite", null, 1);

        helper.queryData("CREATE TABLE IF NOT EXISTS USER(email VARCHAR PRIMARY KEY , name VARCHAR, phone VARCHAR, password VARCHAR, image BLOB)");


        //createdb();
            btnreg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }
            });

        cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed1.getText().toString();
                String pass = ed2.getText().toString();
                savePref(email,pass);
            }
        });
        loadPref();


            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });


        }




        public void login(){
            String email = ed1.getText().toString();
            String password = ed2.getText().toString();

                try {

                    Boolean login = helper.loginCheck(email, password);
                    if (login == true) {

                        user = new User(email, password);
                        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
                        // Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        Intent sendData = new Intent(getApplicationContext(), MainActivity.class);
                        sendData.putExtra("Email", email);
                        startActivity(sendData);
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("DB", e.toString());
                }
                  }


    private void savePref(String e, String p) {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", e);
        editor.putString("password", p);
        editor.commit();
        Toast.makeText(this, "Preferences has been saved", Toast.LENGTH_SHORT).show();


    }

    private void loadPref() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String premail = pref.getString("email", "");
        String prpass = pref.getString("password", "");
        if (premail.length()>0){
            cbox.setChecked(true);
            ed1.setText(premail);
            ed2.setText(prpass);
        }
    }



    }

