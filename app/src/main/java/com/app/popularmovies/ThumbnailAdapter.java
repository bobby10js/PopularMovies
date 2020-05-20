package com.app.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import static android.content.ContentValues.TAG;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.NumberViewHolder> {

    private final ArrayList<String[]> idList;
    private static int viewHolderCount;

    ThumbnailAdapter(ArrayList<String[]> idList){
        this.idList = idList;
        viewHolderCount = 0;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_thumbnail_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        String apiKey = ApiKey.apiKey;
        try {
            Picasso.get().load("https://image.tmdb.org/t/p/w185/" + idList.get(viewHolderCount)[1] + "?api_key=" + apiKey).into(viewHolder.imageView);
        }
        catch (Exception e){
            Log.e("Thumbnail","Image Error"+e);
        }
        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created:" + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
//        holder.bind(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }


    private static ClickListener clickListener;
    void setOnItemClickListener(ClickListener clickListener) {
        ThumbnailAdapter.clickListener = clickListener;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,View.OnLongClickListener {
        final ImageView imageView;
        NumberViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imageView = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getLayoutPosition(), v);
            return false;
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
