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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText et_id;
    private EditText et_pw;
    private EditText et_pwr;
    private EditText et_name;
    private Button bt_signup;

    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private void createNewUser(String userId, String name, String email, String pw){
        User user = new User(name, email, pw);

        databaseReference.child("users").child(userId).setValue(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);
        et_pwr = (EditText)findViewById(R.id.et_pwr);
        et_name = (EditText)findViewById(R.id.et_name);
        bt_signup = (Button)findViewById(R.id.bt_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sign_name = et_name.getText().toString().trim();
                final String sign_id = et_id.getText().toString().trim();
                final String sign_pw = et_pw.getText().toString().trim();
                final String sign_pwr = et_pwr.getText().toString().trim();

                if(sign_pw.equals(sign_pwr)){
                    firebaseAuth.createUserWithEmailAndPassword(sign_id,sign_pw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                createNewUser(firebaseAuth.getUid(),sign_name,sign_id,sign_pw);
                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this, "Sign up failed : check template", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{
                    Toast.makeText(SignUpActivity.this, "Sign up failed : check password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}