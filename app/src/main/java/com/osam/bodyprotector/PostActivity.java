package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private TextView text_postname;
    private TextView text_postmain;
    private ImageView imgView;
    private Button bt_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_layout);

        bt_delete = (Button)findViewById(R.id.bt_delete);

        text_postmain = (TextView)findViewById(R.id.text_postmain);
        text_postname = (TextView)findViewById(R.id.text_postname);
        imgView = (ImageView)findViewById(R.id.image_thumbnail);

        Intent intent = getIntent();

        String title = intent.getExtras().getString("postname");
        String main = intent.getExtras().getString("postmain");
        String uri = intent.getExtras().getString("postimage");
        String uid = intent.getExtras().getString("postuser");
        final String image_name = intent.getExtras().getString("imagename");
        final String post_uid = intent.getExtras().getString("postuid");


        if(!uid.equals(firebaseAuth.getUid())){
            bt_delete.setVisibility(View.INVISIBLE);
        }else{
            bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseStorage storage= FirebaseStorage.getInstance();
                    storage.getReference().child("profileImages/").child(image_name).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PostActivity.this, "성공적으로 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostActivity.this, "삭제하는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    databaseReference.child("post").child(post_uid).removeValue();
                    finish();
                }
            });
        }
        text_postmain.setText(main);
        text_postname.setText(title);
        Glide.with(getBaseContext()).load(uri).into(imgView);

    }
}