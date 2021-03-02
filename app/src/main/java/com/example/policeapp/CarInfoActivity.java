package com.example.policeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CarInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageView car_pic ;
        TextView car_id = findViewById(R.id.car_id) ;
        String carid=getIntent().getStringExtra("car_id");
        TextView car_type = findViewById(R.id.car_type);
        Button owner_info = findViewById(R.id.owner_info);
        Button lic_car_info =findViewById(R.id.lic_car_info);
        Button ins_car_info=findViewById(R.id.ins_car_info);

        final String[] cartype = new String[1];
        final String[] ins_id = new String[1];
        final String[] lic_id = new String[1];
        final String[] owner_id = new String[1];

//        Bundle extras = getIntent().getExtras();
//        byte[] byteArray = extras.getByteArray("car_pic");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        car_pic = (ImageView) findViewById(R.id.car_pic);
//        car_pic.setImageBitmap(bmp);

        DocumentReference data = FirebaseFirestore.getInstance().collection("cars")
                .document(carid);
        data.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                    if (document.exists()) {
                        cartype[0] =  String.valueOf(document.get("car_type"));
                        ins_id[0] = String.valueOf(document.get("ins_id"));
                        lic_id[0] =  String.valueOf(document.get("lic_id"));
                        owner_id[0] = String.valueOf(document.get("owner_id"));

                        Log.d("field car num: " , carid);
                        Log.d("field car type: " ,cartype[0]);
                        Log.d("field ins id: " , ins_id[0]);
                        Log.d("field lic id: " , lic_id[0]);
                        Log.d("field owner id: " , owner_id[0]);

                    } else {
                        Log.d("No such document"," ");
                    }
                } else {
                    Log.d( "get failed with ", String.valueOf(task.getException()));
                }
            }
        });


        car_id.setText(carid);
        car_type.setText(cartype[0]);

        owner_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),OwnerInfoAct.class);
                intent.putExtra("owner_id",owner_id[0] );
                startActivity(intent);
            }
        });
        lic_car_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),LicenseCarAct.class);
                intent.putExtra("license_id", lic_id[0]);
                startActivity(intent);
            }
        });
        ins_car_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),InsuranceCarAct.class);
                intent.putExtra("insurance_id",ins_id[0] );
                startActivity(intent);
            }
        });
            }


}


