package com.example.canibuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private EditText uname;
    private EditText password;
    private CardView register;
    private ProgressDialog progressDialog;

    private EditText fname;
    private EditText lname;
    private EditText cpassword;

    private TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            finish();
            Intent Action;
            Action = new Intent(getApplicationContext(),MainActivity.class);

            startActivity(Action);
        }
        progressDialog = new ProgressDialog(this);
        fname=(EditText) findViewById(R.id.editText);
        lname = (EditText) findViewById(R.id.editText1);
        uname=(EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText3);
        register = (CardView) findViewById(R.id.cardView);
        cpassword=(EditText) findViewById(R.id.editText4);
        signIn = (TextView) findViewById(R.id.sign);

        register.setOnClickListener(this);
        signIn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if(v==register){
            registerUser();
        }
        if(v==signIn){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

    }

    private void registerUser() {
        String email = uname.getText().toString().trim();
        String password1 = password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter user email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password1)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"Registered success",Toast.LENGTH_SHORT).show();
                            Intent Action;
                            Action = new Intent(getApplicationContext(),MainActivity.class);

                            startActivity(Action);

                            }
                        else{
                            Toast.makeText(RegisterActivity.this,"Could not register..",Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.dismiss();
                    }
                });
    }


}
