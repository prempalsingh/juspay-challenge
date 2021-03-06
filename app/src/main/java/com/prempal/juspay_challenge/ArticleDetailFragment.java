package com.prempal.juspay_challenge;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;

public class ArticleDetailFragment extends Fragment {

    public static final String ARG_ITEM_URL = "item_url";
    private String text, title, imageUrl;
    private CollapsingToolbarLayout appBarLayout;
    private ImageView backdrop;
    private TextView article;
    private ImageView articleImage;
    private TextView articleTitle;

    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_URL)) {

            String url = getArguments().getString(ARG_ITEM_URL);
            new FetchArticleTask().execute(url);

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Loading..");
            }

            backdrop = (ImageView) activity.findViewById(R.id.backdrop);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail, container, false);
        article = (TextView) rootView.findViewById(R.id.tv_article_text);
        articleImage = (ImageView) rootView.findViewById(R.id.iv_article_image);
        articleTitle = (TextView) rootView.findViewById(R.id.tv_article_title);
        return rootView;
    }

    private class FetchArticleTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            HtmlFetcher fetcher = new HtmlFetcher();
            JResult res;
            try {
                res = fetcher.fetchAndExtract(urls[0], 15000, true);
                text = res.getText();
                title = res.getTitle();
                imageUrl = res.getImageUrl();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(title != null){
                if(appBarLayout != null){
                    appBarLayout.setTitle(title);
                }else if(articleTitle != null){
                    articleTitle.setText(title);
                }
            }

            if(imageUrl != null){
                if(backdrop != null){
                    Picasso.with(backdrop.getContext()).load(imageUrl)
                            .into(backdrop);
                }else if(articleImage != null){
                    Picasso.with(articleImage.getContext()).load(imageUrl)
                            .into(articleImage);
                }
            }

            if (text != null) {
                article.setText(text);
            }

        }
    }
}
