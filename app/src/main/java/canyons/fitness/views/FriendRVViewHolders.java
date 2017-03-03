package canyons.fitness.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

public class FriendRVViewHolders extends RecyclerView.ViewHolder {
    Context context;
    TextView tvName, tvAge;
    ImageView friendImg;
    CardView cardView;
    public FriendRVViewHolders(View itemView, Context context) {
        super(itemView);

        this.context= context;
            tvName =(TextView) itemView.findViewById(R.id.friend_name);
            tvAge = (TextView)itemView.findViewById(R.id.friend_age);
            friendImg = (ImageView)itemView.findViewById(R.id.friend_thumbnail);

            cardView = (CardView)itemView.findViewById(R.id.card_view);



    }


}
