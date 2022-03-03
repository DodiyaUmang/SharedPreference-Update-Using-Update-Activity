package com.example.sharedpreference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class update extends AppCompatActivity {
    EditText et_name_up, et_surname_up;
    Button btnUpdateItem;
    ImageView iv_img_up;
    String newName = "", newSurname = "";
    ArrayList<DataModel> myList = new ArrayList<>();
    Intent i;
    int pos;
    private static final int PICK = 5000;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        loadDate();
        i = getIntent();
        pos = i.getIntExtra("pos", 0);
        et_name_up = findViewById(R.id.et_name_up);
        et_surname_up = findViewById(R.id.et_surname_up);
        iv_img_up = findViewById(R.id.iv_img_up);
        btnUpdateItem = findViewById(R.id.btnUpdateItem);

        iv_img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = et_name_up.getText().toString();
                newSurname = et_surname_up.getText().toString();
                Glide
                        .with(update.this)
                        .load(myList.get(pos))
                        .centerCrop()
                        .into(iv_img_up);

                myList.get(pos).setName(newName);
                myList.get(pos).setSurname(newSurname);
                myList.get(pos).setImg(String.valueOf(imageUri));

                saveData();
                Toast.makeText(update.this, "Update Done", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(update.this,show.class);
                startActivity(i);
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        gallery.setType("*/Images");
        startActivityForResult(gallery,PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK){
            imageUri = data.getData();
            Glide
                    .with(update.this)
                    .load(imageUri)
                    .centerCrop()
                    .into(iv_img_up);
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myList);
        editor.putString("courses", json);
        editor.apply();
//        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    private void loadDate() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<DataModel>>() {
        }.getType();
        myList = gson.fromJson(json, type);
        if (myList == null) {
            myList = new ArrayList<>();
        }
    }
}