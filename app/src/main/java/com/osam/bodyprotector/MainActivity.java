package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText et_id;
    private EditText et_pw;
    private Button bt_ok;
    private Button bt_mkid;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = (EditText)findViewById(R.id.editText_ID);
        et_pw = (EditText)findViewById(R.id.editText_pw);
        bt_ok = (Button)findViewById(R.id.bt_ok);
        bt_mkid = (Button)findViewById(R.id.bt_mkid);

        firebaseAuth = firebaseAuth.getInstance();
        bt_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = et_id.getText().toString().trim();
                String pw = et_pw.getText().toString().trim();

                if(email.isEmpty() || pw.isEmpty()){
                    Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                }
                else firebaseAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        bt_mkid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}