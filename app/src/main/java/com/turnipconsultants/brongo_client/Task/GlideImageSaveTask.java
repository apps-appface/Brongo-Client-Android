package com.turnipconsultants.brongo_client.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pankaj on 26-12-2017.
 */

public class GlideImageSaveTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "GlideImageSaveTask";
    private Context context;

    public GlideImageSaveTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params == null || params.length != 1) {
            throw new IllegalArgumentException("You should offer 1 params, the first for the image source url");
        }

        String src = params[0];

        try {
            File file = Glide.with(context)
                    .load(src)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

            String imageFileName = "Brongo_Client" + ".jpg";
            File storageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Brongo_Client");

            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            File dstFile = new File(storageDir, imageFileName);
            if (dstFile.exists())
                dstFile.delete();
            if (!dstFile.exists()) {
                if (dstFile.createNewFile()) {
                    Log.i(TAG, "Profile Image File Created");
                } else {
                    Log.e(TAG, "Profile Image File Not Created");
                }
            }

            InputStream in = null;
            OutputStream out = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                out = new BufferedOutputStream(new FileOutputStream(dstFile));

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.flush();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}