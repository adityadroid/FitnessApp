package canyons.fitness.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.utilities.Utilities;

import canyons.fitness.R;
import canyons.fitness.utilities.Settings;
import canyons.fitness.utilities.Utility;

/**
 * Created by adi on 3/3/17.
 */
public class RegisterProfileActivity extends AppCompatActivity {
    EditText etName, etSex, etAge, etHeight, etWeight;
    Button nextButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_profile);
        etName = (EditText)findViewById(R.id.register_name);
        etSex = (EditText)findViewById(R.id.register_sex);
        etAge = (EditText)findViewById(R.id.register_age);
        etHeight= (EditText)findViewById(R.id.register_height);
        etWeight = (EditText)findViewById(R.id.register_weight);
        nextButton = (Button)findViewById(R.id.bt_register);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()){
                    Explode explode = new Explode();
                    explode.setDuration(500);

                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(RegisterProfileActivity.this);
                    Intent intent = new Intent(RegisterProfileActivity.this,OTPVerifyActivity.class);
                    intent.putExtra("INTENT_PHONENUMBER",getIntent().getExtras().getString("INTENT_PHONENUMBER"));
                    intent.putExtra("INTENT_EMAIL",getIntent().getExtras().getString("INTENT_EMAIL"));
                    intent.putExtra("INTENT_PASSWORD",getIntent().getExtras().getString("INTENT_PASSWORD"));
                    intent.putExtra("INTENT_HEIGHT",etHeight.getText().toString().trim());
                    intent.putExtra("INTENT_WEIGHT",etWeight.getText().toString().trim());
                    intent.putExtra("INTENT_NAME",etName.getText().toString().trim());
                    intent.putExtra("INTENT_AGE",etAge.getText().toString().trim());
                    intent.putExtra("INTENT_SEX",etSex.getText().toString().trim());
                    startActivity(intent, oc2.toBundle());

                }else{

                    Utility.showSnack(getApplicationContext(),nextButton,Utility.FIELD_EMPTY);


                }
            }
        });

    }
    public boolean validateData(){
        if(etWeight.getText().toString().isEmpty()||
                etHeight.getText().toString().isEmpty()||
                etSex.getText().toString().isEmpty()||
                etName.getText().toString().isEmpty()||
                etAge.getText().toString().isEmpty())
            return false;
        else
            return true;

    }

}
