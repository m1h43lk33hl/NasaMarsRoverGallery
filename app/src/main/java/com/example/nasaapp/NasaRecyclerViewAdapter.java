package com.example.nasaapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class NasaRecyclerViewAdapter extends RecyclerView.Adapter<NasaRecyclerViewAdapter.ItemViewHolder> {

    private List<RoverItem> roverItemList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;


    public NasaRecyclerViewAdapter(Context context, List<RoverItem> roverItemList)
    {
        this.roverItemList = roverItemList;
        this.mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        // Create layoutInflater
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // Create view from inflated
        View view = inflater.inflate(R.layout.nasaroveritem, viewGroup,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {

        // Get roverItem
        final RoverItem roverItem = roverItemList.get(i);

        // Set Name
        itemViewHolder.itemListTextView.setText(mContext.getString(R.string.image_id_string) + roverItem.getImageID());

        //Download image using picasso library
        if (!TextUtils.isEmpty(roverItem.getImageSource())) {

            // Load image into imageView
            Picasso.with(mContext).load(roverItem.getImageSource())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(itemViewHolder.imageView);
        }

        View.OnClickListener listener = new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(roverItem);
            }
        };

        // Set listeners
        itemViewHolder.imageView.setOnClickListener(listener);
        itemViewHolder.itemListTextView.setOnClickListener(listener);

    }

    // Interface to overwrite onItemClickListener
    public interface OnItemClickListener {
        void onItemClick(RoverItem roverItem);
    }


    @Override
    public int getItemCount() {

        return (roverItemList!=null ? roverItemList.size() : 0);
    }

    /**
     * A method to set/update the roverItemList
     *
     * @param roverItemList
     */
    public void setRoverItemList(List<RoverItem> roverItemList)
    {
        this.roverItemList = roverItemList;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemListTextView;
        ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            // Initiate both items
            itemListTextView = (TextView) itemView.findViewById(R.id.il_tv);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    /**
     * Set the onItemClickListener
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
