package np.com.aasutosh.bloodbankandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FireBaseUIActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 2525;
    List<AuthUI.IdpConfig> providers;
    Button btnSighOut;
    ImageView imageView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnSighOut = findViewById(R.id.btnSignOut);
        imageView = findViewById(R.id.ivIcon);
//        imageView.bringToFront();

        btnSighOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LogOut
                AuthUI.getInstance()
                        .signOut(FireBaseUIActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       btnSighOut.setEnabled(false);
                                                       showSignInOptions();
                                                   }
                                               }
                        )
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FireBaseUIActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

//        Initialize providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
//            new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        showSignInOptions();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_blood_donation)
                        .setTheme(R.style.MyTheme)
                        .build(), REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
//                Get user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                show email on toast

                assert user != null;
                Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
                btnSighOut.setEnabled(true);
            }
            else {
                assert response != null;
                Toast.makeText(this, Objects.requireNonNull(response.getError()).getMessage(), Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }
}
