package pn.com.aasutosh.bloodbankandroid;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DonateBloodActivity extends AppCompatActivity {
    private Spinner spinnerBloodGroup;
    private EditText name;
    private EditText phoneNum;
    private Button location;
    private Button postMyRequest;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private double lat;
    private double lon;
    private String userId;
    private Profile profile;
    private TextView tvLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);
        databaseReference = FirebaseDatabase.getInstance().getReference("donates");
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        name = findViewById(R.id.etName);
        phoneNum = findViewById(R.id.etPhoneNum);
        location = findViewById(R.id.btnLocation);
        postMyRequest = findViewById(R.id.btnPostRequest);
        postMyRequest.setEnabled(false);
        tvLocation = findViewById(R.id.tvLocation);
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userId = user.getUid();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(userId);
            getProfileData();

            postMyRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    progressBar.setVisibility(View.VISIBLE);
                    postData();
                }
            });

        } else {
            startActivity(new Intent(getApplicationContext(), FireBaseUIActivity.class));
        }
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChooseMyLocationMapActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void postData() {
        String nameTxt = name.getText().toString();
        String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
        String phone = phoneNum.getText().toString();
        String id = databaseReference.push().getKey();
        Donate donate = new Donate(id, nameTxt, phone, bloodGroup, lat, lon, userId);

        if (!nameTxt.isEmpty() && phone.length() == 10) {
            assert id != null;
            databaseReference = FirebaseDatabase.getInstance().getReference("donates");
            databaseReference.child(id).setValue(donate).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DonateBloodActivity.this, "Request Posted", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getProfileData() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(Profile.class);
                showProfileData();
            }

            private void showProfileData() {
                name.setText(profile.getName());
                phoneNum.setText(profile.getPhoneNum());
                spinnerBloodGroup.setSelection(getIndex(spinnerBloodGroup, profile.getBloodGroup()));
            }

            private int getIndex(Spinner spinner, String string) {
                for (int i = 0; i < spinner.getCount(); i++) {
                    if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)) {
                        return i;
                    }
                }
                return 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DonateBloodActivity.this, "error occurred" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle coordinates = data.getExtras();
                assert coordinates != null;
                lat = coordinates.getDouble("Lat");
                lon = coordinates.getDouble("Lon");
                postMyRequest.setEnabled(true);
                tvLocation.setText(getAddress(lat, lon));
            }
        }
    }
    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());


        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert addresses != null;
        if (addresses.size() != 0) {
            if (addresses.get(0).getLocality() != null)
                return "in " + addresses.get(0).getLocality();

        }

        return "";

    }
}
