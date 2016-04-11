package com.corporatetaxi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Eyon on 11/16/2015.
 */
public class RegisterLoginActivity extends AppCompatActivity {
    Button mbtn_login, mbtn_register;
    Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        mbtn_login = (Button) findViewById(R.id.login_btn);
        mbtn_register = (Button) findViewById(R.id.register_btn);
        tf = Typeface.createFromAsset(this.getAssets(),"Montserrat-Regular.ttf");
        mbtn_login.setTypeface(tf);
        mbtn_register.setTypeface(tf);
        mbtn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mbtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLoginActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

    }


}
