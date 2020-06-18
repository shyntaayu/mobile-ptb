package com.example.ptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptb.model.Tambalban;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private RecyclerView rvList;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Tambalban> listTb;
    private SearchView searchView;
    private TextView tvSearchresult;
    private String valueDB;
    private String  refinedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        basicReadWrite();
    }

    public void basicReadWrite() {
        searchView = findViewById(R.id.svTb);
        tvSearchresult = findViewById(R.id.tvSearchResult);
        rvList = findViewById(R.id.rvListTb);
        rvList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        // [START write_message]
        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference().child("tambalban");

        // [END write_message]

        // [START read_message]
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                listTb = new ArrayList<>();

                valueDB = dataSnapshot.getValue().toString();
                refinedData = valueDB.substring(1, valueDB.length()-1);
                Log.d(TAG, refinedData);
                for(DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()){
                    Tambalban tambalban = tbdataSnapshot.getValue(Tambalban.class);
                    tambalban.setKey(tbdataSnapshot.getKey());

                    listTb.add(tambalban);
                }
                adapter = new ListAdapter(listTb, ListActivity.this);
                rvList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int SearchIndex = refinedData.indexOf(query);
                if(SearchIndex!=-1) {
                    String SearchResult = refinedData.substring(SearchIndex);
                    String SearchSplit[] = SearchResult.split(",");
                    tvSearchresult.setText("Result : " + SearchSplit[0]);
                    getSearch(true, SearchSplit[0]);
                }else{
                    tvSearchresult.setText("Result : Data not found");
                    Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    getSearch(false, null);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int SearchIndex = refinedData.indexOf(newText);
                if(SearchIndex!=-1) {
                    String SearchResult = refinedData.substring(SearchIndex);
                    String SearchSplit[] = SearchResult.split(",");
                    Log.d(TAG, SearchSplit.toString());
                    tvSearchresult.setText("Result : " + SearchSplit[0]);
                    getSearch(true,SearchSplit[0]);
                }else{
                    tvSearchresult.setText("Result : Data not found");
                    Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    getSearch(false, null);
                }
                return false;
            }
        });
        // [END read_message]
    }

    public void getSearch(boolean mustSearch,String a){
        if(mustSearch){
            myRef.orderByChild("nama").startAt(a).endAt("~").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Log.d(TAG+"aaaa", dataSnapshot.getValue().toString());
                        listTb = new ArrayList<>();
                        for(DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()){
                            Tambalban tambalban = tbdataSnapshot.getValue(Tambalban.class);
                            tambalban.setKey(tbdataSnapshot.getKey());

                            listTb.add(tambalban);
                        }
                        Log.d(TAG,listTb.size()+"");
                        adapter = new ListAdapter(listTb, ListActivity.this);
                        rvList.setAdapter(adapter);
                    } else {
                        Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        }else{
            listTb = new ArrayList<>();
            adapter = new ListAdapter(listTb, ListActivity.this);
            rvList.setAdapter(adapter);
        }

    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, ListActivity.class);
    }
}
