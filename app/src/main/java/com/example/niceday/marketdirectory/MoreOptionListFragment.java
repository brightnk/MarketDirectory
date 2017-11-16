package com.example.niceday.marketdirectory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MoreOptionListFragment extends Fragment implements View.OnClickListener{



    private OnFragmentInteractionListener mListener;
    public ArrayList<USStates> selectedStates = new ArrayList<>();
    public String usStateString = "[" +
            "    { name: 'ALABAMA', abbreviation: 'AL'}," +
            "    { name: 'ALASKA', abbreviation: 'AK'}," +
            "    { name: 'AMERICAN SAMOA', abbreviation: 'AS'}," +
            "    { name: 'ARIZONA', abbreviation: 'AZ'}," +
            "    { name: 'ARKANSAS', abbreviation: 'AR'}," +
            "    { name: 'CALIFORNIA', abbreviation: 'CA'}," +
            "    { name: 'COLORADO', abbreviation: 'CO'}," +
            "    { name: 'CONNECTICUT', abbreviation: 'CT'}," +
            "    { name: 'DELAWARE', abbreviation: 'DE'}," +
            "    { name: 'DISTRICT OF COLUMBIA', abbreviation: 'DC'}," +
            "    { name: 'FEDERATED STATES OF MICRONESIA', abbreviation: 'FM'}," +
            "    { name: 'FLORIDA', abbreviation: 'FL'}," +
            "    { name: 'GEORGIA', abbreviation: 'GA'}," +
            "    { name: 'GUAM', abbreviation: 'GU'}, " +
            "    { name: 'HAWAII', abbreviation: 'HI'}," +
            "    { name: 'IDAHO', abbreviation: 'ID'}," +
            "    { name: 'ILLINOIS', abbreviation: 'IL'}," +
            "    { name: 'INDIANA', abbreviation: 'IN'}," +
            "    { name: 'IOWA', abbreviation: 'IA'}," +
            "    { name: 'KANSAS', abbreviation: 'KS'}," +
            "    { name: 'KENTUCKY', abbreviation: 'KY'}," +
            "    { name: 'LOUISIANA', abbreviation: 'LA'}," +
            "    { name: 'MAINE', abbreviation: 'ME'}," +
            "    { name: 'MARSHALL ISLANDS', abbreviation: 'MH'}," +
            "    { name: 'MARYLAND', abbreviation: 'MD'}," +
            "    { name: 'MASSACHUSETTS', abbreviation: 'MA'}," +
            "    { name: 'MICHIGAN', abbreviation: 'MI'}," +
            "    { name: 'MINNESOTA', abbreviation: 'MN'}," +
            "    { name: 'MISSISSIPPI', abbreviation: 'MS'}," +
            "    { name: 'MISSOURI', abbreviation: 'MO'}," +
            "    { name: 'MONTANA', abbreviation: 'MT'}," +
            "    { name: 'NEBRASKA', abbreviation: 'NE'}," +
            "    { name: 'NEVADA', abbreviation: 'NV'}," +
            "    { name: 'NEW HAMPSHIRE', abbreviation: 'NH'}," +
            "    { name: 'NEW JERSEY', abbreviation: 'NJ'}," +
            "    { name: 'NEW MEXICO', abbreviation: 'NM'}," +
            "    { name: 'NEW YORK', abbreviation: 'NY'}," +
            "    { name: 'NORTH CAROLINA', abbreviation: 'NC'}," +
            "    { name: 'NORTH DAKOTA', abbreviation: 'ND'}," +
            "    { name: 'NORTHERN MARIANA ISLANDS', abbreviation: 'MP'}," +
            "    { name: 'OHIO', abbreviation: 'OH'}," +
            "    { name: 'OKLAHOMA', abbreviation: 'OK'}," +
            "    { name: 'OREGON', abbreviation: 'OR'}," +
            "    { name: 'PALAU', abbreviation: 'PW'}," +
            "    { name: 'PENNSYLVANIA', abbreviation: 'PA'}," +
            "    { name: 'PUERTO RICO', abbreviation: 'PR'}," +
            "    { name: 'RHODE ISLAND', abbreviation: 'RI'}," +
            "    { name: 'SOUTH CAROLINA', abbreviation: 'SC'}," +
            "    { name: 'SOUTH DAKOTA', abbreviation: 'SD'}," +
            "    { name: 'TENNESSEE', abbreviation: 'TN'}," +
            "    { name: 'TEXAS', abbreviation: 'TX'}," +
            "    { name: 'UTAH', abbreviation: 'UT'}," +
            "    { name: 'VERMONT', abbreviation: 'VT'}," +
            "    { name: 'VIRGIN ISLANDS', abbreviation: 'VI'}," +
            "    { name: 'VIRGINIA', abbreviation: 'VA'}," +
            "    { name: 'WASHINGTON', abbreviation: 'WA'}," +
            "    { name: 'WEST VIRGINIA', abbreviation: 'WV'}," +
            "    { name: 'WISCONSIN', abbreviation: 'WI'}," +
            "    { name: 'WYOMING', abbreviation: 'WY' }" +
            "]";

    public ArrayList<USStates> usStates = new ArrayList<>();

    public MoreOptionListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            JSONArray statesArray = new JSONArray(usStateString);
            JSONObject element;

            USStates state;
            for(int i=0; i<statesArray.length();i++){
                element = statesArray.getJSONObject(i);
                state = new USStates();
                state.name = element.getString("name");
                state.abbr = element.getString("abbreviation");
                usStates.add(state);
            }



        }catch (Exception e){
            Log.d("JSONarray", e.getMessage());
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_option_list, container, false);
        view.findViewById(R.id.moreOptionList1).setOnClickListener(this);
        view.findViewById(R.id.moreOptionList2).setOnClickListener(this);
        view.findViewById(R.id.moreOptionList3).setOnClickListener(this);
        view.findViewById(R.id.moreOptionList4).setOnClickListener(this);
        view.findViewById(R.id.moreOptionList5).setOnClickListener(this);
        view.findViewById(R.id.moreOptionList6).setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void updateDetailFragment(ArrayList<USStates> states) {
        if (mListener != null) {
            mListener.onFragmentInteraction(states);
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
        selectedStates.clear();
        switch (v.getId()){
            case R.id.moreOptionList1:
                Log.d("listItemClicked", "A-D");
                updateMoreOptionDetailFragment('A','B','C','D');
                break;
            case R.id.moreOptionList2:
                Log.d("listItemClicked", "E-H");
                updateMoreOptionDetailFragment('E','F','G','H');
                break;
            case R.id.moreOptionList3:
                Log.d("listItemClicked", "I-L");
                updateMoreOptionDetailFragment('I','J','K','L');
                break;
            case R.id.moreOptionList4:
                Log.d("listItemClicked", "M-P");
                updateMoreOptionDetailFragment('M','N','O','P');
                break;
            case R.id.moreOptionList5:
                Log.d("listItemClicked", "Q-T");
                updateMoreOptionDetailFragment('Q','R','S','T');
                break;
            case R.id.moreOptionList6:
                Log.d("listItemClicked", "U-Z");
                updateMoreOptionDetailFragment('U','V','W','X','Y','Z');
                break;
            default:
                Log.d("listItemClicked", "no thing clicked");
                break;
        }
    }

    public void updateMoreOptionDetailFragment(char...letters){
        for (USStates state:usStates) {
            char letter = state.name.charAt(0);

            for(char key: letters){
                if(letter ==key) selectedStates.add(state);
            }
        }
        updateDetailFragment(selectedStates);
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
        void onFragmentInteraction(ArrayList<USStates> states);
    }
}
