package com.example.BillReceipt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsFragment extends Fragment {

    public static final String PREFS_NAME = "SN_PREFS";
    public static final String PREFS_KEY = "SN_PREFS_NAME";
    public static final String PREFS_KEY_MON = "SN_PREFS_MONEY";
    public static final String PREFS_KEY_MON_LM = "SN_PREFS_MONEY_LM";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        Button ok = (Button) v.findViewById(R.id.button_settings_apply);
        Button delete = (Button) v.findViewById(R.id.button_settings_delete);
        final EditText nameText = (EditText) v.findViewById(R.id.edit_name);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameText.getText().toString();

                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                editor = settings.edit();
                editor.putString(PREFS_KEY, name);
                editor.commit();

                Toast.makeText(getActivity(),R.string.saved,Toast.LENGTH_SHORT).show();



            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                editor = settings.edit();
                editor.putFloat(PREFS_KEY_MON,0);
                editor.putFloat(PREFS_KEY_MON_LM,0);
                editor.commit();
                Toast.makeText(getActivity(),R.string.cleared,Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }



}
