package com.osam.bodyprotector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteActivity extends AppCompatActivity {

    private EditText et_postname;
    private EditText et_postmain;

    private Button bt;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private void createNewPost(User user, String postname, String postmain){
        Post post = new Post(user, postname, postmain);

        databaseReference.child("post").child(user.uuid+System.currentTimeMillis()).setValue(post);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        et_postname = (EditText)findViewById(R.id.et_postname);
        et_postmain = (EditText)findViewById(R.id.et_postmain);

        bt = (Button)findViewById(R.id.button);


        final User user = new User();
        SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
        String name = pref.getString("name", "null");
        if(name != "null"){
            user.uuid = pref.getString("uuid", "null");
            user.email = pref.getString("email", "null");
            user.pw = pref.getString("pw", "null");
            user.username = pref.getString("name", "null");
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_main = et_postmain.getText().toString().trim();
                String post_name = et_postname.getText().toString().trim();
                if(!post_main.isEmpty() && !post_name.isEmpty()){
                    createNewPost(user,post_name, post_main);
                    finish();
                    Toast.makeText(WriteActivity.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(WriteActivity.this, "Check Template", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}