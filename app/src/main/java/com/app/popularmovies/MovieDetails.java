package com.app.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.util.Objects;

public class MovieDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        final String[] header = getIntent().getStringArrayExtra("MovieId");
        Objects.requireNonNull(getSupportActionBar()).setTitle(Objects.requireNonNull(header)[2]);

        new QueryClass(new QueryClass.AsyncResponse() {
            @Override
            public void processFinish(JSONObject result) {
                try {
                    String apiKey = ApiKey.apiKey;
                    ImageView posterView = findViewById(R.id.posterView);
                    Picasso.get().load("https://image.tmdb.org/t/p/w185/" + header[1] + "?api_key=" + apiKey).into(posterView);
                    TextView durationView = findViewById(R.id.durationView);
                    durationView.setText(String.format("Running Time: %s min", result.getString("runtime")));
                    TextView yearView = findViewById(R.id.yearView);
                    yearView.setText(String.format("Release Date: %s",result.getString("release_date")));
                    TextView ratingView = findViewById(R.id.ratingView);
                    ratingView.setText(String.format("Rating: %s/10", result.getString("vote_average")));
                    TextView overViewView = findViewById(R.id.overViewView);
                    overViewView.setText(result.getString("overview"));

                }
                catch (Exception e){
                    Log.e("processFinish",e.toString());
                    finish();
                }
            }

            @Override
            public void errorSignal() {
                Toast.makeText(MovieDetails.this, "Error occurred", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).execute("https://api.themoviedb.org/3/movie/"+header[0]);

    }
}
