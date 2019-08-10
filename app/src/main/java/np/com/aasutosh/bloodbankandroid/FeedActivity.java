package np.com.aasutosh.bloodbankandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class FeedActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnRequestFeed, btnDonateFeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        btnRequestFeed = findViewById(R.id.btnRequestFeed);
        btnDonateFeed = findViewById(R.id.btnDonateFeed);

        btnRequestFeed.setOnClickListener(this);
        btnDonateFeed.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == btnRequestFeed) {
            startActivity(new Intent(getApplicationContext(), RequestFeedActivity.class));
        }
        else if(view == btnDonateFeed) {
            startActivity(new Intent(getApplicationContext(), DonateFeedActivity.class));
        }
        else {
            Log.i("Info", "Unknown button pressed");
        }
    }
}

