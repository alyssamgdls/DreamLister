package com.magdales_and_genson.dreamlister;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView wishlist;
    Button addButton;
    ArrayList<WishItem> list;
    WishListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        list = new ArrayList<>();
        adapter = new WishListAdapter(this, R.layout.wish_item, list);
        wishlist.setAdapter(adapter);

        // get data from sqlite
        Cursor cursor = AddOrUpdateItem.sqLiteHelper.getData("SELECT * FROM ITEM");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            String description = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new WishItem(name, price, description, image, id));
        }
        adapter.notifyDataSetChanged();

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
