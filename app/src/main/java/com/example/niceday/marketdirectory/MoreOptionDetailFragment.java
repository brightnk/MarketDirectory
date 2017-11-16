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

    public void setupDetailView(ArrayList<USStates> states){
        TextView insertTextView;
        rootLinear.removeAllViews();
        for(USStates state: states){

            insertTextView = new TextView(getContext());
            insertTextView.setTextSize(40);
            insertTextView.setText(state.name);
            insertTextView.setTag(state);
            insertTextView.setGravity(Gravity.CENTER);
            insertTextView.setId(View.generateViewId());
            insertTextView.setOnClickListener(this);
            rootLinear.addView(insertTextView);

        }

    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        Log.d("detailfragment", ((USStates)v.getTag()).name);

        Intent downloadService = new Intent(getActivity(), DownloadService.class);
        downloadService.putExtra("SERVICETYPE", "byStateCode");
        downloadService.putExtra("StateCode", ((USStates)v.getTag()).abbr);
        getActivity().startService(downloadService);


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
        void onFragmentInteraction(Uri uri);
    }
}
