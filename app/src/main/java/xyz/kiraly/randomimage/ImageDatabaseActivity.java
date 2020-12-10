package xyz.kiraly.randomimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import xyz.kiraly.randomimage.adapter.ItemAdapter;

public class ImageDatabaseActivity extends AppCompatActivity {
    public static final int RESULT_LOAD_IMG = 1;
    private ImageStore store;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Images");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        store = new ImageStore(getApplicationContext());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickerIntent.setType("image/*");
                pickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(pickerIntent, "Select Pictures"), RESULT_LOAD_IMG);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new ItemAdapter(store));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<Uri> uris = new ArrayList<Uri>();

        if (resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uris.add(imageUri);
                }
            } else if (data.getData() != null) {
                uris.add(data.getData());
            }
        }

        for (Uri uri : uris) {
            try {
                final InputStream imageStream = getContentResolver().openInputStream(uri);
                final Bitmap image = BitmapFactory.decodeStream(imageStream);
                store.storeImage(image, uri.getLastPathSegment());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        recyclerView.setAdapter(new ItemAdapter(store));
        recyclerView.invalidate();
    }
}