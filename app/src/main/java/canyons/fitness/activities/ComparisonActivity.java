package canyons.fitness.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import canyons.fitness.R;

/**
 * Created by adi on 3/3/17.
 */
public class ComparisonActivity extends AppCompatActivity {
    ImageView profilePhoto;
    TextView nameTv, ageTv;
    TextView bmiFrTv,heightFrTv, weightFrTv;
    TextView bmiTv, heightTv, weightTv;
    String sex, name,age, bmifr, bmi, height, heightfr, weight, weightfr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comperison);
        profilePhoto = (ImageView)findViewById(R.id.Profile_image);
        nameTv = (TextView)findViewById(R.id.Profile_tv_name);
        ageTv = (TextView)findViewById(R.id.Profile_tv_age);
        bmiFrTv = (TextView)findViewById(R.id.Compeison_tv_fribmi);
        heightFrTv = (TextView)findViewById(R.id.Compeison_tv_friheight);
        weightFrTv = (TextView)findViewById(R.id.Compeison_tv_friweight);
        bmiTv = (TextView)findViewById(R.id.Comperison_tv_bmi);
        heightTv = (TextView)findViewById(R.id.Comperison_tv_height);
        weightTv = (TextView)findViewById(R.id.Comerison_tv_weight);
        name= getIntent().getExtras().getString("name");
        age= getIntent().getExtras().getString("age");
        sex= getIntent().getExtras().getString("sex");
        bmifr= getIntent().getExtras().getString("bmi1");
        bmi= getIntent().getExtras().getString("bmi2");
        heightfr= getIntent().getExtras().getString("height1");
        height= getIntent().getExtras().getString("height2");
        weightfr= getIntent().getExtras().getString("weight1");
        weight= getIntent().getExtras().getString("weight2");
        ageTv.setText(age);
        nameTv.setText(name);
        if(sex.equals("male")){
            profilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.newmale));
        }else{
            profilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.newfemale));
        }
        bmiFrTv.setText(bmifr);
        heightFrTv.setText(heightfr);
        weightFrTv.setText(weightfr);
        bmiTv.setText(bmi);
        heightTv.setText(height);
        weightTv.setText(weight);
        if((Float.parseFloat(bmi)>Float.parseFloat(bmifr))){
            bmiTv.setTextColor(getResources().getColor(R.color.green));
            bmiFrTv.setTextColor(getResources().getColor(R.color.red));
        }else {

            bmiFrTv.setTextColor(getResources().getColor(R.color.green));
            bmiTv.setTextColor(getResources().getColor(R.color.red));
        }


    }
}
