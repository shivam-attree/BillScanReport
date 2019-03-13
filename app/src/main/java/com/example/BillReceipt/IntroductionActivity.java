package com.example.BillReceipt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IntroductionActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SN_PREFS";
    public static final String PREFS_KEY = "SN_PREFS_NAME";
    public static final String PREFS_KEY_MON = "SN_PREFS_MONEY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SpannableString s = new SpannableString("$napney");
        s.setSpan(new TypefaceSpan(this, "Playball.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Button save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveName();
            }
        });
    }

    public void saveName() {
        EditText nameText = (EditText) findViewById(R.id.editText_enter_name);
        String name = nameText.getText().toString();

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY, name);
        editor.commit();
        editor.putFloat(PREFS_KEY_MON, 0);
        editor.commit();

        Intent getToMain = new Intent(this, MainActivity.class);
        startActivity(getToMain);
    }
}
