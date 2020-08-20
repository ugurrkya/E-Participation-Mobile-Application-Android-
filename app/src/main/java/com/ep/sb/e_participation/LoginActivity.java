package com.ep.sb.e_participation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class LoginActivity extends AppCompatActivity {
    EditText userEmailEditText, userPasswordEditText;
    Button loginLayoutBtn;
    private Toolbar toolbar;
    TextView get_password;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference userDF;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("E-Participation");

        get_password = (TextView) findViewById(R.id.get_password);
        userEmailEditText = (EditText)findViewById(R.id.email);
        userPasswordEditText = (EditText)findViewById(R.id.password);
        userPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
        loginLayoutBtn = (Button) findViewById(R.id.login);


        mProgressDialog = new ProgressDialog(this);

        //firebase


        get_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //check users

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user!=null)
                {
                    final String userId = user.getUid();
                    userDF = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    userDF.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent moveToHome = new Intent(LoginActivity.this, MainActivity.class);
                                moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(moveToHome);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        };



        mAuth.addAuthStateListener(mAuthListener);




        loginLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Logging the user");
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
                loginUser();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void loginUser() {


        String userEmail, userPassword;

        userEmail = userEmailEditText.getText().toString().trim();
        userPassword = userPasswordEditText.getText().toString().trim();


        if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword))
        {

            mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        mProgressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent moveToHome = new Intent(LoginActivity.this, MainActivity.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    }else
                    {
                        Toast.makeText(LoginActivity.this, "Unable to logging user", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }else
        {
            Toast.makeText(LoginActivity.this, "Please enter user email and password", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }

    }


}
