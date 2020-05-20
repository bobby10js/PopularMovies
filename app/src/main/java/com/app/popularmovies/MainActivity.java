package com.app.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity    {
    String url = "http://api.themoviedb.org/3/movie/popular";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//       FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });

    generateRecyclerView(url);
    }

    void generateRecyclerView (String url){
        final ArrayList <String[]> idList = new ArrayList<>();
        QueryClass mainJson = new QueryClass(new QueryClass.AsyncResponse(){

            @Override
            public void processFinish(JSONObject result){
                try {
                    JSONArray movies = result.getJSONArray("results");

                    for (int i=0;i<movies.length();i++){
                        JSONObject data = movies.getJSONObject(i);
                        idList.add(new String[]{data.getString("id"), data.getString("poster_path"), data.getString("original_title")});
                    }

                    final RecyclerView thumbnailRecyclerView =  findViewById(R.id.rv_numbers);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
                    thumbnailRecyclerView.setLayoutManager(gridLayoutManager);
                    thumbnailRecyclerView.setHasFixedSize(true);
                    ThumbnailAdapter mAdapter = new ThumbnailAdapter(idList);
                    thumbnailRecyclerView.setAdapter(mAdapter);
                    Log.i("processFinish",idList.toString());

//                    thumbnailRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//
//                            if (!recyclerView.canScrollVertically(1)) {
//                                Toast.makeText(MainActivity.this, "Last", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
                    mAdapter.setOnItemClickListener(new ThumbnailAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent intent = new Intent(getApplicationContext(),MovieDetails.class);
                            intent.putExtra ("MovieId",idList.get(position));
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {
//                            Toast.makeText(t, "longclick", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("processFinish",e.toString());
                }
            }

            @Override
            public void errorSignal() {
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
        mainJson.execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            generateRecyclerView(url);
            return true;
        }
        else if(id == R.id.rating){
            url = "http://api.themoviedb.org/3/movie/top_rated";
            generateRecyclerView(url);
            return true;
        }
        else if(id == R.id.popularity){
            url = "http://api.themoviedb.org/3/movie/popular";
            generateRecyclerView(url);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
