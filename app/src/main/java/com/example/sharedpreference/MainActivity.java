package com.example.sharedpreference;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 5000;
    String name = "",surname = "";
    EditText et_name,et_surname;
    Button btnAddItem,btnShowItem;
    ImageView iv_img;
    ArrayList<DataModel> myList = new ArrayList<>();
    public static final int RequestPermissionCode = 7;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_surname = findViewById(R.id.et_surname);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnShowItem = findViewById(R.id.btnShowItem);
        iv_img = findViewById(R.id.iv_img);

        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckingPermissionIsEnabledOrNot()) {
                    Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                    openGallery();
                }
                else {
                    RequestMultiplePermission();
                }
            }
        });



        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString();
                surname = et_surname.getText().toString();
                if (name.matches("")) {
                    Toast.makeText(MainActivity.this, "You did not enter a Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.matches("")) {
                    Toast.makeText(MainActivity.this, "You did not enter a Surname", Toast.LENGTH_SHORT).show();
                    return;
                }

                DataModel mLog = new DataModel();
                mLog.setName(name);
                mLog.setSurname(surname);
                mLog.setImg(String.valueOf(imageUri));
                myList.add(mLog);
                saveData();
                et_name.setText("");
                et_surname.setText("");
                Glide
                        .with(MainActivity.this)
                        .load("")
                        .centerCrop()
                        .into(iv_img);

            }
        });

        btnShowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,show.class);
                startActivity(i);
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        gallery.setType("*/Images");
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            Glide
                    .with(MainActivity.this)
                    .load(imageUri)
                    .centerCrop()
                    .into(iv_img);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean ReadPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WritePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (ReadPermission && WritePermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    private boolean CheckingPermissionIsEnabledOrNot() {
        int RearPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WritePermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return RearPermissionResult == PackageManager.PERMISSION_GRANTED && WritePermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myList);
        editor.putString("courses", json);
        editor.apply();
        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }
}