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
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST = 1;

    private EditText et_postname;
    private EditText et_postmain;
    private Spinner spinner;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private Button bt;
    private Button bt_photo;
    private ImageView imgView;

    final User user = new User();
    Uri imguri = null;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private void createNewPost(User user, String postname, String postmain, String image_path, String image_name, String PostingType){
        Post post = new Post(user, postname, postmain, image_path, user.uuid+System.currentTimeMillis(), image_name, PostingType);

        databaseReference.child("post").child(user.uuid+System.currentTimeMillis()).setValue(post);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        et_postname = (EditText)findViewById(R.id.et_postname);
        et_postmain = (EditText)findViewById(R.id.et_postmain);
        spinner = (Spinner)findViewById(R.id.spinner_posting);

        bt = (Button)findViewById(R.id.bt_post);
        bt_photo = (Button)findViewById(R.id.bt_photo);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        user.uuid = pref.getString("uuid", "null");
        user.email = pref.getString("email", "null");
        user.pw = pref.getString("pw", "null");
        user.username = pref.getString("name", "null");
        user.uuid = pref.getString("uuid", "null");

        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pick_intent = new Intent(Intent.ACTION_GET_CONTENT);
                pick_intent.setType("image/*");
                startActivityForResult(Intent.createChooser(pick_intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_main = et_postmain.getText().toString().trim();
                String post_name = et_postname.getText().toString().trim();

                if(!post_main.isEmpty() && !post_name.isEmpty()){
                    if(imguri != null){
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss"); //20191024111224
                        final String fileName= sdf.format(new Date())+".png";

                        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
                        final StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);

                        UploadTask uploadTask=imgRef.putFile(imguri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String post_main = et_postmain.getText().toString().trim();
                                        String post_name = et_postname.getText().toString().trim();
                                        String PostingType = spinner.getSelectedItem().toString();

                                        createNewPost(user,post_name, post_main, uri.toString(), fileName, PostingType);
                                        finish();
                                        Toast.makeText(WriteActivity.this, "성공적으로 글을 게시했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } else{
                        String PostingType = spinner.getSelectedItem().toString();
                        createNewPost(user,post_name, post_main, null, null, PostingType);
                        finish();
                        Toast.makeText(WriteActivity.this, "성공적으로 글을 게시했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(WriteActivity.this, "제목과 본문을 적어야 합니다.", Toast.LENGTH_SHORT).show();
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

                imgView = (ImageView) findViewById(R.id.image_post);
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