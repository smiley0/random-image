package xyz.kiraly.randomimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageStore store;
    private ImageView imageView;
    private Random random = new Random();
    private int previousRandom = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        store = new ImageStore(getApplicationContext());
        imageView = findViewById(R.id.imageView);

        getRandomImage(null);
    }

    public void getRandomImage(View view) {
        ArrayList<String> images = store.getImages();

        if (images.size() == 0) {
            return;
        }

        if (images.size() == 1) {
            // Prevent inifinty loop
            previousRandom = -1;
        }

        int r = previousRandom;
        while (r == previousRandom) {
            r = random.nextInt(images.size());
        }
        previousRandom = r;

        String path = images.get(r);
        Bitmap image = store.loadImage(path);
        imageView.setImageBitmap(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ImageDatabaseActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}