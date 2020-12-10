package xyz.kiraly.randomimage.adapter;

import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.kiraly.randomimage.ImageStore;
import xyz.kiraly.randomimage.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    private final ImageStore store;
    private ArrayList<String> items;

    public ItemAdapter(ImageStore store) {
        this.store = store;
        items = store.getImages();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("Inflating");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String path = items.get(position);
        holder.getImageView().setImageBitmap(store.loadImage(path));

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(path);
                store.deleteImage(path);
                items = store.getImages();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
