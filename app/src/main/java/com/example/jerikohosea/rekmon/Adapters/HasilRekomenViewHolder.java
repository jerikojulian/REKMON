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

public class HasilRekomenViewHolder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private double max1=Integer.MAX_VALUE,max2=Integer.MAX_VALUE,max3=Integer.MAX_VALUE,max4=Integer.MAX_VALUE,
            min1=Integer.MIN_VALUE,min2=Integer.MIN_VALUE,min3=Integer.MIN_VALUE,min4=Integer.MIN_VALUE;
    View row;
    Context context;
    private int[]urutan;
    private double[]urutanJarak=new double[5], urutanJarakFix=new double[5];
    ArrayList<TempatRental> items=new ArrayList<>();
    ArrayList<TempatRental> terurut= new ArrayList<>();

    public HasilRekomenViewHolder(Context context){
        this.context=context;
        if (items.size()<3){
            items.add(0,new TempatRental(0,0,0,"kosong",0,0,0));
            items.add(1,new TempatRental(0,0,0,"kosong",0,0,0));
            items.add(2,new TempatRental(0,0,0,"kosong",0,0,0));
            terurut.add(0,new TempatRental(0,0,0,"kosong",0,0,0));
            terurut.add(1,new TempatRental(0,0,0,"kosong",0,0,0));
            terurut.add(2,new TempatRental(0,0,0,"kosong",0,0,0));
        }

    }

    public void setDataBinding(ArrayList<TempatRental> items,int[] urutan,double[] urutanJarak,double[]filter, boolean[]filterTukar){
        this.items=items;
        this.urutan=urutan;
        this.urutanJarak=urutanJarak;
        this.urutanJarakFix=new double[items.size()];
        terurut.clear();
        if(urutan[2]==0){
        }else{
            if (filterTukar[0]){
                max1=filter[0];
                min1=Integer.MIN_VALUE;
            }
            if (!filterTukar[0]){
                max1=Integer.MAX_VALUE;
                min1=filter[0];
            }
            if (filterTukar[1]){
                max2=filter[1];
                min2=Integer.MIN_VALUE;
            }
            if (!filterTukar[1]){
                max2=Integer.MAX_VALUE;
                min2=filter[1];
            }
            if (filterTukar[2]){
                max3=filter[2];
                min3=Integer.MIN_VALUE;
            }
            if (!filterTukar[2]) {
                max3=Integer.MAX_VALUE;
                min3=filter[2];
            }
            if (filterTukar[3]){
                max4=filter[3];
                min4=Integer.MIN_VALUE;
            }
            if (!filterTukar[3]){
                max4=Integer.MAX_VALUE;
                min4=filter[3];
            }


            int index=0;
            for (int i = 0; i < urutan.length; i++) {
                for (int j = 0; j < items.size(); j++) {
                    if (urutan[i] == items.get(j).getId()) {
                        int harga = items.get(j).getHarga();
                        int populer = items.get(j).getPopuleritas();
                        double rating = items.get(j).getRating();
                        double jarak = urutanJarak[i];
                        if (((min1 <= harga) && (harga <= max1)) && ((min2 <= jarak) && (jarak <= max2)) && ((min3 <= populer) && (populer <= max3)) && ((min4 <= rating) && (rating <= max4))) {

                            urutanJarakFix[index]=urutanJarak[i];
                            terurut.add(index,items.get(j));
                            index++;
                        }
                    }
                }
            }

        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        row=inflater.inflate(R.layout.custome_row,parent,false);
        Item item=new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((Item)holder).name.setText(terurut.get(position).getNama());
        ((Item)holder).telp.setText("Rating ="+terurut.get(position).getRating()+".Populeritas ="+terurut.get(position).getPopuleritas()+".Jarak ="+urutanJarakFix[position]);
        ((Item)holder).harga.setText("Rp. "+terurut.get(position).getHarga());
        Picasso.with(context)
                .load(terurut.get(position).getImageUrl())
                .fit()
                .centerCrop()
                .into(((Item)holder).imgView);
        ((Item)holder).linearMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailTempatRentalActivity.class);
                    intent.putExtra("Tempat", Parcels.wrap(terurut.get(position)));
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return terurut.size();
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
