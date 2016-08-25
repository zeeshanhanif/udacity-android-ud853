package com.example.android.sunshine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> list_forcasts = new ArrayList<String>();
        list_forcasts.add("Today - Sunny -- 88 / 63");
        list_forcasts.add("Tomorrow - Foggy -- 70 / 46");
        list_forcasts.add("Wed - Cloudy -- 72 / 63");
        list_forcasts.add("Thurs - Rainy -- 64 / 51");
        list_forcasts.add("Fri - Foggy -- 70 / 46");
        list_forcasts.add("Sat - Sunny -- 76 / 68");

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(),R.layout.list_itme_forecast,R.id.list_item_forcast_textview,list_forcasts);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forcast);
        listView.setAdapter(arrayAdapter);

        return rootView;
    }

}
