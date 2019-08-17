package pn.com.aasutosh.bloodbankandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DonateFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String selectedSpinnerItem;
    private RecyclerView recyclerView;
    private DatabaseReference databaseDonates;
    DatabaseReference databaseProfile;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_feed);
        databaseDonates = FirebaseDatabase.getInstance().getReference().child("donates");
        databaseDonates.keepSynced(true);
        databaseProfile = FirebaseDatabase.getInstance().getReference().child("profile");

        databaseProfile.keepSynced(true);
        user = FirebaseAuth.getInstance().getCurrentUser();

        spinner = findViewById(R.id.spinnerSelectBloodGroup);
        selectedSpinnerItem = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerViewDonate);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedSpinnerItem = adapterView.getItemAtPosition(i).toString();
        if (selectedSpinnerItem.equals("All")) {
            FirebaseRecyclerAdapter<Donate, DonateViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donate, DonateViewHolder>
                    (Donate.class, R.layout.activity_donate_list, DonateViewHolder.class, databaseDonates) {
                @Override
                protected void populateViewHolder(DonateViewHolder donateViewHolder, Donate donate, int i) {
                    donateViewHolder.setName(donate.getName());
                    donateViewHolder.setBloodGroup(donate.getBloodGroup());
                    donateViewHolder.setPhoneNum(donate.getPhoneNum());
                    donateViewHolder.setAddress(getAddress(donate.getLat(), donate.getLng()));
                    setPostedByField(donateViewHolder, donate.getUserId());
                }

                private void setPostedByField(final DonateViewHolder donateViewHolder, String userId) {
                    Query q = databaseProfile.orderByChild("userId").equalTo(userId);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot profileDataSnapshot: dataSnapshot.getChildren()) {
                                Profile profile = profileDataSnapshot.getValue(Profile.class);
                                assert profile != null;
                                donateViewHolder.setPostedBy("Posted By: " + profile.getName());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(DonateFeedActivity.this, "Read Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onBindViewHolder(@NotNull DonateViewHolder viewHolder, final int position) {
                    super.onBindViewHolder(viewHolder, position);

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Donate d = getItem(position);
                            Uri phoneNum = Uri.parse("tel:" + d.getPhoneNum());

                            Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNum);

                            startActivity(callIntent);
                        }
                    });
                    viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            final Donate d = getItem(position);
                            if (user.getUid().equals(d.getUserId())) {
                                new AlertDialog.Builder(DonateFeedActivity.this)
                                        .setTitle("Delete entry")
                                        .setMessage("Are you sure you want to delete this entry? This process can't be undone!")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                                databaseDonates.child(d.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(DonateFeedActivity.this, "Post Deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            return false;
                        }
                    });
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        } else {
            Query q = databaseDonates.orderByChild("bloodGroup").equalTo(selectedSpinnerItem);
            FirebaseRecyclerAdapter<Donate, DonateViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donate, DonateViewHolder>
                    (Donate.class, R.layout.activity_donate_list, DonateViewHolder.class, q) {
                @Override
                protected void populateViewHolder(DonateViewHolder donateViewHolder, Donate donate, int i) {
                    donateViewHolder.setName(donate.getName());
                    donateViewHolder.setBloodGroup(donate.getBloodGroup());
                    donateViewHolder.setPhoneNum(donate.getPhoneNum());
                }


            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//        FirebaseRecyclerAdapter<Donate, DonateViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donate, DonateViewHolder>
//                (Donate.class, R.layout.activity_donate_list, DonateViewHolder.class, databaseDonates) {
//            @Override
//            protected void populateViewHolder(DonateViewHolder donateViewHolder, Donate donate, int i) {
//                donateViewHolder.setName(donate.getName());
//                donateViewHolder.setQuantity(donate.getQuantity());
//                donateViewHolder.setBloodGroup(donate.getBloodGroup());
//                donateViewHolder.setPhoneNum(donate.getPhoneNum());
//            }
//        };
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//    }
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
//            return addresses.get(0).getLocality();
            if (addresses.get(0).getLocality() != null)
                return "in " + addresses.get(0).getLocality();

        }

        return "";

    }
}
