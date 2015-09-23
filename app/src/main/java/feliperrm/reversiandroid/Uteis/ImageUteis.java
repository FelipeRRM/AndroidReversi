package feliperrm.reversiandroid.Uteis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import feliperrm.reversiandroid.Interfaces.IImageLoad;

/**
 * Created by Felipe on 08/05/2015.
 */
public class ImageUteis {

    private static final String HIGH_QUALITY = "_high_quality";

    public static void loadImage(final String imageName, final Context context, final IImageLoad callback){

        new AsyncTask<Void,Void,Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap = getBitmapFromMemCache(imageName,context);
                return bitmap;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                callback.onLoadFinished(bitmap);
            }
        }.execute();

    }

    public static Bitmap getScaledBitmapAndQuality(Bitmap source, int maxHeightOrWidth, int quality){
        Bitmap bitmap = getScaledBitmap(source,maxHeightOrWidth);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
        return BitmapFactory.decodeByteArray(bos.toByteArray(),0,bos.toByteArray().length);
    }

    public static Bitmap getScaledBitmap(Bitmap source, int maxHeightOrWidth){
        try {
            int height = source.getHeight();
            int width = source.getWidth();
            float factor;
            if (height >= width) {
                factor = (float) ((float) height / (float) maxHeightOrWidth);
                height = maxHeightOrWidth;
                width = (int) (width / factor);
            } else {
                factor = (float) ((float) width / (float) maxHeightOrWidth);
                width = maxHeightOrWidth;
                height = (int) (height / factor);
            }
            return Bitmap.createScaledBitmap(source, width, height, true);
        }
        catch (Exception e){
            // e.printStackTrace();
            Log.d("getScaledBitmap", "Null object (getScaledBitmap)");
            return null;
        }
    }

    /*Deleta  a imagem do cache e do disco, se existir*/
    public static void deleteBitmapFromCacheAndDisk(String key, Context context){
        synchronized (Singleton.getSingleton(context).getCache()){
            if(key!=null) {
                deleteImageFromDisk(context, key);
                Singleton.getSingleton(context).getCache().remove(key);
            }

        }
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap, Context context) {
        synchronized (Singleton.getSingleton(context).getCache()) {
            if(key!=null && bitmap!=null)
                if (getBitmapFromMemCache(key,context) == null) {
                    putImage(key, bitmap,context);
                }
        }
    }

    public static Bitmap getBitmapFromMemCache(String key, Context context) {
        synchronized (Singleton.getSingleton(context).getCache()) {
            Bitmap bmp=Singleton.getSingleton(context).getCache().get(key);
            if(bmp==null) {
                bmp = ImageUteis.loadImageFromDisk(context, key);
                if(bmp!=null)
                    Singleton.getSingleton(context).getCache().put(key, bmp);
            }
            return bmp;
        }
    }

    /*Coloca a imagem no cache e salva ela em disco*/
    private static void putImage(final String key, final Bitmap bitmap, Context context) {
        Singleton.getSingleton(context).getCache().put(key, bitmap);
        ImageUteis.saveImageToDisk(context, bitmap, key);
    }

    static public void deleteImageFromDisk(Context ctx, String name){
        if(ctx.deleteFile(name))
            Log.d("deleteImageFromDisk",name+" deletado do disco");
        else
            Log.d("deleteImageFromDisk","erro ao deletar " + name + " do disco");
    }

    static public void deleteImageFromDiskHighQuality(Context ctx, String name){
        if(ctx.deleteFile(name+HIGH_QUALITY))
            Log.d("deleteImageFromDisk",name+HIGH_QUALITY+" deletado do disco");
        else
            Log.d("deleteImageFromDisk","erro ao deletar " + name+HIGH_QUALITY + " do disco");
    }

    static public void saveImageToDisk(Context ctx, Bitmap tmp, String name){
        try {
            FileOutputStream out = ctx.openFileOutput(name, Context.MODE_PRIVATE);
            tmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d("ImageUteis", "Erro bitmap");
        }
    }

    static public void saveImageToDiskHighQuality(Context ctx, Bitmap tmp, String name){
        try {
            name = name + HIGH_QUALITY;
            FileOutputStream out = ctx.openFileOutput(name, Context.MODE_PRIVATE);
            tmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d("ImageUteis", "Erro bitmap");
        }
    }

    static public Bitmap loadImageFromDisk(Context ctx, String name){
        Bitmap bitmap = null;

        BitmapFactory.Options options=new BitmapFactory.Options();
        //options.inSampleSize = size;

        FileInputStream fis;
        //options.inPurgeable=true;
        options.inDither=false;

        try {

            fis = ctx.openFileInput( name );

            bitmap = BitmapFactory.decodeStream(fis,null,options);
            fis.close();

        } catch (FileNotFoundException e) {
            Log.d("ImageUteis", "Erro loadImageFromDisk FileNotFoundException");
        } catch (IOException e) {
            Log.d("ImageUteis", "Erro loadImageFromDisk IOException");
        } catch (OutOfMemoryError e) {
            Log.d("ImageUteis", "Erro loadImageFromDisk OutOfMemoryError");
        }

        return bitmap;
    }

    static public Bitmap loadImageFromDiskHighQuality(Context ctx, String name){
        name = name + HIGH_QUALITY;
        Bitmap bitmap = null;

        BitmapFactory.Options options=new BitmapFactory.Options();
        //options.inSampleSize = size;

        FileInputStream fis;
        //options.inPurgeable=true;
        options.inDither=false;

        try {

            fis = ctx.openFileInput( name );

            bitmap = BitmapFactory.decodeStream(fis,null,options);
            fis.close();

        } catch (FileNotFoundException e) {
            Log.d("ImageUteis", "Erro loadImageFromDisk FileNotFoundException");
        } catch (IOException e) {
            Log.d("ImageUteis", "Erro loadImageFromDisk IOException");
        } catch (OutOfMemoryError e) {
            Log.d("ImageUteis", "Erro loadImageFromDisk OutOfMemoryError");
        }

        return bitmap;
    }

    public static Bitmap rotateImage(Bitmap in, int angle) {
        Matrix mat = new Matrix();
        mat.postRotate(angle);
        return Bitmap.createBitmap(in, 0, 0, in.getWidth(), in.getHeight(), mat, true);
    }


}
