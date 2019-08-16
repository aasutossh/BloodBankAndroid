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

public class RequestFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private DatabaseReference databaseRequests;
    Spinner spinner;
    String selectedSpinnerItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_feed);
        databaseRequests = FirebaseDatabase.getInstance().getReference().child("requests");
        databaseRequests.keepSynced(true);
        spinner = findViewById(R.id.spinnerSelectBloodGroup);
        selectedSpinnerItem = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(this);


        recyclerView = findViewById(R.id.recyclerViewRequest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedSpinnerItem = adapterView.getItemAtPosition(i).toString();
        if(selectedSpinnerItem.equals("All")) {
            FirebaseRecyclerAdapter <Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
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
        } else {
            Query q = databaseRequests.orderByChild("bloodGroup").equalTo(selectedSpinnerItem);
            FirebaseRecyclerAdapter <Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
                    (Request.class, R.layout.activity_request_list, RequestViewHolder.class, q) {
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
