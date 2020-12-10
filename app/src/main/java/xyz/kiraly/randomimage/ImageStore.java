package xyz.kiraly.randomimage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageStore {
    public static final String IMG_DIR = "images";

    private final ContextWrapper cw;
    private final File directory;

    public ImageStore(Context context) {
        cw = new ContextWrapper(context);
        directory = cw.getDir(IMG_DIR, Context.MODE_PRIVATE);
    }

    public void storeImage(Bitmap bitmap, String name) {
        File path = new File(directory, name + ".png");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getImages() {
        return new ArrayList<String>(Arrays.asList(directory.list()));
    }

    public Bitmap loadImage(String path) {
        try {
            File f = new File(directory, path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteImage(String path) {
        File f = new File(directory, path);
        f.delete();
    }
}
