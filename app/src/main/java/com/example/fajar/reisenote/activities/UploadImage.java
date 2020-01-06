package com.example.fajar.reisenote.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.fajar.reisenote.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.fajar.reisenote.R.string.upload_failed;

public class UploadImage extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap = null;
    ProgressDialog pd;
    @BindView(R.id.btn_select)
    Button btnSelect;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.image_load)
    ImageView imgView;
    @BindView(R.id.back_buttonUpload)
    ImageView backButton;
    //creating reference to firebase storage
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;

    private int image;
    private static final String IMAGE_RESOURCE = "image-resource";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.upload_activity);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://reisenote1.appspot.com");


        ButterKnife.bind(this);



        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (filePath != null) {
            outState.putString(IMAGE_RESOURCE, filePath.toString());
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState.containsKey(IMAGE_RESOURCE)){
            filePath = Uri.parse(savedInstanceState.getString(IMAGE_RESOURCE));
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgView.setImageBitmap(bitmap);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                imgView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            pd.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String value = downloadUrl.toString();
                            Intent myIntent = new Intent(UploadImage.this, ReiseEditActivity.class);
                            myIntent.putExtra("url_image", value); //Optional parameters
                            UploadImage.this.startActivity(myIntent);
                            pd.dismiss();
                            Toast.makeText(UploadImage.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(UploadImage.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UploadImage.this, R.string.select_image, Toast.LENGTH_SHORT).show();
        }

    }

}
