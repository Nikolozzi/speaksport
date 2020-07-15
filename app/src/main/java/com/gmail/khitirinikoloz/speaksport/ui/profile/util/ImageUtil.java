package com.gmail.khitirinikoloz.speaksport.ui.profile.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.gmail.khitirinikoloz.speaksport.ui.profile.ProfileActivity.IMAGE_QUALITY;

public class ImageUtil {

    public static String savePhotoToStorage(final Context context, final Bitmap bitmap) {
        final ContextWrapper contextWrapper = new ContextWrapper(context);
        final File directory = contextWrapper.getDir("images", Context.MODE_PRIVATE);
        final File path = new File(directory, "profile.jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadImageFromStorage(final String path) {
        final File file = new File(path, "profile.jpg");
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
