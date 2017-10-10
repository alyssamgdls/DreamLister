package com.magdales_and_genson.dreamlister;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lai on 10/10/2017.
 */

public class WishListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<WishItem> wishlist;

    public WishListAdapter(Context context, int layout, ArrayList<WishItem> wishlist) {
        this.context = context;
        this.layout = layout;
        this.wishlist = wishlist;
    }

    @Override
    public int getCount() {
        return wishlist.size();
    }

    @Override
    public Object getItem(int position) {
        return wishlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.wishName = row.findViewById(R.id.wish_name);
            holder.wishPrice = row.findViewById(R.id.wish_price);
            holder.wishDescription = row.findViewById(R.id.wish_descr);
            holder.wishImage = row.findViewById(R.id.wish_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        WishItem wishItem = wishlist.get(position);

        holder.wishName.setText(wishItem.getName());
        holder.wishPrice.setText(wishItem.getPrice());
        holder.wishDescription.setText(wishItem.getDescription());

        byte[] image = wishItem.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.wishImage.setImageBitmap(bitmap);

        return row;
    }

    private class ViewHolder {
        ImageView wishImage;
        TextView wishName, wishPrice, wishDescription;
    }
}
