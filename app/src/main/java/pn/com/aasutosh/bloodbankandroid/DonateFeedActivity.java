package pn.com.aasutosh.bloodbankandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DonateFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private RecyclerView recyclerView;
    private DatabaseReference databaseDonates;
    Spinner spinner;
    String selectedSpinnerItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_feed);
        databaseDonates = FirebaseDatabase.getInstance().getReference().child("donates");
        databaseDonates.keepSynced(true);
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
        if(selectedSpinnerItem.equals("All")) {
            FirebaseRecyclerAdapter <Donate, DonateViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donate, DonateViewHolder>
                    (Donate.class, R.layout.activity_donate_list, DonateViewHolder.class, databaseDonates) {
                @Override
                protected void populateViewHolder(DonateViewHolder donateViewHolder, Donate donate, int i) {
                    donateViewHolder.setName(donate.getName());
                    donateViewHolder.setBloodGroup(donate.getBloodGroup());
                    donateViewHolder.setPhoneNum(donate.getPhoneNum());
                }


            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        } else {
            Query q = databaseDonates.orderByChild("bloodGroup").equalTo(selectedSpinnerItem);
            FirebaseRecyclerAdapter <Donate, DonateViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donate, DonateViewHolder>
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
}
