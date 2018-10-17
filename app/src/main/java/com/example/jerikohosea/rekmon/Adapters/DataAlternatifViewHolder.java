package com.example.jerikohosea.rekmon.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jerikohosea.rekmon.Activities.DetailTempatRentalActivity;
import com.example.jerikohosea.rekmon.R;
import com.example.jerikohosea.rekmon.ServiceAndClassObject.TempatRental;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class DataAlternatifViewHolder  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    Context context;
    ArrayList<TempatRental> items=new ArrayList<>();

    public DataAlternatifViewHolder(Context context){
        this.context=context;

    }

    public void setDataBinding(ArrayList<TempatRental> items){
        this.items=items;

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.custome_row,parent,false);
        DataAlternatifViewHolder.Item item=new DataAlternatifViewHolder.Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((DataAlternatifViewHolder.Item)holder).name.setText(items.get(position).getNama());
        ((DataAlternatifViewHolder.Item)holder).telp.setText(items.get(position).getNoTelp());
        ((DataAlternatifViewHolder.Item)holder).harga.setText("Rp. "+String.valueOf(items.get(position).getHarga()));
        Picasso.with(context)
                .load(items.get(position).getImageUrl())
                .fit()
                .centerCrop()
                .into(((Item)holder).imgView);
        ((DataAlternatifViewHolder.Item)holder).linearMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTempatRentalActivity.class);
                intent.putExtra("Tempat", Parcels.wrap(items.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class Item extends RecyclerView.ViewHolder{
        TextView name, telp, harga;
        LinearLayout linearMain;
        ImageView imgView;

        public Item(View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.itemName);
            telp=(TextView)itemView.findViewById(R.id.itemTelp);
            harga=(TextView)itemView.findViewById(R.id.itemHarga);
            imgView = (ImageView)itemView.findViewById(R.id.imgView);
            linearMain=(LinearLayout) itemView.findViewById(R.id.linearMain);
        }
    }
}
