package pn.com.aasutosh.bloodbankandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

public class RequestFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    String selectedSpinnerItem;
    private RecyclerView recyclerView;
    private DatabaseReference databaseRequests;
    DatabaseReference databaseProfile;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_feed);
        databaseRequests = FirebaseDatabase.getInstance().getReference().child("requests");
        databaseRequests.keepSynced(true);
        databaseProfile = FirebaseDatabase.getInstance().getReference().child("profile");
        databaseProfile.keepSynced(true);
        spinner = findViewById(R.id.spinnerSelectBloodGroup);
        selectedSpinnerItem = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(this);


        recyclerView = findViewById(R.id.recyclerViewRequest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {
        selectedSpinnerItem = adapterView.getItemAtPosition(i).toString();
        if (selectedSpinnerItem.equals("All")) {
            FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
                    (Request.class, R.layout.activity_request_list, RequestViewHolder.class, databaseRequests) {
                @Override
                protected void populateViewHolder(RequestViewHolder requestViewHolder, Request request, int i) {
                    requestViewHolder.setName(request.getName());
                    requestViewHolder.setQuantity(request.getQuantity());
                    requestViewHolder.setBloodGroup(request.getBloodGroup());
                    requestViewHolder.setPhoneNum(request.getPhoneNum());
                    requestViewHolder.setAddress(getAddress(request.getLat(), request.getLon()));
                    requestViewHolder.setDateAddress(("On " + request.getDate() +" at " +request.getTime()));
                    setPostedByField(requestViewHolder, request.getUserId());
                }




                //
                @Override
                public void onBindViewHolder(@NotNull RequestViewHolder viewHolder, final int position) {
                    super.onBindViewHolder(viewHolder, position);

                    viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Request r = getItem(position);
                            Toast.makeText(RequestFeedActivity.this, "clicked"+position+r.getKey(), Toast.LENGTH_SHORT).show();
                            Uri phoneNum = Uri.parse("tel:" + r.getPhoneNum());
                            Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNum);
                            startActivity(callIntent);
                        }
                    });
                    viewHolder.myView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Request r = getItem(position);
                            if (user.getUid().equals(r.getUserId())) {
                                //show dialog confirming to delete
                                new AlertDialog.Builder(RequestFeedActivity.this)
                                        .setTitle("Delete entry")
                                        .setMessage("Are you sure you want to delete this entry?")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                Toast.makeText(RequestFeedActivity.this, "You are not allowed to delete this post", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    });
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        } else {
            Query q = databaseRequests.orderByChild("bloodGroup").equalTo(selectedSpinnerItem);
            FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
                    (Request.class, R.layout.activity_request_list, RequestViewHolder.class, q) {
                @Override
                protected void populateViewHolder(RequestViewHolder requestViewHolder, Request request, int i) {
                    requestViewHolder.setName(request.getName());
                    requestViewHolder.setQuantity(request.getQuantity());
                    requestViewHolder.setBloodGroup(request.getBloodGroup());
                    requestViewHolder.setDateAddress(("On " + request.getDate() +" at " +request.getTime()));
                    requestViewHolder.setAddress(getAddress(request.getLat(), request.getLon()));
                    requestViewHolder.setPhoneNum(request.getPhoneNum());
                    setPostedByField(requestViewHolder, request.getUserId());

                }


            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
    private void setPostedByField(final RequestViewHolder requestViewHolder, String userId) {
        Query q = databaseProfile.orderByChild("userId").equalTo(userId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot profileDataSnapshot: dataSnapshot.getChildren()) {
                    Profile profile = profileDataSnapshot.getValue(Profile.class);
                    assert profile != null;
                    requestViewHolder.setPostedBy("Posted By: " + profile.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RequestFeedActivity.this, "Read Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
