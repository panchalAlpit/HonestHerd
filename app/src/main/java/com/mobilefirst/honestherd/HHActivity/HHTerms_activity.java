package com.mobilefirst.honestherd.HHActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HHTerms_activity extends AppCompatActivity implements View.OnClickListener {

    AppCompatTextView txt_agree, txt_termof_use, txt_contnet_terms, txt_privacypolicy;
    AppCompatTextView txt_english,txt_hindi,txt_chinese,txt_french,txt_italian;
    AppCompatTextView txtprivacy1,txtprivacy2,txtprivacy3,byclicking;
    private FirebaseAuth mAuth;
    LinearLayout linear_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_terms_activity);
        mAuth = FirebaseAuth.getInstance();

        /*if (ContextCompat.checkSelfPermission(HHTerms_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HHTerms_activity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(HHTerms_activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(HHTerms_activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }*/

        init();
        singleTextViewSingup(txt_privacypolicy, " Privacy Policy", " and ", "Terms of Service");
    }

    private void init() {
        txt_english = findViewById(R.id.txt_english);
        txt_hindi = findViewById(R.id.txt_hindi);
        txt_chinese = findViewById(R.id.txt_chinese);
        txt_french = findViewById(R.id.txt_french);
        txt_italian = findViewById(R.id.txt_italian);
        txt_agree = findViewById(R.id.txt_agree);
        txt_termof_use = findViewById(R.id.txt_termof_use);
        txt_contnet_terms = findViewById(R.id.txt_contnet_terms);
        txt_privacypolicy = findViewById(R.id.txt_privacypolicy);
        linear_login = findViewById(R.id.linear_login);

        txtprivacy1 = findViewById(R.id.txtprivacy1);
        txtprivacy2 = findViewById(R.id.txtprivacy2);
        txtprivacy3 = findViewById(R.id.txtprivacy3);
        byclicking = findViewById(R.id.byclicking);


        txt_contnet_terms.setText(Html.fromHtml(getResources().getString(R.string.bulleted_list)));

        txt_agree.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_termof_use.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_contnet_terms.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));

        txt_english.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_hindi.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_chinese.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_french.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_italian.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));

        txtprivacy1.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txtprivacy2.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txtprivacy3.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        byclicking.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));

        linear_login.setOnClickListener(this);
    }

    private void singleTextViewSingup(TextView textView, final String userName, String status, final String songName) {

        SpannableStringBuilder spanText = new SpannableStringBuilder();
     //   spanText.append(getResources().getString(R.string.privacy_policy));
        spanText.append(userName);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // On Click Action
//                Toast.makeText(HHTerms_activity.this,userName,Toast.LENGTH_SHORT).show();
                OpenWebView();
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.white));    // you can use custom color
                textPaint.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
                textPaint.setUnderlineText(true);    // this remove the underline
            }
        }, spanText.length() - userName.length(), spanText.length(), 0);

        spanText.append(status);
        spanText.append(songName);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                // On Click Action
//                Toast.makeText(HHTerms_activity.this,songName,Toast.LENGTH_SHORT).show();
                OpenWebView();

            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.white));    // you can use custom color   // you can use custom color
                textPaint.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
                textPaint.setUnderlineText(true);    // this remove the underline
            }
        }, spanText.length() - songName.length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanText, TextView.BufferType.SPANNABLE);

    }

    public void OpenWebView() {
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse("https://honestherd.com/privacy-policy.php"));
        startActivity(viewIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_login: {
                loginAnonymously();
                break;
            }
        }
    }

    void loginAnonymously() {

        mAuth.signInAnonymously()
                .addOnCompleteListener(HHTerms_activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.e("TAG", "onActivityResult: " + user.getUid());
                            HHSharedPrefrence.SetLogin(HHTerms_activity.this, true);
                            HHSharedPrefrence.SetJointDate(HHTerms_activity.this, Utils.getDateFromate("yyyy-MM-dd"));
                            Intent intent = new Intent(HHTerms_activity.this, HHHealthStatusActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInAnonymously:failure", task.getException());
                            Toast.makeText(HHTerms_activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            HHSharedPrefrence.SetLogin(HHTerms_activity.this, false);
                        }

                        // ...
                    }
                });
    }
}
