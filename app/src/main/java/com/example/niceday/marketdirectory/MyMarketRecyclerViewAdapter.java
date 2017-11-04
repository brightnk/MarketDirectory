package com.example.niceday.marketdirectory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niceday.marketdirectory.MarketFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;



public class MyMarketRecyclerViewAdapter extends RecyclerView.Adapter<MyMarketRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Market> mValues;

    private final OnListFragmentInteractionListener mListener;

    public MyMarketRecyclerViewAdapter(ArrayList<Market> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_market, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).id));
        holder.mContentView.setText(mValues.get(position).marketName);

        String productDetails = mValues.get(position).marketDetail.products.trim();
        if(productDetails.length()>150){
            productDetails = productDetails.substring(0, 150)+"...";
        }
        if(productDetails.length()==0) holder.mProductView.setText("No product listed online for this Market");
        else holder.mProductView.setText(productDetails);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mProductView;
        public Market mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mProductView = (TextView) view.findViewById(R.id.productText);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
