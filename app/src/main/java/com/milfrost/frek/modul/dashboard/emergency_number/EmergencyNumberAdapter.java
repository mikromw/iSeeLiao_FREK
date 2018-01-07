package com.milfrost.frek.modul.dashboard.emergency_number;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milfrost.frek.R;
import com.milfrost.frek.models.EmergencyNumber;

import java.util.List;

/**
 * Created by ASUS on 05/12/2017.
 */

public class EmergencyNumberAdapter extends RecyclerView.Adapter<EmergencyNumberAdapter.ViewHolder>{
    Context context;
    String chosenNumber;
    List<EmergencyNumber> emergencyNumberList;
    EmergencyNumberInterface.View numberInterface;

    public EmergencyNumberAdapter(Context context,List<EmergencyNumber> emergencyNumbers){
        this.context = context;
        emergencyNumberList = emergencyNumbers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.emergency_number_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EmergencyNumber emergencyNumber = emergencyNumberList.get(position);
        holder.categoryIcon.setImageDrawable(emergencyNumber.getDrawableIcon());
        holder.instancePhoneNumber.setText(emergencyNumber.number);
        holder.instanceName.setText(emergencyNumber.name);
        holder.phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenNumber = emergencyNumber.number;
                if(numberInterface!=null)
                    numberInterface.makePhoneCall(emergencyNumber.number);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emergencyNumberList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView phoneIcon;
        ImageView categoryIcon;
        TextView instanceName;
        TextView instancePhoneNumber;
        public ViewHolder(View view){
            super(view);
            phoneIcon = (ImageView)view.findViewById(R.id.phone_icon);
            categoryIcon = (ImageView)view.findViewById(R.id.ic_emergency_num_category);
            instanceName = (TextView)view.findViewById(R.id.instance_name);
            instancePhoneNumber = (TextView)view.findViewById(R.id.instance_number);
        }
    }
}
