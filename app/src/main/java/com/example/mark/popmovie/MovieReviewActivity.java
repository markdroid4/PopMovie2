package com.example.mark.popmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieReviewActivity extends AppCompatActivity {

    private TextView mTitle;

    private TextView mReviewAuthor;
    private TextView mReviewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        mTitle = (TextView) findViewById(R.id.tv_review_title);
        mReviewAuthor = (TextView) findViewById(R.id.tv_author);
        mReviewContent = (TextView)  findViewById(R.id.tv_content);

        mTitle.setText("Movie Review");
        Intent intent = getIntent();
        if (intent.getStringExtra("author") != null) {
            mReviewAuthor.setText(intent.getStringExtra("author"));
        }

        if (intent.getStringExtra("content") != null) {
            mReviewContent.setText(intent.getStringExtra("content"));
        }


    }
}
