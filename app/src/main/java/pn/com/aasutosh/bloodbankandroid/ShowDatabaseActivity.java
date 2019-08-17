package pn.com.aasutosh.bloodbankandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ShowDatabaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
    DatabaseReference databaseProfile;
    TextView tvPhone;
    private Spinner spinnerDistrict, spinnerBloodGroup;
    private String txtDistrict, txtBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_database);
        spinnerDistrict = findViewById(R.id.spinnerSelectDistrict);
        spinnerBloodGroup = findViewById(R.id.spinnerSelectBloodGroup);
//        tvPhone = findViewById(R.id.tvProfilePhoneNum);
//
//        tvPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri number = Uri.parse("tel:" + tvPhone.getText().toString());
//                Intent dialNum = new Intent(Intent.ACTION_DIAL, number);
//                startActivity(dialNum);
//            }
//        });


        txtDistrict = spinnerDistrict.getSelectedItem().toString();
        txtBloodGroup = spinnerBloodGroup.getSelectedItem().toString();

        databaseProfile = FirebaseDatabase.getInstance().getReference("profile");
        databaseProfile.keepSynced(true);

        spinnerDistrict.setOnItemSelectedListener(this);
        spinnerBloodGroup.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerViewShowDatabase);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinnerSelectDistrict) {
            txtDistrict = spinnerDistrict.getSelectedItem().toString();
        } else if (adapterView.getId() == R.id.spinnerSelectBloodGroup) {
            txtBloodGroup = spinnerBloodGroup.getSelectedItem().toString();

        }

        if (txtDistrict.equals("All Districts") && txtBloodGroup.equals("All")) {
            FirebaseRecyclerAdapter<Profile, ProfileViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Profile, ProfileViewHolder>(
                    Profile.class, R.layout.activity_show_database_list, ProfileViewHolder.class, databaseProfile
            ) {

                @Override
                protected void populateViewHolder(ProfileViewHolder profileViewHolder, final Profile profile, int i) {
                    profileViewHolder.setName(profile.getName());
                    profileViewHolder.setBloodGroup(profile.getBloodGroup());
                    profileViewHolder.setPhoneNumber(profile.getPhoneNum());
                    profileViewHolder.setDistrict(profile.getDistrict());

                    profileViewHolder.setStatus(checkStatus(profile));

                }

            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);

        } else if (txtDistrict.equals("All Districts")) {
            Query query = databaseProfile.orderByChild("bloodGroup").equalTo(txtBloodGroup);
            FirebaseRecyclerAdapter<Profile, ProfileViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Profile, ProfileViewHolder>(
                    Profile.class, R.layout.activity_show_database_list, ProfileViewHolder.class, query
            ) {
                @Override
                protected void populateViewHolder(ProfileViewHolder profileViewHolder, Profile profile, int i) {
                    profileViewHolder.setName(profile.getName());
                    profileViewHolder.setBloodGroup(profile.getBloodGroup());
                    profileViewHolder.setPhoneNumber(profile.getPhoneNum());
                    profileViewHolder.setDistrict(profile.getDistrict());
                    profileViewHolder.setStatus(checkStatus(profile));
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        } else if (txtBloodGroup.equals("All")) {
            Query query = databaseProfile.orderByChild("district").equalTo(txtDistrict);
            FirebaseRecyclerAdapter<Profile, ProfileViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Profile, ProfileViewHolder>(
                    Profile.class, R.layout.activity_show_database_list, ProfileViewHolder.class, query
            ) {
                @Override
                protected void populateViewHolder(ProfileViewHolder profileViewHolder, Profile profile, int i) {
                    profileViewHolder.setName(profile.getName());
                    profileViewHolder.setBloodGroup(profile.getBloodGroup());
                    profileViewHolder.setPhoneNumber(profile.getPhoneNum());
                    profileViewHolder.setDistrict(profile.getDistrict());
                    profileViewHolder.setStatus(checkStatus(profile));
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        } else {
            Query query = databaseProfile.orderByChild("district_bloodGroup").equalTo(txtDistrict + "_" + txtBloodGroup);
//            query."bloodGroup").equalTo(txtBloodGroup);


//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(!dataSnapshot.exists()){
//                        Toast.makeText(ShowDatabaseActivity.this, "No user found in " + txtDistrict + " having " + txtBloodGroup, Toast.LENGTH_SHORT).show();
//                        spinnerBloodGroup.setSelection(1);
//                        spinnerDistrict.setSelection(1);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
            FirebaseRecyclerAdapter<Profile, ProfileViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Profile, ProfileViewHolder>(
                    Profile.class, R.layout.activity_show_database_list, ProfileViewHolder.class, query
            ) {
                @Override
                protected void populateViewHolder(ProfileViewHolder profileViewHolder, Profile profile, int i) {
                    profileViewHolder.setName(profile.getName());
                    profileViewHolder.setBloodGroup(profile.getBloodGroup());
                    profileViewHolder.setPhoneNumber(profile.getPhoneNum());
                    profileViewHolder.setDistrict(profile.getDistrict());
                    profileViewHolder.setStatus(checkStatus(profile));
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }
    }

    private String checkStatus(Profile profile) {
//                    if the last donated date is more than 90 days ago then the person can donate
        @SuppressLint("SimpleDateFormat") SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        String inputString1 = profile.getLastDonated();
        Date date = new Date();
        String inputString2 = myFormat.format(date);
        long days = 0;
        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            assert date2 != null;
            assert date1 != null;
            long diff = date2.getTime() - date1.getTime();
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (days >= 90) {
            return "Person is available for donation";
        } else {
            return "Person is not available for donation";
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
