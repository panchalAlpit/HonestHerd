package com.mobilefirst.honestherd.HHActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class HHLogin_activity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    AppCompatTextView txt_im_in_login,txt_title;
    List<AuthUI.IdpConfig> providers;
    int RC_SIGN_IN = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_login_activity);
        mAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        init();

    }

    private void init() {
        txt_im_in_login = findViewById(R.id.txt_im_in_login);
        txt_title = findViewById(R.id.txt_title);

        txt_im_in_login.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_title.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_im_in_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.txt_im_in_login:{

               mAuth.signInAnonymously()
                       .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()) {
                                   // Sign in success, update UI with the signed-in user's information
                                   Log.d("TAG", "signInAnonymously:success");
                                   FirebaseUser user = mAuth.getCurrentUser();
                                   Log.e("TAG", "onActivityResult: "+user.getUid());
                                   HHSharedPrefrence.SetLogin(HHLogin_activity.this,true);
                                   HHSharedPrefrence.SetJointDate(HHLogin_activity.this, Utils.getDateFromate("yyyy-MM-dd"));
                                   Intent intent = new Intent(HHLogin_activity.this, MainActivity.class);
                                   startActivity(intent);
                                   finish();
                               } else {
                                   // If sign in fails, display a message to the user.
                                   Log.w("TAG", "signInAnonymously:failure", task.getException());
                                   Toast.makeText(HHLogin_activity.this, "Authentication failed.",
                                           Toast.LENGTH_SHORT).show();
                                   HHSharedPrefrence.SetLogin(HHLogin_activity.this,false);
                               }

                               // ...
                           }
                       });

               /*startActivityForResult(
                       AuthUI.getInstance()
                               .createSignInIntentBuilder()
                               .setAvailableProviders(providers)
                               .build(),
                       RC_SIGN_IN);*/
           }
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.e("TAG", "onActivityResult: "+user.getUid());
                HHSharedPrefrence.SetLogin(HHLogin_activity.this,true);
                HHSharedPrefrence.SetJointDate(HHLogin_activity.this, Utils.getDateFromate("yyyy-MM-dd"));
                Intent intent = new Intent(HHLogin_activity.this, MainActivity.class);
                startActivity(intent);
                finish();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                HHSharedPrefrence.SetLogin(HHLogin_activity.this,false);
                Toast.makeText(HHLogin_activity.this,"Sign In failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
