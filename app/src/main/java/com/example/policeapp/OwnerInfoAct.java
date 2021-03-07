package com.example.policeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class OwnerInfoAct extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_owner_info);
        Date currentTime = Calendar.getInstance().getTime();
        TextView name = findViewById(R.id.owner_name);
        TextView phone = findViewById(R.id.owner_phone);
        TextView is_exp = findViewById(R.id.has_lic);

        String ownerId;
        ownerId=getIntent().getStringExtra("owner_id");
        final Timestamp[] licStartDate = new Timestamp[1];
        final Timestamp [] licEndDate = new Timestamp[1];
        final String[] ownername = new String[1];
        final String[] ownerphone = new String[1];
        final String[] ownerlic = new String[1];

        assert ownerId != null;
        DocumentReference dataa = FirebaseFirestore.getInstance().collection("owners")
                .document(ownerId);
        dataa.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                    if (document.exists()) {

                        ownername[0] =  String.valueOf(document.get("name"));
                        ownerphone[0] =  String.valueOf(document.get("phone"));
                        ownerlic[0] =  String.valueOf(document.get("lic_id"));

                        Log.d("No such document",ownerlic[0]);
                        get_owner_license(ownerlic[0]);


                    } else {
                        Log.d("No such document"," ");
                    }
                } else {
                    Log.d( "get failed with ", String.valueOf(task.getException()));
                }
            }

            private void get_owner_license(String licId) {
                assert licId != null;
                DocumentReference data = FirebaseFirestore.getInstance().collection("license")
                        .document(licId);
                data.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                            if (document.exists()) {
                                licStartDate[0] = (Timestamp) document.get("start_date");
                                licEndDate[0] = (Timestamp) document.get("end_date");

                                Date end = licEndDate[0].toDate();
                                name.setText(ownername[0]);
                                phone.setText(ownerphone[0]);


                                int is_expired = end.compareTo(currentTime);
                                if(is_expired < 0)
                                {
                                    is_exp.setText("HAS EXPIRED LICENCE  ");
                                    is_exp.setBackgroundColor(Color.parseColor("#D59C5B5B"));
                                }
                                else
                                {
                                    is_exp.setText("HAS VALIED LICENCE ");
                                    is_exp.setBackgroundColor(Color.parseColor("#D55B9C6E"));
                                }



                            } else {
                                Log.d("No such document"," ");
                                is_exp.setText("HAS NO LICENCE");
                                is_exp.setBackgroundColor(Color.parseColor("#D59C5B5B"));

                            }
                        } else {
                            Log.d( "get failed with ", String.valueOf(task.getException()));
                        }
                    }
                });



            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(OwnerInfoAct.this, LoginActivity.class));

        }
        return true;
    }



}