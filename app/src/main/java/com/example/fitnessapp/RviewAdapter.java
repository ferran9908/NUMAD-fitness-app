package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder>{

    private final ArrayList<ItemCard> itemList;
    private ItemClickListener listener;

    private Context context;



    //Constructor
    public RviewAdapter(ArrayList<ItemCard> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new RviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RviewHolder holder, int position) {
        ItemCard currentItem = itemList.get(position);
        final String[] link = {itemList.get(position).getItemDesc()};

        holder.itemName.setText(currentItem.getItemName());
        holder.itemDesc.setText(currentItem.getItemDesc());

        holder.itemDesc.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // String link =  itemList.get(position).getItemDesc();

                        Intent i = new Intent(Intent.ACTION_VIEW);

                        if (URLUtil.isValidUrl(link[0])) {

                            i.setData(Uri.parse(link[0]));
                            context.startActivity(i);
                            link[0] = "";
                        }
                        else {
                            Toast.makeText(context, "Link is not a valid URL", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
