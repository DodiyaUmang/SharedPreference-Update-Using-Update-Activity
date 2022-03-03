package com.example.sharedpreference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class show extends AppCompatActivity {
    myAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<DataModel> myList = new ArrayList<>();
    Intent i;
    String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        loadData();

        i = getIntent();
        pos = i.getStringExtra("pos");


        recyclerView = findViewById(R.id.recyclerView);
        adapter = new myAdapter(myList,this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
        myList = gson.fromJson(json, type);
        if (myList == null) {
            myList = new ArrayList<>();
        }
    }
}