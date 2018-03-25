package library.smart.com.smartlibrary.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import library.smart.com.smartlibrary.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        emailEditText = (EditText) findViewById(R.id.enter_email_login_activity);
        passwordEditText = (EditText) findViewById(R.id.enter_password_login_activity);
        loginButton = (Button) findViewById(R.id.login_button_login_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
        displayData();
        loginButton.setOnClickListener(this);
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Failed Check Username/Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sharedPref() {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userName", emailEditText.getText().toString());
        editor.putString("passwords", passwordEditText.getText().toString());
        editor.apply();
    }

    public void displayData() {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String emailSp = sharedPref.getString("userName", "");
        String passwordSp = sharedPref.getString("passwords", "");
        emailEditText.setText(emailSp);
        passwordEditText.setText(passwordSp);

    }


    @Override
    public void onClick(View view) {

        if (view == loginButton) {
            sharedPref();
            loginUser();
        }
    }
}
