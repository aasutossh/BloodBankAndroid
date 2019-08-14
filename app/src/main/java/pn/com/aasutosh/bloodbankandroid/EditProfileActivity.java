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

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etPhoneNum;
    private Spinner spinnerDistrict, spinnerBloodGroup;
    private Button btnChooseDate, btnUpdateProfile;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private Profile profile;
    private String name, phoneNum, bloodGroup, district, lastDonatedDate, userId;
    private int mYear, mDay, mMonth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etFullName);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        spinnerBloodGroup.setEnabled(false);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        btnUpdateProfile = findViewById(R.id.btnCreateProfile);
        btnUpdateProfile.setText("Update Profile");
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userId = user.getUid();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(userId);
            getProfileData();

            btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    updateData();
                }
            });

        } else {
            startActivity(new Intent(getApplicationContext(), FireBaseUIActivity.class));
        }

    }

    private void getProfileData() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(Profile.class);
                showProfileData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, "error occurred" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProfileData() {
        etName.setText(profile.getName());
        etPhoneNum.setText(profile.getPhoneNum());
        spinnerBloodGroup.setSelection(getIndex(spinnerBloodGroup, profile.getBloodGroup()));
        spinnerDistrict.setSelection(getIndex(spinnerDistrict, profile.getDistrict()));
        btnChooseDate.setText(profile.getLastDonated());
    }

    private int getIndex(@org.jetbrains.annotations.NotNull Spinner spinner, String string) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)) {
                return i;
            }
        }
        return 0;
    }

    private void updateData() {
        name = etName.getText().toString();
        phoneNum = etPhoneNum.getText().toString();
        bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
        district = spinnerDistrict.getSelectedItem().toString();
        lastDonatedDate = btnChooseDate.getText().toString();
        if (!name.isEmpty() && phoneNum.length() == 10) {
//        on successful validation
//            store to database
            Profile profile = new Profile(name, phoneNum, bloodGroup, district, lastDonatedDate, userId);
            databaseReference.setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
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
