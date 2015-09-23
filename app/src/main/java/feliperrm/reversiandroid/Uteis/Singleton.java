package feliperrm.reversiandroid.Uteis;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.util.LruCache;

import feliperrm.reversiandroid.Interfaces.GPSCallback;


/**
 * Created by Felipe on 04/05/2015.
 */
public class Singleton {

    private final int codigo = 413;
    private LruCache<String,Bitmap> cache;
    private GPS gps;
    private String diaDeHoje, lat, lng, token;
    private static Singleton singleton = null;
    private String login, senha;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getDiaDeHoje() {
        return diaDeHoje;
    }

    public void setDiaDeHoje(String diaDeHoje) {
        this.diaDeHoje = diaDeHoje;
    }

    public LruCache<String,Bitmap> getCache() {
        return cache;
    }

    private Singleton(Context context){
        this.lat = new String("");
        this.lng = new String("");
        this.gps = new GPS(context, new GPSCallback() {
            @Override
            public void onLocationChanged(Location location) {
                Singleton.this.lat = String.valueOf(location.getLatitude());
                Singleton.this.lng = String.valueOf(location.getLongitude());
            }
        });
        int memClass = ( (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE ) ).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 8;
        cache = new LruCache<String, Bitmap>( cacheSize ){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return (int) getSizeInBytes(bitmap);
            }
        };
        this.diaDeHoje = Uteis.getDataDeHoje();
    }

    private long getSizeInBytes(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    public GPS getGps() {
        return gps;
    }

    public static Singleton getSingleton(Context context){
        if(singleton==null){
            singleton = new Singleton(context);
        }
        return singleton;
    }

}