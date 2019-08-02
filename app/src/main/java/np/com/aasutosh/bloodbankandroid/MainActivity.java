package np.com.aasutosh.bloodbankandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 2525;
    List<AuthUI.IdpConfig> providers;
    Button btnSighOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSighOut = findViewById(R.id.btnSignOut);

        btnSighOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LogOut
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
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
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
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

                try {
                    Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException n) {
                    Toast.makeText(this, n.getMessage(), Toast.LENGTH_SHORT).show();
                }
                btnSighOut.setEnabled(true);

            }
            else {
                try {
                    Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException n) {
                    Toast.makeText(this, n.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
