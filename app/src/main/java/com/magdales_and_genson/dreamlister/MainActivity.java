package com.magdales_and_genson.dreamlister;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView wishlist;
    Button addButton;
    Button updateButton;
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

        wishlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        if (item == 0) {
                            //;update
                            showDialogUpdate(MainActivity.this);
//                            updateButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Intent itemInfo = new Intent(MainActivity.this, AddOrUpdateItem.class);
//                                    startActivity(itemInfo);
//                                }
//                            });
                        } else {
                            //delete
                            Toast.makeText(getApplicationContext(), "Deleted..", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialog.show();
                return true;
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemInfo = new Intent(MainActivity.this, AddOrUpdateItem.class);
                startActivity(itemInfo);
            }
        });
    }

    private void showDialogUpdate(Activity activity){

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_wish_activity);
        dialog.setTitle("Update");

        ImageView imageViewWish = (ImageView) dialog.findViewById(R.id.imageViewWish);
        EditText edtName = (EditText) dialog.findViewById(R.id.item_name_edittext);
        EditText edtPrice = (EditText) dialog.findViewById(R.id.item_price_edittext);
        EditText edtDescription = (EditText) dialog.findViewById(R.id.item_description_edittext);
        Button btnUpdate = (Button) dialog.findViewById(R.id.update_button);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();


    }

    public void findViews() {
        wishlist = (ListView)findViewById(R.id.wishes_listview);
        addButton = (Button)findViewById(R.id.add_wish_button);
        updateButton = (Button)findViewById(R.id.add_wish_button);
    }
}
