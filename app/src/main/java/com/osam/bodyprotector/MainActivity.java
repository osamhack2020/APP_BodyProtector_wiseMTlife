package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText et_id;
    private EditText et_pw;
    private Button bt_ok;
    private Button bt_mkid;
    private CheckBox chb_memory;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = (EditText)findViewById(R.id.editText_ID);
        et_pw = (EditText)findViewById(R.id.editText_pw);
        bt_ok = (Button)findViewById(R.id.bt_ok);
        bt_mkid = (Button)findViewById(R.id.bt_mkid);
        chb_memory = (CheckBox)findViewById(R.id.chb_memory);

        SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
        if(pref.getBoolean("AutoLogin", false)){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            finish();
            startActivity(intent);
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                            DatabaseReference myRef = database.getReference().child("users").child(firebaseAuth.getUid());
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    User value = dataSnapshot.getValue(User.class);
                                    if(chb_memory.isChecked()){
                                        SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("email", value.email);
                                        editor.putString("pw", value.pw);
                                        editor.putString("name", value.username);
                                        editor.putString("uuid", value.uuid);
                                        editor.putBoolean("AutoLogin", true);
                                        editor.commit();
                                    }
                                    Toast.makeText(MainActivity.this, value.email +", " + value.username, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("HI", "Failed to read value.", error.toException());
                                }
                            });

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