package com.example.policeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CarInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageView car_pic ;
        TextView car_id = findViewById(R.id.car_id) ;
        String carid= getIntent().getStringExtra("car_id");
        TextView car_type = findViewById(R.id.car_type);
        Button owner_info = findViewById(R.id.owner_info);
        Button lic_car_info =findViewById(R.id.lic_car_info);
        Button ins_car_info=findViewById(R.id.ins_car_info);

        final String[] cartype = new String[1];
        final String[] ins_id = new String[1];
        final String[] lic_id = new String[1];
        final String[] owner_id = new String[1];
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");

           FirebaseFirestore db = FirebaseFirestore.getInstance();
           db.collection("cars")
                   .document(carid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                       if (document.exists()) {

                           Log.d("TAG", document.getId() + " => " + document.getData());
                           cartype[0] = String.valueOf(document.get("car_type"));
                           ins_id[0] = String.valueOf(document.get("ins_id"));
                           lic_id[0] = String.valueOf(document.get("lic_id"));
                           owner_id[0] = String.valueOf(document.get("owner_id"));
                           car_id.setText(carid);
                           car_type.setText(cartype[0]);

                           Log.d("field car num: ", carid);
                           Log.d("field car type: ", cartype[0]);
                           Log.d("field ins id: ", ins_id[0]);
                           Log.d("field lic id: ", lic_id[0]);
                           Log.d("field owner id: ", owner_id[0]);

                       } else {
                           Log.d("No such document", " ");
                       }
                   } else {
                       Log.d("get failed with ", String.valueOf(task.getException()));
                   }
               }
           });


        owner_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),OwnerInfoAct.class);
                if(owner_id[0] != null)
                intent.putExtra("owner_id",owner_id[0] );

                startActivity(intent);
            }
        });
        lic_car_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),LicenseCarAct.class);
                if(lic_id[0] != null)
                    intent.putExtra("license_id", lic_id[0]);
                startActivity(intent);
            }
        });
        ins_car_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),InsuranceCarAct.class);
                if(ins_id[0] != null)
                    intent.putExtra("insurance_id",ins_id[0] );
                startActivity(intent);
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
            startActivity(new Intent(CarInfoActivity.this, LoginActivity.class));

        }
        return true;
    }




}


