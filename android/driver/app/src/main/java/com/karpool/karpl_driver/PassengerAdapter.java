package com.karpool.karpl_driver;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter for the passenger class to be used for the recycler view
 */
public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.MyViewHolder> {

    private List<Passenger> passengerList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        protected TextView name, number, textUser;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number);
            textUser = view.findViewById(R.id.textUser);


        }
    }


    public PassengerAdapter(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_passenger, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Passenger passenger = passengerList.get(position);
        holder.name.setText(passenger.getName());
        holder.number.setText(passenger.getNumber());
        holder.textUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + passenger.getNumber())));


            }
        });



        }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }
}