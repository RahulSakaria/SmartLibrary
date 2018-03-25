package library.smart.com.smartlibrary.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import library.smart.com.smartlibrary.R;

public class LoginMethod extends AppCompatActivity {

    private Button loginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_method);
        loginButton = (Button) findViewById(R.id.login_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginMethod.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginMethod.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
