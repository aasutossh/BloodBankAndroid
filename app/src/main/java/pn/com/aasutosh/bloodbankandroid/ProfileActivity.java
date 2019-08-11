package pn.com.aasutosh.bloodbankandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName, etPhoneNum;
    private Spinner spinnerBloodGroup;
    private Button btnChooseDate, btnCreateMyProfile;

    private FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etName);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        btnCreateMyProfile = findViewById(R.id.btnCreateProfile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
//            user is not logged in
//            take the user to FireBaseUIActivity
        } else {
//            user is logged in
//            check if there is a child with the userid in "profile" "table"
//            if there is already a branch with userid then directly go to MainActivity
//            else stay on this activity and let the user create the profile
//            after user fills the form store in the database with id as their userid
        }
    }
}
