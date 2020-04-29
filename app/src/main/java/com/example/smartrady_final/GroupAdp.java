package com.example.smartrady_final;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder> {

    private Activity activity;
    ArrayList<String> arrayListGroup;

    public GroupAdp(Activity activity, ArrayList<String> arrayListGroup) {
        this.activity = activity;
        this.arrayListGroup = arrayListGroup;
    }

    @NonNull
    @Override
    public GroupAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_vysledku,parent,false);
        return new GroupAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdp.ViewHolder holder, int position) {

        ArrayList<String> arrayListCasPr = new ArrayList<>();
        ArrayList<String> arrayListCasOd = new ArrayList<>();
        ArrayList<String> arrayListLinka = new ArrayList<>();
        ArrayList<String> arrayListPoZas = new ArrayList<>();
        ArrayList<String> arrayListKonZas = new ArrayList<>();

        for (int i = 1; i <= 5; i++){

            arrayListCasPr.add("18:45");
            arrayListCasOd.add("19:45");
            arrayListLinka.add("133");
            arrayListPoZas.add("Zelivskeho");
            arrayListKonZas.add("Vozovna Zizkov");





        }
        MemberAdp adapterMember = new MemberAdp(arrayListCasPr,arrayListCasOd,arrayListLinka,arrayListPoZas, arrayListKonZas);

        LinearLayoutManager layoutManagerMember = new LinearLayoutManager(activity);

        holder.jedVysl.setLayoutManager(layoutManagerMember);

        holder.jedVysl.setAdapter(adapterMember);


    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView jedVysl;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            jedVysl = itemView.findViewById(R.id.jedVysl);
        }
    }

}
