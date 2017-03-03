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
import canyons.fitness.models.ActivityObject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<ActivityObject> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<ActivityObject> itemList) {
        this.itemList = itemList;
        this.context = context;

    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView,context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
       //set values on holder
        holder.tvCalories.setText(itemList.get(position).getCalories());
        holder.tvSteps.setText(itemList.get(position).getDistance());
        holder.tvDate.setText(itemList.get(position).getDate());
        holder.tvDistance.setText(itemList.get(position).getSteps());


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

