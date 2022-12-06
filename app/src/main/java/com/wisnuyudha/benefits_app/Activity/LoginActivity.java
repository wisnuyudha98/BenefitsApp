package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.Model.GetUser;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername, loginPassword;
    private Button buttonLogin;
    SharedPreferences sp;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        buttonLogin = findViewById(R.id.button_login);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = "";
                String password = "";
                username = loginUsername.getText().toString();
                password = loginPassword.getText().toString();
                Call<GetUser> UserCall = mApiInterface.getUser("get_user", username, password);
                UserCall.enqueue(new Callback<GetUser>() {
                    @Override
                    public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                        sp.edit().putString("USER_NAME", response.body().getUser().getNamaUser()).apply();
                        sp.edit().putString("USER_ROLE", response.body().getUser().getUserRole()).apply();
                        sp.edit().putString("USER_PHOTO", response.body().getUser().getFotoUser()).apply();
                        sp.edit().putBoolean("USER_LOGGED", true).apply();
                        Toast.makeText(LoginActivity.this, "Login for " + response.body().getUser().getNamaUser(), Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<GetUser> call, Throwable t) {
                        Log.d(TAG, Log.getStackTraceString(t));
                        Toast.makeText(LoginActivity.this, "X", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}