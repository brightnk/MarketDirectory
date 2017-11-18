package com.example.niceday.marketdirectory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MoreOptionDetailFragment extends Fragment implements View.OnClickListener {



    private LinearLayout rootLinear;
    private OnFragmentInteractionListener mListener;
    private boolean showCity;
    public MoreOptionDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View _view = inflater.inflate(R.layout.fragment_more_option_detail, container, false);
        rootLinear = _view.findViewById(R.id.moreOptionRoot);


        return _view;
    }

    public <E> void setupDetailView(ArrayList<E> inputs, boolean showCity){
        TextView insertTextView;
        rootLinear.removeAllViews();
        this.showCity = showCity;


        if(inputs.size()>0) {
            for (E input : inputs) {

                insertTextView = new TextView(getContext());
                insertTextView.setTextSize(30);
                insertTextView.setGravity(Gravity.CENTER);
                insertTextView.setId(View.generateViewId());
                if (!showCity) {
                    insertTextView.setText(((USStates) input).name);
                } else {
                    insertTextView.setText(((USCities) input).name + " - " + ((USCities) input).postcode);

                }


                insertTextView.setTag(input);
                insertTextView.setOnClickListener(this);

                rootLinear.addView(insertTextView);

            }
        }else{
            insertTextView = new TextView(getContext());
            insertTextView.setTextSize(30);
            insertTextView.setGravity(Gravity.CENTER);
            insertTextView.setId(View.generateViewId());
            insertTextView.setText("There's no content under these letters");
            rootLinear.addView(insertTextView);
        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String zipcode) {
        if (mListener != null) {
            mListener.onFragmentInteraction(zipcode);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(!showCity) {
            Log.d("detailfragment", ((USStates) v.getTag()).name);

            Intent downloadService = new Intent(getActivity(), DownloadService.class);
            downloadService.putExtra("SERVICETYPE", "byStateCode");
            downloadService.putExtra("StateCode", ((USStates) v.getTag()).abbr);
            getActivity().startService(downloadService);
        }else{
            Log.d("detailfragment", ((USCities) v.getTag()).postcode);
            onButtonPressed(((USCities) v.getTag()).postcode);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String zipcode);
    }
}
