package pn.com.aasutosh.bloodbankandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends AppCompatActivity {
    EditText etFeedbackSubject;
    EditText etMessage;
    Button btnSendFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        etFeedbackSubject = findViewById(R.id.etFeedbackSubject);
        etMessage = findViewById(R.id.etFeedbackMessage);
        btnSendFeedback = findViewById(R.id.btnSendFeedbacki);


        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = etFeedbackSubject.getText().toString();
                String message = etMessage.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "aasutossh@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}
