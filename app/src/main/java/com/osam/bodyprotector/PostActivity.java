package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private Button bt_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_layout);

        bt_delete = (Button)findViewById(R.id.bt_delete);
        bt_update = (Button)findViewById(R.id.bt_update);

        text_postmain = (TextView)findViewById(R.id.text_postmain);
        text_postname = (TextView)findViewById(R.id.text_postname);
        imgView = (ImageView)findViewById(R.id.image_thumbnail);

        Intent intent = getIntent();

        final String title = intent.getExtras().getString("postname");
        final String main = intent.getExtras().getString("postmain");
        final String uri = intent.getExtras().getString("postimage", null);
        final String uid = intent.getExtras().getString("postuser");
        final String image_name = intent.getExtras().getString("imagename");
        final String post_uid = intent.getExtras().getString("postuid");


        if(!uid.equals(firebaseAuth.getUid())){
            bt_update.setVisibility(View.INVISIBLE);
        }else{
            bt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(PostActivity.this, RewriteActivity.class);
                    intent2.putExtra("postuid", post_uid);
                    intent2.putExtra("title", title);
                    intent2.putExtra("main", main);
                    intent2.putExtra("postimage", uri);
                    intent2.putExtra("postuser", uid);
                    intent2.putExtra("imagename", image_name);
                    startActivityForResult(Intent.createChooser(intent2, "Update"), 1);
                }
            });
        }


        if(!uid.equals(firebaseAuth.getUid())){
            bt_delete.setVisibility(View.INVISIBLE);
        }else{bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(uri != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
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
                    } else{
                        Toast.makeText(PostActivity.this, "성공적으로 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        databaseReference.child("post").child(post_uid).removeValue();
                        finish();
                    }
                }
            });
        }
        text_postmain.setText(main);
        text_postname.setText(title);
        Glide.with(getBaseContext()).load(uri).into(imgView);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                text_postname.setText(data.getExtras().getString("title"));
                text_postmain.setText(data.getExtras().getString("main"));
                String img = data.getExtras().getString("postimg", null);
                if(img != null)Glide.with(getBaseContext()).load(img).into(imgView);
            }
        } catch (Exception e) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}