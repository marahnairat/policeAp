package com.example.policeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerInfoAct extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_owner_info);
//        Date currentTime = Calendar.getInstance().getTime();
        TextView name = findViewById(R.id.owner_name);
        TextView phone = findViewById(R.id.owner_phone);
        TextView is_exp = findViewById(R.id.has_lic);

        String ownerId;
        ownerId=getIntent().getStringExtra("owner_id");
//        final Timestamp[] licStartDate = new Timestamp[1];
//        final Timestamp [] licEndDate = new Timestamp[1];
        final String[] ownername = new String[1];
        final String[] ownerphone = new String[1];

        DocumentReference data = FirebaseFirestore.getInstance().collection("owner")
                .document(ownerId);
        data.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                    if (document.exists()) {
//                        licStartDate[0] = (Timestamp) document.get("start_date");
//                        licEndDate[0] = (Timestamp) document.get("end_date");
                        ownername[0] =  String.valueOf(document.get("owner_name"));
                        ownerphone[0] =  String.valueOf(document.get("owner_phone"));

//                        Date end = licEndDate[0].toDate();
//                        Date start = licStartDate[0].toDate();

//                        lic_start_date.setText(start.toString());
//                        lic_end_date.setText(end.toString());
                        name.setText(ownername[0]);
                        phone.setText(ownerphone[0]);


//                        int is_expired = end.compareTo(currentTime);
//                        if(is_expired < 0)
//                        {
//                            is_exp.setText("LICENCE IS EXPIRED ");
//                            is_exp.setBackgroundColor(Color.RED);
//                        }
//                        else
//                        {
//                            is_exp.setText("LICENCE IS VALID ");
//                            is_exp.setBackgroundColor(Color.GREEN);
//                        }


                    } else {
                        Log.d("No such document"," ");
                        is_exp.setBackgroundColor(Color.RED);
                        is_exp.setText("HAS NOT LICENCE");
                    }
                } else {
                    Log.d( "get failed with ", String.valueOf(task.getException()));
                }
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

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(OwnerInfoAct.this, LoginActivity.class));
                            finish();
                        }
                    });
            Toast.makeText(getApplicationContext(), "signout", Toast.LENGTH_SHORT).show();

        }
        return true;
    }


}