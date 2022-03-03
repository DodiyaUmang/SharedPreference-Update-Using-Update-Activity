package com.example.sharedpreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    ArrayList<DataModel> list;
    Context context;

    public myAdapter(ArrayList<DataModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("TAG", "position : "+list.get(position));
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_surname.setText(list.get(position).getSurname());
        Glide
                .with(context)
                .load(list.get(position).getImg())
                .centerCrop()
                .into(holder.iv_img);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,update.class);
                int pos = holder.getAdapterPosition();
                Log.d("TAG", "xcascx: "+pos);
                i.putExtra("pos",pos);
//                Log.d("TAG", "onClick: "+getItemId(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_surname;
        ImageView iv_img;
        CardView card_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_surname = itemView.findViewById(R.id.tv_surname);
            iv_img = itemView.findViewById(R.id.iv_img);
            card_view = itemView.findViewById(R.id.card_view);

        }
    }
}
