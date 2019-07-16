package com.example.adminapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.adminapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {

                    try {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayList.clear();
                                //For Retrieving
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("");
                                listView = (ListView) findViewById(R.id.wifiList);
                                arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                listView.setAdapter(arrayAdapter);
                                databaseReference.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        String value = dataSnapshot.getValue().toString();
                                        //Extracting Name and Location
                                        String nam = "\0", loc = "\0";
                                        Boolean t = false;
                                        for(int i = 0; i < value.length(); i++) {
                                            if(t == true) break;
                                            if(value.charAt(i) == 'A' && value.charAt(i+1) == 'g' && value.charAt(i+2) == 'e') {
                                                for(int j = i+4; j < value.length(); j++) {
                                                    if(value.charAt(j) == ',' || value.charAt(j) == '}') {t = true; break;}
                                                    loc += value.charAt(j);
                                                }
                                            }
                                        }
                                        t = false;
                                        for(int i = 0; i < value.length(); i++) {
                                            if(t == true) break;
                                            if(value.charAt(i) == 'N' && value.charAt(i+1) == 'a' && value.charAt(i+2) == 'm' && value.charAt(i+3) == 'e') {
                                                for(int j = i+5; j < value.length(); j++) {
                                                    if(value.charAt(j) == ',' || (value.charAt(j) == '}')) {t = true; break;}
                                                    nam += value.charAt(j);
                                                }
                                            }
                                        }
                                        arrayList.add(nam + " - " + loc );
                                        arrayAdapter.notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });

                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        t.start();



    }
}
