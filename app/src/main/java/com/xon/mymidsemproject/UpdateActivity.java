package com.xon.mymidsemproject;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    Button updatebtn4,cancelbtn;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    User user;
    ImageView imgbtn3;
    final int REQUEST_CODE_GALLERY = 999;
    public static SQLiteHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        ed1 = findViewById(R.id.updEmailTxt);
        ed2 = findViewById(R.id.updNametxt);
        ed3 = findViewById(R.id.updatePhonetxt);
        ed4 = findViewById(R.id.updatepasswordtxt);
        imgbtn3 = findViewById(R.id.updbtn);
        updatebtn4 = findViewById(R.id.updateBtn3);
        cancelbtn = findViewById(R.id.cancenBtn);
        helper = new SQLiteHelper(this, "UserDB.sqlite", null, 1);

        helper.queryData("CREATE TABLE IF NOT EXISTS USER(email VARCHAR PRIMARY KEY , name VARCHAR, phone VARCHAR, password VARCHAR, image BLOB)");
        Intent sendData2 = getIntent();
        final String getEmail2 = sendData2.getStringExtra("Email2");

        Cursor cursor = MainActivity.helper.getData(getEmail2);
        cursor.moveToFirst();
        final String email = cursor.getString(0);
        String name = cursor.getString(1);
        String phone = cursor.getString(2);
        String password = cursor.getString(3);
        imgbtn3.setMinimumWidth(1280);
        imgbtn3.setMinimumHeight(960);
        imgbtn3.setImageBitmap(helper.getImage(email));

        ed1.setText(email);
        ed2.setText(name);
        ed3.setText(phone);
        ed4.setText(password);




        updatebtn4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if((ed1.getText().toString().length() >0)&&(ed2.getText().toString().length() >0)&&(ed3.getText().toString().length() >0)&&(ed4.getText().toString().length() >0)){
                    validateInput();
                    try {
                    Boolean uniqueEmail = helper.emailCheck(getEmail2);
                    if (uniqueEmail == false) {
                        UpdateConfirmDialog(getEmail2);
                        Toast.makeText(getApplicationContext(), "Successfully Updated!", Toast.LENGTH_SHORT);
                    }else{
                        Toast.makeText(UpdateActivity.this, "The Email is Already registrated, Please try again with different Email", Toast.LENGTH_SHORT).show();
                    }
                    } catch(Exception e){
                        e.printStackTrace();
                    }}else{
                    Toast.makeText(UpdateActivity.this, "Please Fill Up the form and Upload The image", Toast.LENGTH_SHORT).show();
                }

                }
        });

        imgbtn3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                chooseImage();
            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            }
        });


    }

    private void chooseImage(){



        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();


    }

    private Boolean validateInput() {
        String emailPattern = "[a-zA-Z0-9._-]+@[gmail]+\\.+[a-z]+";
        String emailval = ed1.getText().toString();

        if(ed4.length() < 8){
            ed4.setError("Password too short !");
            return false;
        }else if(!(emailval.matches(emailPattern))){
            ed1.setError("Invalid Email,Required Email Eg. user@gmail.com ");
            return false;
        }else {
            return true;
        }
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                imgbtn3.setMinimumWidth(1280);
                imgbtn3.setMinimumHeight(960);
                imgbtn3.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri uri = data.getData();
                imgbtn3.setImageURI(uri);
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgbtn3.setImageBitmap(bitmap);
            }

        }
    }


    private void UpdateConfirmDialog(final String email) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.confirmUpdate));

        alertDialogBuilder
                .setMessage(this.getResources().getString(R.string.confirmUpdateMsge))
                .setCancelable(false)
                .setPositiveButton(this.getResources().getString(R.string.yesbutton1), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helper.updateData(
                                email,
                                ed1.getText().toString().trim(),
                                ed2.getText().toString().trim(),
                                ed3.getText().toString().trim(),
                                ed4.getText().toString().trim(),
                                imageViewToByte(imgbtn3));

                        Intent back = new Intent(UpdateActivity.this,LoginActivity.class);
                        startActivity(back);
                    }

                })
                .setNegativeButton(this.getResources().getString(R.string.nobutton1), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



}
