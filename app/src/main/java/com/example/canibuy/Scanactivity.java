package com.example.canibuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Scanactivity extends AppCompatActivity {

    private TextView textView;

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        textView = (TextView) findViewById(R.id.textViewAddress);
        qrScan = new IntentIntegrator(this);
    }

    public void scanQRCode(View view){
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if(result.getContents() == null){
                Toast.makeText(this,"Result not found",Toast.LENGTH_SHORT).show();
            }
            else {
                try{
                    JSONObject obj = new JSONObject(result.getContents());
                    textView.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this,result.getContents(),Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode,data);
        }
    }
}
