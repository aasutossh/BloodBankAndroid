package pn.com.aasutosh.bloodbankandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RequestBloodActivity extends AppCompatActivity implements View.OnClickListener {
    String userId;
    private Spinner spinnerBloodGroup;
    private EditText name;
    private EditText phoneNum;
    private Button location;
    private Button postMyRequest;
    private Button chooseTime;
    private Button chooseDate;
    private EditText txtDate, txtTime;
    private EditText etAmount;
    private DatabaseReference databaseReference;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String time, date;
    private double lat;
    private double lon;
    private int flag = 0;
    private TextView tvLocation;

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
        chooseTime = findViewById(R.id.btnChooseTime);
        chooseDate = findViewById(R.id.btnChooseDate);
        postMyRequest = findViewById(R.id.btnPostRequest);
        postMyRequest.setEnabled(false);
        txtTime = findViewById(R.id.etChooseTime);
        txtDate = findViewById(R.id.etChooseDate);
        tvLocation = findViewById(R.id.tvLocation);
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        chooseTime.setOnClickListener(this);
        chooseDate.setOnClickListener(this);


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChooseMyLocationMapActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        postMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeToDatabase();

            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == chooseTime) {
//            get Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            time = hourOfDay + ":" + minute;

                            txtTime.setText(time);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        } else if (view == chooseDate) {
//            get Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            txtDate.setText(date);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            if(date==null) {
                flag++;
            }
        }
    }

    private void storeToDatabase() {
        String nameText = name.getText().toString();
        String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
        String phoneText = phoneNum.getText().toString();
        String amountText = etAmount.getText().toString();

        if (!nameText.isEmpty() && phoneText.length() == 10 && phoneText.startsWith("9") && !amountText.isEmpty()) {
//            TODO: store name to db
            String id = databaseReference.push().getKey();

//            Request(String name, String phoneNum, String bloodGroup, String typeOfRequest, Date date, int quantity)
            Request request = new Request(id, nameText, bloodGroup, Integer.parseInt(amountText), phoneText, time, date, lat, lon, userId);
            assert id != null;
            Toast.makeText(this, nameText + bloodGroup + phoneText + amountText, Toast.LENGTH_SHORT).show();
            databaseReference.child(id).setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(RequestBloodActivity.this, "Request posted to database", Toast.LENGTH_SHORT).show();
                }
            });


            Toast.makeText(this, lat + " " + lon, Toast.LENGTH_SHORT).show();

        }
        if (nameText.isEmpty()) {
            name.setError("Name is required.");
        }
        if (phoneText.isEmpty()) {
            phoneNum.setError("Phone Number is required");
        }
        if (phoneText.length() != 10) {
            phoneNum.setError("Please enter 10 digit phone number.");
        }
        if (!phoneText.startsWith("9")) {
            phoneNum.setError("Please enter valid phone number");
        }
        if (amountText.isEmpty()) {
            etAmount.setError("Amounts can't be empty.");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle coordinates = data.getExtras();
                assert coordinates != null;
                lat = coordinates.getDouble("Lat");
                lon = coordinates.getDouble("Lon");
                flag++;
                if(flag == 2) {
                    postMyRequest.setEnabled(true);
                }
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
