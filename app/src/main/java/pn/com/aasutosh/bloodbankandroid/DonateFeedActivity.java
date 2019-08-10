package pn.com.aasutosh.bloodbankandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonateFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseDonates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_feed);
        databaseDonates = FirebaseDatabase.getInstance().getReference().child("donates");
        databaseDonates.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerViewDonate);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
                (Request.class, R.layout.activity_donate_list, RequestViewHolder.class, databaseDonates) {
            @Override
            protected void populateViewHolder(RequestViewHolder requestViewHolder, Request request, int i) {
                requestViewHolder.setName(request.getName());
                requestViewHolder.setQuantity(request.getQuantity());
                requestViewHolder.setBloodGroup(request.getBloodGroup());
                requestViewHolder.setPhoneNum(request.getPhoneNum());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
