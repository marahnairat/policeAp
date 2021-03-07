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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class InsuranceCarAct  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_insurance_info);

        Date currentTime = Calendar.getInstance().getTime();
        TextView ins_type= findViewById(R.id.ins_typ);
        TextView ins_start_date=findViewById(R.id.ins_start_date);
        TextView ins_end_date = findViewById(R.id.ins_end_date);
        TextView is_exp = findViewById(R.id.is_ex_in);

        String insId;
        insId=getIntent().getStringExtra("insurance_id");


        final Timestamp[] insStartDate = new Timestamp[1];
        final Timestamp [] insEndDate = new Timestamp[1];
        final String [] instype = new String[1];
try {
    DocumentReference data = FirebaseFirestore.getInstance().collection("insurance")
            .document(insId);
    data.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                if (document.exists()) {
                    insStartDate[0] = (Timestamp) document.get("start_date");
                    insEndDate[0] = (Timestamp) document.get("end_date");
                    instype[0] = (String) document.get("ins_type");

                    Date end = insEndDate[0].toDate();
                    Date start = insStartDate[0].toDate();

                    ins_start_date.setText(start.toString());
                    ins_end_date.setText(end.toString());
                    ins_type.setText(instype[0]);

                    int is_expired = end.compareTo(currentTime);
                    if (is_expired < 0) {
                        is_exp.setText("INSURANCE IS EXPIRED ");
                        is_exp.setBackgroundColor(Color.parseColor("#D59C5B5B"));
                    } else {
                        is_exp.setText("INSURANCE IS VALID ");
                        is_exp.setBackgroundColor(Color.parseColor("#D55B9C6E"));
                    }

                    Log.d("field insurance start: ", start.toString());
                    Log.d("field insurance end: ", end.toString());

                } else {
                    Log.d("No such document", " ");
                    is_exp.setText("HAS NO INSURANCE ");
                    is_exp.setBackgroundColor(Color.parseColor("#D59C5B5B"));
                }
            } else {
                Log.d("get failed with ", String.valueOf(task.getException()));
            }
        }
    });


} catch (Exception e) {
    Toast.makeText(InsuranceCarAct.this,"THIS CAR HAS NO INSURANCE ",Toast.LENGTH_SHORT).show();

//    e.printStackTrace();
}


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
            startActivity(new Intent(InsuranceCarAct.this, LoginActivity.class));

        }
        return true;
    }


//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.signout) {
//
//            AuthUI.getInstance()
//                    .signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // user is now signed out
//                            startActivity(new Intent(InsuranceCarAct.this, LoginActivity.class));
//                            finish();
//                        }
//                    });
//            Toast.makeText(getApplicationContext(), "signout", Toast.LENGTH_SHORT).show();
//
//        }
//        return true;
//    }

}
