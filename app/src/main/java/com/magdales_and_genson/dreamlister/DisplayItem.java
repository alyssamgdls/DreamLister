package com.magdales_and_genson.dreamlister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayItem extends AppCompatActivity {

    ImageView image;
    LinearLayout nameLinear, priceLinear, descriptionLinear;
    TextView namelbl, nametxt, pricelbl, pricetxt, desclbl, desctxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        findViews();

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            String getName = (String) bd.get("we");
            namelbl.setText(getName);
            String getPrice = (String) bd.get("are");
            pricetxt.setText(getPrice);
            String getDescription = (String) bd.get("one");
            desctxt.setText(getDescription);
        }


//        Intent i = this.getIntent();
//        String name = i.getStringExtra("we");
//        String price = i.getStringExtra("are");
//        String description = i.getStringExtra("one");

//        nametxt.setText(name);
//        pricetxt.setText(price);
//        desctxt.setText(description);
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
