package np.com.aasutosh.bloodbankandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestBloodActivity extends AppCompatActivity {
    private Spinner spinnerBloodGroup;
    private EditText name;
    private EditText phoneNum;
    private Button location;
    private Button postMyRequest;
    private EditText etAmount;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        databaseReference = FirebaseDatabase.getInstance().getReference("requests");

        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        name = findViewById(R.id.etName);
        phoneNum = findViewById(R.id.etPhoneNum);
        etAmount = findViewById(R.id.etAmount);
        location = findViewById(R.id.btnLocation);
        postMyRequest = findViewById(R.id.btnPostRequest);


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO: take the user to the map screen
            }
        });
        postMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO: post the data to db
                storeToDatabase();

            }
        });



    }

    private void storeToDatabase() {
        String nameText = name.getText().toString();
        String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
        String phoneText = phoneNum.getText().toString();
        String amountText = etAmount.getText().toString();

        if(!nameText.isEmpty() && bloodGroup!=null && !phoneText.isEmpty() && phoneText.length() == 10 && phoneText.startsWith("9") && !amountText.isEmpty()){
//            TODO: store name to db
            String id = databaseReference.push().getKey();

//            Request(String name, String phoneNum, String bloodGroup, String typeOfRequest, Date date, int quantity)
            Request request = new Request(nameText, phoneText, bloodGroup, "request", Integer.parseInt(amountText));
            assert id != null;
            Toast.makeText(this, nameText + bloodGroup+phoneText+amountText, Toast.LENGTH_SHORT).show();
            databaseReference.child(id).setValue(request);
            Toast.makeText(this, "Request posted to database", Toast.LENGTH_SHORT).show();

        } else if (nameText.isEmpty()){
            name.setError("Name is required.");
        } else if(bloodGroup == null){
            Toast.makeText(this, "BloodGroup is required.", Toast.LENGTH_SHORT).show();
        } else if (phoneText.isEmpty()){
        phoneNum.setError("Phone Number is required");
        }
        else if(phoneText.length() == 10){
            phoneNum.setError("Please enter 10 digit phone number.");
        }
        else if(!phoneText.startsWith("9")){
            phoneNum.setError("Please enter valid phone number");
        }
        else if(amountText.isEmpty()) {
            etAmount.setError("Amounts can't be empty.");
        }
    }
}
