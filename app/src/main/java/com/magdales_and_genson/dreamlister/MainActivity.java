package com.magdales_and_genson.dreamlister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView wishlist;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemInfo = new Intent(MainActivity.this, AddOrUpdateItem.class);
                startActivity(itemInfo);
            }
        });
    }

    public void findViews() {
        wishlist = (ListView)findViewById(R.id.wishes_listview);
        addButton = (Button)findViewById(R.id.add_wish_button);
    }
}
