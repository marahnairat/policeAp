package com.example.policeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class LicenseCarAct  extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_license_info);
        Date currentTime = Calendar.getInstance().getTime();
        TextView lic_start_date=findViewById(R.id.lic_start_date);
        TextView lic_end_date = findViewById(R.id.lic_end_date);
        TextView car_id = findViewById(R.id.lic_carid);
        TextView is_exp = findViewById(R.id.is_ex);

        String licId;
        licId=getIntent().getStringExtra("license_id");


        final Timestamp [] licStartDate = new Timestamp[1];
        final Timestamp [] licEndDate = new Timestamp[1];
        final String[] cartype = new String[1];
        final String[] carid = new String[1];
try {
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
                    cartype[0] = String.valueOf(document.get("car_type"));
                    carid[0] = String.valueOf(document.get("car_id"));

                    Date end = licEndDate[0].toDate();
                    Date start = licStartDate[0].toDate();

                    lic_start_date.setText(start.toString());
                    lic_end_date.setText(end.toString());
                    car_id.setText(carid[0]);


                    int is_expired = end.compareTo(currentTime);
                    if (is_expired < 0) {
                        is_exp.setText("LICENCE IS EXPIRED ");
                        is_exp.setBackgroundColor(Color.parseColor("#D59C5B5B"));
                    } else {
                        is_exp.setText("LICENCE IS VALID ");
                        is_exp.setBackgroundColor(Color.parseColor("#D55B9C6E"));
                    }

                    Log.d("field car num: ", carid[0]);
                    Log.d("field car type: ", cartype[0]);
                    Log.d("field lic start: ", String.valueOf(start.getTime()));
                    Log.d("field lic end: ", end.toString());

                } else {
                    Log.d("No such document", " ");
                    is_exp.setText("HAS NO LICENCE  ");
                    is_exp.setBackgroundColor(Color.parseColor("#D59C5B5B"));
                }
            } else {
                Log.d("get failed with ", String.valueOf(task.getException()));
            }
        }
    });


} catch (Exception e) {
    Toast.makeText(LicenseCarAct.this,"NO CAR LICENSE ",Toast.LENGTH_SHORT).show();

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
            startActivity(new Intent(LicenseCarAct.this, LoginActivity.class));

        }
        return true;
    }


}
