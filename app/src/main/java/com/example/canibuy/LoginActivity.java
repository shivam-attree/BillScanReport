package com.example.canibuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText uname;
    private EditText password;
    private Button login;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private TextView reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uname = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.signin);
        progressDialog = new ProgressDialog(this);
        reg = (TextView) findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            finish();
            Intent Action;
            Action = new Intent(getApplicationContext(),MainActivity.class);

            startActivity(Action);
        }

        login.setOnClickListener(this);
        reg.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if(v==login){
            userLogin();
        }

        if(v==reg){
            finish();
            startActivity(new Intent(this,RegisterActivity.class));
        }

    }

    private void userLogin() {
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

        progressDialog.setMessage("Logining In User...");
        progressDialog.show();

        auth.signInWithEmailAndPassword(email,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                            Intent Action;
                            Action = new Intent(getApplicationContext(),MainActivity.class);

                            startActivity(Action);
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Could not Login..",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
