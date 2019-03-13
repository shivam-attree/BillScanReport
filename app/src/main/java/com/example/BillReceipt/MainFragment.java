package com.example.BillReceipt;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainFragment extends Fragment {


    TextView moneyThisMonth, moneyLastMonth;
    String money, lastMonth;

    public MainFragment() {
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

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        money  = setMoney();
        lastMonth = setMoneyLM();

        moneyThisMonth = (TextView) v.findViewById(R.id.text_money_this_month);
        moneyLastMonth = (TextView) v.findViewById(R.id.text_money_last_month);


        moneyThisMonth.setText(money + "€");
        moneyLastMonth.setText(lastMonth + "€");

        return v;
    }

    public String setMoney(){
        final String PREFS_NAME = "SN_PREFS";
        final String PREFS_KEY = "SN_PREFS_MONEY";

        SharedPreferences settings;
        String text;
        settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = String.format("%.2f",settings.getFloat(PREFS_KEY, 0));
        return text;


    }

    public String setMoneyLM(){
        final String PREFS_NAME = "SN_PREFS";
        final String PREFS_KEY = "SN_PREFS_MONEY_LM";

        SharedPreferences settings;
        String text;
        settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = String.format("%.2f",settings.getFloat(PREFS_KEY, 0));
        return text;


    }
}
