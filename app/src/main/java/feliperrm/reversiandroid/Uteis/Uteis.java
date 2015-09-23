package feliperrm.reversiandroid.Uteis;

/**
 * Created by Felipe on 06/09/2015.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Felipe on 08/05/2015.
 */
public class Uteis {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static final String _ARQUIVO = "Arquivo";

    public static void salvarSharedPreference(Context context, String chave, String dado){
        if(context==null) return;
        SharedPreferences shared = context.getSharedPreferences(_ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(chave, dado);
        editor.commit();
    }

    public static  void clearSharedPreference(Context context) {
        SharedPreferences shared = context.getSharedPreferences(_ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
    }

    public static void deleteSharedPreference(Context context, String chave){
        SharedPreferences shared = context.getSharedPreferences(_ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(chave);
        editor.commit();
    }

    public static String loadSharedPreference(Context context, String chave, String padrao){
        SharedPreferences shared = context.getSharedPreferences(_ARQUIVO, Context.MODE_PRIVATE);
        return shared.getString(chave,padrao);
    }

    public static String getDataDeHoje(){
        DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
        return df1.format(new Date());
    }

    public static String getHorario(){
        DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        Log.d("getHorario",df1.format(new Date()));
        return df1.format(new Date());

    }

    public static String getImei(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String fixJson(String json){
        String[] array = json.split("\\{");
        String retorno = new String();
        int i = 1;
        while (i<array.length){
            retorno = retorno + "{" + array[i];
            i++;
        }
        return retorno;
    }

    /**
     * Inserts the character ch at the location index of string st
     * @param st
     * @param ch
     * @param index
     * @return a new string
     */
    public static String insertCharAt(String st, char ch, int index){
        //1 Exception if st == null
        //2 Exception if index<0 || index>st.length()

        if (st == null){
            throw new NullPointerException("Null string!");
        }

        if (index < 0 || index > st.length())
        {
            throw new IndexOutOfBoundsException("Try to insert at negative location or outside of string");
        }
        return st.substring(0, index)+ch+st.substring(index, st.length());
    }

}