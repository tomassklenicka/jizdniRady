package com.example.smartrady_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemberAdp extends RecyclerView.Adapter<MemberAdp.ViewHolder> {

    ArrayList<String> arrayListCasPr;
    ArrayList<String> arrayListCasOd;
    ArrayList<String> arrayListLinka;
    ArrayList<String> arrayListPoZas;
    ArrayList<String> arrayListKonZas;

    public MemberAdp(ArrayList<String> arrayListCasPr,ArrayList<String> arrayListCasOd,ArrayList<String> arrayListLinka,ArrayList<String> arrayListPoZas,ArrayList<String> arrayListKonZas) {

        this.arrayListCasPr = arrayListCasPr;
        this.arrayListCasOd = arrayListCasOd;
        this.arrayListLinka = arrayListLinka;
        this.arrayListPoZas = arrayListPoZas;
        this.arrayListKonZas = arrayListKonZas;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_casti,parent,false);

        return new MemberAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.casPr.setText(arrayListCasPr.get(position));
        holder.casOd.setText(arrayListCasOd.get(position));
        holder.link.setText(arrayListLinka.get(position));
        holder.pocZas.setText(arrayListPoZas.get(position));
        holder.konZas.setText(arrayListKonZas.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayListCasPr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView casPr;
        TextView casOd;
        TextView link;
        TextView pocZas;
        TextView konZas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            casPr = itemView.findViewById(R.id.cas_pr);
            casOd = itemView.findViewById(R.id.cas_od);
            link = itemView.findViewById(R.id.linka);
            pocZas = itemView.findViewById(R.id.poc_zas);
            konZas = itemView.findViewById(R.id.kon_sta);
        }
    }
}
