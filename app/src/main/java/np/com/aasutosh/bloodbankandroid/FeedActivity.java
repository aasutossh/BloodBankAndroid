package np.com.aasutosh.bloodbankandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("requests");
        databaseReference.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>
                (Request.class, R.layout.request_list, RequestViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(RequestViewHolder requestViewHolder, Request request, int i) {
                requestViewHolder.setName(request.getName());
                requestViewHolder.setQuantity(request.getQuantity());
                requestViewHolder.setBloodGroup(request.getBloodGroup());
                requestViewHolder.setTypeOfRequest(request.getTypeOfRequest());
                requestViewHolder.setPhoneNum(request.getPhoneNum());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        View myView;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.myView = itemView;
        }
        public void setName(String name) {
            TextView tvName = myView.findViewById(R.id.tvRequestListName);
            tvName.setText(name);
        }
        public void setQuantity(int quantity) {
            TextView tvQuantity = myView.findViewById(R.id.tvRequestListQuantity);
            tvQuantity.setText(String.valueOf(quantity));
        }
        public void setBloodGroup(String bloodGroup) {
            TextView tvBloodGroup = myView.findViewById(R.id.tvRequestListBloodGroup);
            tvBloodGroup.setText(bloodGroup);
        }
        public void setTypeOfRequest(String typeOfRequest) {
            TextView tvTypeOfRequest = myView.findViewById(R.id.tvRequestListName);
            tvTypeOfRequest.setText(typeOfRequest);
        }
        public void setPhoneNum(String phoneNum) {
            TextView tvPhoneNum = myView.findViewById(R.id.tvRequestListPhoneNum);
            tvPhoneNum.setText(phoneNum);
        }
    }

}

