package com.magdales_and_genson.dreamlister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayItem extends AppCompatActivity {

    private ImageView image;
    private LinearLayout nameLinear, priceLinear, descriptionLinear;
    private TextView namelbl, nametxt, pricelbl, pricetxt, desclbl, desctxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_item);

        findViews();

        Intent intent = getIntent();

        String name = intent.getStringExtra("we");
        String price = intent.getStringExtra("are");
        String description = intent.getStringExtra("one");

        nametxt.setText(name);
        pricetxt.setText(price);
        desctxt.setText(description);
    }

    public void findViews() {
        image = (ImageView) findViewById(R.id.dream_image);

        nameLinear = (LinearLayout) findViewById(R.id.name_linear);
        priceLinear = (LinearLayout) findViewById(R.id.price_linear);
        descriptionLinear = (LinearLayout) findViewById(R.id.description_linear);

        namelbl = (TextView) findViewById(R.id.name_label);
        pricelbl = (TextView) findViewById(R.id.price_label);
        desclbl = (TextView) findViewById(R.id.description_label);

        nametxt = (TextView) findViewById(R.id.name_text);
        pricetxt = (TextView) findViewById(R.id.price_text);
        desctxt = (TextView) findViewById(R.id.description_text);
    }
}
