package com.example.myapphousecom.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapphousecom.Handlers.Payment;
import com.example.myapphousecom.R;

import java.util.ArrayList;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final String TAG = "MyRecycleViewAdapter";
    private ArrayList<Payment> mDataset;
    private ArrayList<Payment> countryList;
    private Activity activity;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecycleViewAdapter(ArrayList<Payment> myDataset, Activity activity) {

        mDataset = myDataset;
        countryList = new ArrayList<>();
//            mFilter = new CustomFilter(MyRecycleViewAdapter.this);
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        // create a new view

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylistitem, parent, false);
        MyViewHolder vh = new MyViewHolder(v, viewType);

        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(mDataset.get(position).getDate() != null)
            holder.CountryView.setText("Date: "+mDataset.get(position).getDate());
        holder.nativeName.setText("Sum: "+mDataset.get(position).getSum());
        holder.buildingNumber.setText("Building Number: "+mDataset.get(position).getBuildingNumber());
        holder.apartmentNumber.setText("Apartment Number: "+mDataset.get(position).getApartmentNumber());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView CountryView,nativeName ,buildingNumber,apartmentNumber;
        public ImageView imageFlag;

        public MyViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            itemView.setTag(this);

            //inflate your layout
            CountryView = itemView.findViewById(R.id.paymentDate);
            nativeName = itemView.findViewById(R.id.paymentSum);
            apartmentNumber = itemView.findViewById(R.id.apartment_number);
            buildingNumber = itemView.findViewById(R.id.building_number);
        }


    }

   class VHItem extends RecyclerView.ViewHolder {
     TextView title;

       public VHItem(View itemView) {
       super(itemView);
       }
   }
}