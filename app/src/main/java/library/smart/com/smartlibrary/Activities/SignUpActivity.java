package library.smart.com.smartlibrary.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import library.smart.com.smartlibrary.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private TextView nameTextView, emailTextView, passwordTextView, retypePasswordTextView, phoneNumberTextView;
    private ImageView profileImageView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String authUid;
    private ProgressDialog progressDialog;
    private static int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    public Uri uri, downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        profileImageView = findViewById(R.id.profile_image_sign_up_activity);
        nameTextView = findViewById(R.id.full_name_sign_up_activity);
        emailTextView = findViewById(R.id.enter_email_sign_up_activity);
        passwordTextView = findViewById(R.id.password_sign_up_activity);
        retypePasswordTextView = findViewById(R.id.retype_password_sign_up_activity);
        phoneNumberTextView = findViewById(R.id.phone_number_sign_up_activity);
        signUpButton = findViewById(R.id.sign_up_button_sign_up_activity);
        mStorage = FirebaseStorage.getInstance().getReference();
        signUpButton.setOnClickListener(this);
        profileImageView.setOnClickListener(this);


    }

    private void registerUser() {
        String names = nameTextView.getText().toString().trim();
        String email = emailTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString().trim();
        String retypePassword = retypePasswordTextView.getText().toString().trim();
        String numbers = phoneNumberTextView.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(retypePassword)) {
            Toast.makeText(this, "Please Retype Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(names)) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(numbers)) {
            Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals(retypePassword)) {
            progressDialog.setMessage("Registering...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SaveUserInfo();
                                uploadImageToStorage();
                                finish();
                                Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else {
                                Toast.makeText(SignUpActivity.this, "User Registration Failed", Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUpActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(SignUpActivity.this, "Retype Correct Password", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveUserInfo() {
        String mName = nameTextView.getText().toString().trim();
//        String genders = ((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getText().toString();
        String emails = emailTextView.getText().toString().trim();
        String numbers = phoneNumberTextView.getText().toString().trim();

        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(mName);
//        databaseReference.child(user.getUid()).child("gender").setValue(genders);
//        databaseReference.child(user.getUid()).child("dob").setValue(dates);
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("email").setValue(emails);
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("number").setValue(numbers);
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("photo").setValue("https://firebasestorage.googleapis.com/v0/b/traveloh-9218d.appspot.com/o/images.png?alt=media&token=cb88ae14-40e9-494f-95bf-e32fd8c30f97");
        progressDialog.dismiss();

        Toast.makeText(SignUpActivity.this, "Registered Successful", Toast.LENGTH_SHORT).show();

    }

    private void uploadImageToStorage() {
        if (uri != null) {
            final StorageReference upload = mStorage.child("Profile Photo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Photo");
            upload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    databaseReference.child(user.getUid()).child("photo").setValue(downloadUrl.toString());

                }
            });
        }
    }


    private void showImageFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == signUpButton) {
            registerUser();
        }
        if (view == profileImageView) {
            showImageFileChooser();
        }
    }

}
