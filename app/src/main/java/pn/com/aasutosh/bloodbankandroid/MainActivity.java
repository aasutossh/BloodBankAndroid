package pn.com.aasutosh.bloodbankandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    Button btnSighOut;
    Button btnRequestBlood;
    Button btnDonateBlood;
    Button btnShowMap;
    Button btnShowList;
    Button btnEditProfile;
    Button btnShowDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
        btnSighOut = findViewById(R.id.btnSignOut);
        btnDonateBlood = findViewById(R.id.btnDonateBlood);
        btnRequestBlood = findViewById(R.id.btnRequestBlood);
        btnShowMap = findViewById(R.id.btnShowMap);
        btnShowList = findViewById(R.id.btnShowList);
        btnShowDatabase = findViewById(R.id.btnShowDatabase);
        btnEditProfile = findViewById(R.id.btnEditProfile);


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
                                                       finish();
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
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });
        btnShowDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowDatabaseActivity.class));
            }
        });
    }
}
