package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText et_id;
    private EditText et_pw;
    private Button bt_ok;
    private Button bt_mkid;
    private CheckBox chb_memory;

    FirebaseAuth firebaseAuth;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = firebaseAuth.getInstance();

        et_id = (EditText)findViewById(R.id.editText_ID);
        et_pw = (EditText)findViewById(R.id.editText_pw);
        bt_ok = (Button)findViewById(R.id.bt_ok);
        bt_mkid = (Button)findViewById(R.id.bt_mkid);
        chb_memory = (CheckBox)findViewById(R.id.chb_memory);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String email = pref.getString("email",null);
        String pw = pref.getString("pw",null);
        if(pref.getBoolean("AutoLogin", false) && email != null && pw != null){
            firebaseAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        finish();
                        startActivity(intent);
                        myRef.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User value = dataSnapshot.getValue(User.class);
                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("email", value.email);
                                editor.putString("pw", value.pw);
                                editor.putString("name", value.username);
                                editor.putString("regeon", value.regeon);
                                editor.putString("uuid", firebaseAuth.getUid());
                                editor.putString("height", value.height);
                                editor.putString("weight", value.weight);
                                editor.putBoolean("AutoLogin", true);
                                editor.commit();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
                            }
                        });
                    }
                }
            });
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                                    User value = dataSnapshot.getValue(User.class);
                                    if(chb_memory.isChecked()){
                                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("email", value.email);
                                        editor.putString("pw", value.pw);
                                        editor.putString("name", value.username);
                                        editor.putString("regeon", value.regeon);
                                        editor.putString("uuid", firebaseAuth.getUid());
                                        editor.putString("height", value.height);
                                        editor.putString("weight", value.weight);
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