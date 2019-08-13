package pn.com.aasutosh.bloodbankandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName, etPhoneNum;
    private Spinner spinnerBloodGroup, spinnerDistricts;
    private Button btnChooseDate, btnCreateMyProfile;

    private FirebaseUser user;
    private String userId, lastDonatedDate, name, phoneNum, bloodGroup, district;
    private int mYear, mMonth, mDay;
    private ProgressBar progressBar;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etFullName);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        btnCreateMyProfile = findViewById(R.id.btnCreateProfile);
        spinnerDistricts = findViewById(R.id.spinnerDistrict);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference("profile");

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
//            user is not logged in
//            take the user to FireBaseUIActivity
            startActivity(new Intent(ProfileActivity.this, FireBaseUIActivity.class));

        } else {
//            user is logged in
//            check if there is a child with the userid in "profile" "table"
            userId = user.getUid();
//            if(databaseReference.child(userId).exists)
            databaseReference.child("profile");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(userId).exists()) {
                        Toast.makeText(ProfileActivity.this, "child exists " + userId, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ProfileActivity.this, "The child doesn't exist", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
//                                create profile if validation is perfect
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            if there is already a branch with userid then directly go to MainActivity
//            else stay on this activity and let the user create the profile
//            after user fills the form store in the database with id as their userid

        }

        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                lastDonatedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                btnChooseDate.setText(lastDonatedDate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        btnCreateMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    storeToDatabase();
            }
        });

    }

    private void storeToDatabase() {

        name = etName.getText().toString();
        phoneNum = etPhoneNum.getText().toString();
        bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
        district = spinnerDistricts.getSelectedItem().toString();
        if (!name.isEmpty() && phoneNum.length() == 10) {
//        on successful validation
//            store to database
            Profile profile = new Profile(name, phoneNum, bloodGroup, district, lastDonatedDate);
            databaseReference.child(userId).setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileActivity.this, "Profile created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        }
        if (name.isEmpty())
            etName.setError("Name is required");
        if (!phoneNum.startsWith("9"))
            etPhoneNum.setError("Please enter valid phone number");


    }
}

