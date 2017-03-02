package canyons.fitness.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import canyons.fitness.R;
import canyons.fitness.models.ActivityObject;

/**
 * Created by adi on 2/23/17.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder {
    Context context;
    TextView tvSteps, tvDate, tvDistance, tvCalories;

    public RecyclerViewHolders(View itemView, Context context) {
        super(itemView);

        this.context= context;
        tvSteps = (TextView)itemView.findViewById(R.id.activity_steps_tv);
        tvDate= (TextView)itemView.findViewById(R.id.activity_date_tv);
        tvCalories= (TextView)itemView.findViewById(R.id.activity_calories_tv);
        tvDistance = (TextView)itemView.findViewById(R.id.activity_distance_tv);





    }


}
