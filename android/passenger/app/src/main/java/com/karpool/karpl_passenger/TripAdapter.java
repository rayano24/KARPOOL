package com.karpool.karpl_passenger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private List<Trip> tripList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView origin, destination, date, time;

        public MyViewHolder(View view) {
            super(view);
            origin = (TextView) view.findViewById(R.id.origin);
            destination = (TextView) view.findViewById(R.id.destination);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);

        }
    }


    public TripAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_trip, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.origin.setText(trip.getOrigin());
        holder.destination.setText(trip.getDestination());
        holder.date.setText(trip.getDate());
        holder.time.setText(trip.getTime());

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}