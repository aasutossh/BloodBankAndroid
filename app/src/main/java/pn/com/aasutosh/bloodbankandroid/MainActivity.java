package pn.com.aasutosh.bloodbankandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

//    private static final int REQUEST_CODE = 2525;
//    List<AuthUI.IdpConfig> providers;
    Button btnSighOut;
    Button btnRequestBlood;
    Button btnDonateBlood;
    Button btnShowMap;
    Button btnShowList;
//    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//
        btnSighOut = findViewById(R.id.btnSignOut);
        btnDonateBlood = findViewById(R.id.btnDonateBlood);
        btnRequestBlood = findViewById(R.id.btnRequestBlood);
        btnShowMap = findViewById(R.id.btnShowMap);
        btnShowList = findViewById(R.id.btnShowList);


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
//                                showSignInOptions();
                                startActivity(new Intent(getApplicationContext(), FireBaseUIActivity.class));
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

        btnDonateBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DonateBloodActivity.class));
            }
        });
        btnRequestBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RequestBloodActivity.class));
            }
        });

//        btnShowMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, ShowMapActivity.class));
//            }
//        });
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(intent);
            }
        });
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShowAllMapActivity.class));
            }
        });

    }
}
