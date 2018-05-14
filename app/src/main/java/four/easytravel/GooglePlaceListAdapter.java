package four.easytravel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

/**
 * Created by Nick on 2018-05-11.
 */

public class GooglePlaceListAdapter extends RecyclerView.Adapter<GooglePlaceListAdapter.ViewHolder> {

    Context mContext;
    LinearLayoutManager llm;
    ArrayList<NearbyPlace> placeList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView propertyName;
        protected TextView address;
        protected ImageView image;

        protected TextView amount;
        protected TextView currency;

        public ViewHolder(View v) {
            super(v);

            propertyName = (TextView) v.findViewById(R.id.property_name);
            address = (TextView) v.findViewById(R.id.line1);
            image = v.findViewById(R.id.imageView);

            amount = v.findViewById(R.id.amount);
            currency = v.findViewById(R.id.currency);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GooglePlaceListAdapter(Context context, LinearLayoutManager linearLayoutManager, ArrayList<NearbyPlace> placeList) {
        mContext = context;
        this.placeList = placeList;
        llm = linearLayoutManager;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public GooglePlaceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        mContext = parent.getContext();

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final GooglePlaceListAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final NearbyPlace place = placeList.get(position);

        holder.propertyName.setText(place.getName());
        holder.address.setText(place.getAddress());
        holder.amount.setText("");
        holder.currency.setText("");
        holder.image.setImageBitmap(place.getImage());


    }



    @Override
    public int getItemCount() {
        return placeList.size();
    }

}
