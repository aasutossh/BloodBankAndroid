package np.com.aasutosh.bloodbankandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestFeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_feed);
        databaseRequests = FirebaseDatabase.getInstance().getReference().child("requests");
        databaseRequests.keepSynced(true);


        recyclerView = findViewById(R.id.recyclerViewRequest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
                (Request.class, R.layout.activity_request_list, RequestViewHolder.class, databaseRequests) {
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
