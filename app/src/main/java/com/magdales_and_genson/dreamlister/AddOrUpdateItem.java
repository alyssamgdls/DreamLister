package com.magdales_and_genson.dreamlister;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddOrUpdateItem extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_FOR_GALLERY = 123;
    LinearLayout buttonsLinear;
    Button addButton, updateButton, deleteButton, backButton;
    ImageView cameraImage;
    EditText nameEdit, priceEdit, descriptionEdit;
    TextView cameraText;

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish_item);

        findViews();

        sqLiteHelper = new SQLiteHelper(this, "DreamListerDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS DREAM(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, description VARCHAR, image BLOB)");

        cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        AddOrUpdateItem.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_FOR_GALLERY
                );
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqLiteHelper.addData(
                            nameEdit.getText().toString().trim(),
                            priceEdit.getText().toString().trim(),
                            descriptionEdit.getText().toString().trim(),
                            imageViewToByte(cameraImage));
                    Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                    nameEdit.setText("");
                    priceEdit.setText("");
                    descriptionEdit.setText("");
                    cameraImage.setImageResource(R.drawable.camera);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wishlist = new Intent(AddOrUpdateItem.this, MainActivity.class);
                startActivity(wishlist);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_FOR_GALLERY) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_FOR_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                cameraImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void findViews(){
        buttonsLinear = (LinearLayout)findViewById(R.id.buttons_linear);
        addButton = (Button)findViewById(R.id.add_button);
        updateButton = (Button)findViewById(R.id.update_button);
        deleteButton = (Button)findViewById(R.id.delete_button);
        backButton = (Button) findViewById(R.id.back_button);

        cameraImage = (ImageView) findViewById(R.id.camera_photo);

        nameEdit = (EditText) findViewById(R.id.item_name_edittext);
        priceEdit = (EditText) findViewById(R.id.item_price_edittext);
        descriptionEdit = (EditText) findViewById(R.id.item_description_edittext);

        cameraText = (TextView) findViewById(R.id.camera_text);
    }
}
