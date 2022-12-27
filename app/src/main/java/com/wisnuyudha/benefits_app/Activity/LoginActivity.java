package com.wisnuyudha.benefits_app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wisnuyudha.benefits_app.Model.User;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername, loginPassword;
    private Button buttonLogin;
    SharedPreferences sp;
    ApiInterface mApiInterface;
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        buttonLogin = findViewById(R.id.button_login);
        toolbar = findViewById(R.id.toolbar);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Login");
        }

        loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = loginUsername.getText().toString();

                db.collection("user")
                        .whereEqualTo("username", username)
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().isEmpty()) {
                                    Toast.makeText(LoginActivity.this, "Kredensial login salah", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        User user = documentSnapshot.toObject(User.class);
                                        if (user.getPassword().equals(loginPassword.getText().toString())) {
                                            sp.edit().putString("USER_NAME", user.getNama_user()).apply();
                                            sp.edit().putString("USER_ROLE", user.getUser_role()).apply();
                                            sp.edit().putString("USER_PHOTO", user.getFoto_user()).apply();
                                            sp.edit().putBoolean("USER_LOGGED", true).apply();
                                            Toast.makeText(LoginActivity.this, "Login for " + sp.getString("USER_NAME", ""), Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this, "Kredensial login salah", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}