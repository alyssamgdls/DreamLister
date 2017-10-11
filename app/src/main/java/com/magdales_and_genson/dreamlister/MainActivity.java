package com.magdales_and_genson.dreamlister;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView wishlist;
    Button addButton;
    Button updateButton;
    ArrayList<WishItem> list;
    WishListAdapter adapter = null;
    ImageView imageViewWish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        list = new ArrayList<>();
        adapter = new WishListAdapter(this, R.layout.wish_item, list);
        wishlist.setAdapter(adapter);

        // get data from sqlite
        Cursor cursor = AddOrUpdateItem.sqLiteHelper.getData("SELECT * FROM DREAM");
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
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        if (item == 0) {
                            //;update
                            Cursor c = AddOrUpdateItem.sqLiteHelper.getData("SELECT id FROM DREAM");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            //show dialog update here
                            showDialogUpdate(MainActivity.this, arrID.get(position));
                        } else {
                            //delete
                            Cursor c = AddOrUpdateItem.sqLiteHelper.getData("SELECT id FROM DREAM");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDeleteDialog(arrID.get(position));

                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        wishlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TextView name = (TextView) findViewById(R.id.wish_name);
                TextView price = (TextView) findViewById(R.id.wish_price);
                TextView description = (TextView) findViewById(R.id.wish_descr);

                String nameStr = name.getText().toString();
                String priceStr = price.getText().toString();
                String descriptionStr = description.getText().toString();

                Intent intent = new Intent(getApplicationContext(), DisplayItem.class);
                intent.putExtra("we", nameStr);
                intent.putExtra("are", priceStr);
                intent.putExtra("one", descriptionStr);
                startActivity(intent);
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

    private void showDialogUpdate(Activity activity, final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_wish_item);
        dialog.setTitle("Update");

        imageViewWish = (ImageView) dialog.findViewById(R.id.imageViewWish);
        final EditText edtName = (EditText) dialog.findViewById(R.id.item_name_edittext);
        final EditText edtPrice = (EditText) dialog.findViewById(R.id.item_price_edittext);
        final EditText edtDescription = (EditText) dialog.findViewById(R.id.item_description_edittext);
        Button btnUpdate = (Button) dialog.findViewById(R.id.update_button);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AddOrUpdateItem.sqLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            edtDescription.getText().toString().trim(),
                            AddOrUpdateItem.imageViewToByte(imageViewWish),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateWishList();
            }
        });


    }

    private void showDeleteDialog(final int idWish) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle("Delete?");
        dialogDelete.setMessage("Are you sure you want to delete this dream?");
        dialogDelete.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    AddOrUpdateItem.sqLiteHelper.deleteData(idWish);
                    Toast.makeText(MainActivity.this, "Dream has been deleted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                updateWishList();
            }
        });

        dialogDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateWishList() {
        // get all data from sqlite
        Cursor cursor = AddOrUpdateItem.sqLiteHelper.getData("SELECT * FROM DREAM");
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
    }


    public void findViews() {
        wishlist = (ListView) findViewById(R.id.wishes_listview);
        addButton = (Button) findViewById(R.id.add_wish_button);
        updateButton = (Button) findViewById(R.id.back_button);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewWish.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
