package com.ishan.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishan.nytimessearch.R;
import com.ishan.nytimessearch.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArticleRVAdapter extends RecyclerView.Adapter<ArticleRVAdapter.ViewHolder>{
    // Store a member variable for the contacts
    private List<Article> mArticles;
    private Context mContext;

    // Pass in the contact array into the constructor
    public ArticleRVAdapter(List<Article> articles,Context context) {
        mArticles = articles;
        mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticleRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleRVAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadLine());

        ImageView imageView = viewHolder.imageView;
        imageView.setImageResource(0);

        //clear out the recycled image
        imageView.setImageResource(0);

        // populate the thumbnail image
        String thumbnail = article.getThumbNail();

        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(mContext).load(thumbnail).into(imageView);
        }
    }

    public void clearData() {
        if(mArticles!=null) {
            if (mArticles.size() > 0) {
                for (int i = 0; i < mArticles.size() -1; i++) {
                    mArticles.remove(i);
                }
                notifyItemRangeRemoved(0, mArticles.size());
            }
        }
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView imageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            // Lookup view for data population
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            imageView = (ImageView) itemView.findViewById(R.id.ivImage);

        }
    }
}
