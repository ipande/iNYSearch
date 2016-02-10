package com.ishan.nytimessearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishan.nytimessearch.R;
import com.ishan.nytimessearch.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article>{
    public ArticleArrayAdapter(Context context, List<Article> articleList) {
        super(context, 0,articleList);
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Article article = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
        }

        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        // Populate the data into the template view using the data object
        tvTitle.setText(article.getHeadLine());

        //clear out the recycled image
        imageView.setImageResource(0);

        // populate the thumbnail image
        String thumbnail = article.getThumbNail();

        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        // Return the completed view to render on screen
        return convertView;

    }
}
