package com.osam.bodyprotector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RewriteActivity extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST = 2;

    private EditText et_repostname;
    private EditText et_repostmain;
    private Spinner spinner;

    private Button bt;
    private Button updatepost;
    private ImageView imgView;

    final User user = new User();
    Uri imguri = null;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private void UpdatePost(User user, String postname, String postmain, String image_path, String image_name, String postuid, String PostingType){
        Post post = new Post(user, postname, postmain, image_path, postuid, image_name, PostingType);

        databaseReference.child("post").child(postuid).setValue(post);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewrite);

        et_repostname = (EditText)findViewById(R.id.et_updatename);
        et_repostmain = (EditText)findViewById(R.id.et_updatemain);

        bt = (Button)findViewById(R.id.bt_updatepost);
        updatepost = (Button) findViewById(R.id.bt_updatephoto);

        final Intent intent = getIntent();

        final String title = intent.getExtras().getString("title");
        final String main = intent.getExtras().getString("main");
        final String postuid = intent.getExtras().getString("postuid");
        final String postimg = intent.getExtras().getString("postimage");
        final String image_name = intent.getExtras().getString("imagename");

        imgView = (ImageView)findViewById(R.id.image_repost);

        Glide.with(getBaseContext()).load(postimg).into(imgView);
        et_repostmain.setText(main);
        et_repostname.setText(title);

        SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
        if(pref.getBoolean("AutoLogin", false)){
            user.uuid = pref.getString("uuid", "null");
            user.email = pref.getString("email", "null");
            user.pw = pref.getString("pw", "null");
            user.username = pref.getString("name", "null");
            user.uuid = pref.getString("uuid", "null");
        }

        updatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imguri = null;
                Intent pick_intent = new Intent(Intent.ACTION_GET_CONTENT);
                pick_intent.setType("image/*");
                startActivityForResult(Intent.createChooser(pick_intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_main = et_repostmain.getText().toString().trim();
                String post_name = et_repostname.getText().toString().trim();

                if(!post_main.isEmpty() && !post_name.isEmpty()){
                    if(imguri != null){
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss"); //20191024111224
                        final String fileName= sdf.format(new Date())+".png";

                        final FirebaseStorage storage = FirebaseStorage.getInstance();
                        final StorageReference imgRef= storage.getReference("profileImages/"+fileName);

                        UploadTask uploadTask=imgRef.putFile(imguri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String post_main = et_repostmain.getText().toString().trim();
                                        String post_name = et_repostname.getText().toString().trim();
                                        String PostingType = spinner.getSelectedItem().toString();

                                        if(postimg != null) storage.getReference().child("profileImages/").child(image_name).delete();

                                        UpdatePost(user,post_name, post_main, uri.toString(), fileName, postuid, PostingType);
                                        Intent intent = new Intent();
                                        intent.putExtra("title",post_name);
                                        intent.putExtra("main",post_main);
                                        intent.putExtra("postimg",uri.toString());
                                        setResult(RESULT_OK,intent);
                                        finish();
                                        Toast.makeText(RewriteActivity.this, "성공적으로 글을 게시했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } else{
                        String PostingType = spinner.getSelectedItem().toString();
                        UpdatePost(user,post_name, post_main, postimg, image_name, postuid, PostingType);
                        Intent intent = new Intent();
                        intent.putExtra("title",post_name);
                        intent.putExtra("main",post_main);
                        setResult(RESULT_OK,intent);
                        finish();
                        Toast.makeText(RewriteActivity.this, "성공적으로 글을 게시했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RewriteActivity.this, "제목과 본문을 적어야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                imguri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imguri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                imgView.setImageBitmap(scaled);

            } else {
                Toast.makeText(this, "취소했습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}