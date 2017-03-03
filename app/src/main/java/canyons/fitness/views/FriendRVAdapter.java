package canyons.fitness.views;

/**
 * Created by adi on 2/23/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import canyons.fitness.R;
import canyons.fitness.activities.HomeActivity;
import canyons.fitness.models.ActivityObject;
import canyons.fitness.models.UserDetailsObject;

public class FriendRVAdapter extends RecyclerView.Adapter<FriendRVViewHolders> {

    private List<UserDetailsObject> itemList;
    private Context context;
    private HomeActivity.clickListener listener;

    public FriendRVAdapter(Context context, List<UserDetailsObject> itemList, HomeActivity.clickListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener =listener;

    }

    @Override
    public FriendRVViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_friend_item, null);
        FriendRVViewHolders rcv = new FriendRVViewHolders(layoutView,context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(FriendRVViewHolders holder, final int position) {
        //set values on holder
        if(itemList.get(position).getSex().equals("male")) {
            holder.friendImg.setImageDrawable(context.getResources().getDrawable(R.drawable.newmale));
        }else{
            holder.friendImg.setImageDrawable(context.getResources().getDrawable(R.drawable.newfemale));
        }
        holder.tvName.setText(itemList.get(position).getName());
        holder.tvAge.setText(itemList.get(position).getAge());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

