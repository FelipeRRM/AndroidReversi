package feliperrm.reversiandroid.Uteis;

import android.media.MediaPlayer;
import android.util.Log;

import feliperrm.reversiandroid.R;

/**
 * Created by Felipe on 06/09/2015.
 */
public class Application extends android.app.Application {

    String p1Name;
    String p2Name;
    final String p1NameKey="P1_NAME";
    final String p2NameKey="P2_NAME";
    final String p1ImageResKey="P1_RES";
    final String p2ImageResKey="P2_RES";
    final String p1ImagePathKey="P1_PATH";
    final String p2ImagePathKey="P2_PATH";
    final String p2ImageNumberKey="P2_IMG_NUM";
    final String p1ImageNumberKey="P1_IMG_NUM";
    final String clickSoundKey="SOUND_KEY";
    final String musicSoundKey="MUSIC_KEY";
    final String bgImageResKey="BG_RES";
    final String bgImagePathKey="BG_PATH";
    final String bgImageNumberKey="BG_IMG_NUM";
    final String boardSizeKey="SIZE_KEY";
    int p1ImageRes, p2ImageRes, bgImageRes;
    int p1ImageNum, p2ImageNum, bgImageNum;
    String p1ImagePath, p2ImagePath, bgImagePath;
    boolean clickSound;
    boolean music;
    MediaPlayer mp;
    int boardSize;


    @Override
    public void onCreate() {
        super.onCreate();
        /* Assim que iniciar o aplicativo, carrega os ultimos nomes definidos e seta eles */
        p1Name=Uteis.loadSharedPreference(getApplicationContext(),p1NameKey,"Player1");
        p2Name=Uteis.loadSharedPreference(getApplicationContext(), p2NameKey, "Player2");
        bgImageRes= Integer.parseInt(Uteis.loadSharedPreference(getApplicationContext(), bgImageResKey, String.valueOf(-1)));
        bgImagePath=Uteis.loadSharedPreference(getApplicationContext(), bgImagePathKey, null);
        bgImageNum= Integer.parseInt(Uteis.loadSharedPreference(getApplicationContext(), bgImageNumberKey, String.valueOf(0)));
        p1ImageRes= Integer.parseInt(Uteis.loadSharedPreference(getApplicationContext(), p1ImageResKey, String.valueOf(-1)));
        p2ImageRes= Integer.parseInt(Uteis.loadSharedPreference(getApplicationContext(), p2ImageResKey, String.valueOf(-1)));
        p1ImagePath=Uteis.loadSharedPreference(getApplicationContext(), p1ImagePathKey, null);
        p2ImagePath=Uteis.loadSharedPreference(getApplicationContext(), p2ImagePathKey, null);
        p1ImageNum= Integer.parseInt(Uteis.loadSharedPreference(getApplicationContext(), p1ImageNumberKey, String.valueOf(0)));
        p2ImageNum= Integer.parseInt(Uteis.loadSharedPreference(getApplicationContext(), p2ImageNumberKey, String.valueOf(1)));
        boardSize = Integer.parseInt(Uteis.loadSharedPreference(getApplicationContext(), boardSizeKey, String.valueOf(6)));
        music = Boolean.parseBoolean(Uteis.loadSharedPreference(getApplicationContext(), musicSoundKey, String.valueOf(true)));
        mp = MediaPlayer.create(getApplicationContext(), R.raw.music_bg);
        mp.setLooping(true);
        resumeMusic();
        clickSound = Boolean.parseBoolean(Uteis.loadSharedPreference(getApplicationContext(), clickSoundKey, String.valueOf(true)));
        Log.d("Image1Res", String.valueOf(p1ImageRes));
        Log.d("Image2Res", String.valueOf(p2ImageRes));
        if(p1ImageRes==-1 && p1ImagePath==null){
            p1ImageRes = R.drawable.white_piece;
        }
        if(p2ImageRes==-1 && p2ImagePath==null){
            p2ImageRes = R.drawable.black_piece;
        }
        if(bgImageRes==-1 && bgImagePath==null){
            bgImageRes = R.drawable.background;
        }

    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        Uteis.salvarSharedPreference(getApplicationContext(),boardSizeKey, String.valueOf(boardSize));
    }

    public String getBgImagePath() {
        return bgImagePath;
    }

    public void setBgImagePath(String bgImagePath) {
        Uteis.salvarSharedPreference(getApplicationContext(),bgImagePathKey, bgImagePath);
        Uteis.deleteSharedPreference(getApplicationContext(), bgImageResKey);
        this.bgImageRes = -1;
        this.bgImagePath = bgImagePath;
    }

    public int getBgImageNum() {
        return bgImageNum;
    }

    public void setBgImageNum(int bgImageNum) {
        this.bgImageNum = bgImageNum;
        Uteis.salvarSharedPreference(getApplicationContext(),bgImageNumberKey, String.valueOf(bgImageNum));

    }

    public int getBgImageRes() {
        return bgImageRes;
    }

    public void setBgImageRes(int bgImageRes) {
        Uteis.salvarSharedPreference(getApplicationContext(),bgImageResKey, String.valueOf(bgImageRes));
        Uteis.deleteSharedPreference(getApplicationContext(), bgImagePathKey);
        this.bgImagePath = null;
        this.bgImageRes = bgImageRes;
    }

    public void pauseMusic(){
        if(mp!=null){
            mp.pause();
        }
    }

    public void resumeMusic(){
        if(mp!=null && music){
            mp.start();
        }
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
        Uteis.salvarSharedPreference(getApplicationContext(),musicSoundKey, String.valueOf(music));
        if(music==false){
            mp.pause();
        }
        else{
            mp.start();
        }
    }

    public boolean isClickSound() {
        return clickSound;
    }

    public void setClickSound(boolean clickSound) {
        this.clickSound = clickSound;
        Uteis.salvarSharedPreference(getApplicationContext(),clickSoundKey, String.valueOf(clickSound));

    }

    public int getP1ImageNum() {
        return p1ImageNum;
    }

    public void setP1ImageNum(int p1ImageNum) {
        this.p1ImageNum = p1ImageNum;
        Uteis.salvarSharedPreference(getApplicationContext(),p1ImageNumberKey, String.valueOf(p1ImageNum));
    }

    public int getP2ImageNum() {
        return p2ImageNum;
    }

    public void setP2ImageNum(int p2ImageNum) {
        this.p2ImageNum = p2ImageNum;
        Uteis.salvarSharedPreference(getApplicationContext(),p2ImageNumberKey, String.valueOf(p2ImageNum));
    }

    public int getP1ImageRes() {
        return p1ImageRes;
    }

    public void setP1ImageRes(int p1ImageRes) {
        Uteis.salvarSharedPreference(getApplicationContext(),p1ImageResKey, String.valueOf(p1ImageRes));
        Uteis.deleteSharedPreference(getApplicationContext(), p1ImagePathKey);
        this.p1ImagePath = null;
        this.p1ImageRes = p1ImageRes;
    }

    public int getP2ImageRes() {
        return p2ImageRes;
    }

    public void setP2ImageRes(int p2ImageRes) {
        Uteis.salvarSharedPreference(getApplicationContext(),p2ImageResKey, String.valueOf(p2ImageRes));
        Uteis.deleteSharedPreference(getApplicationContext(), p2ImagePathKey);
        this.p2ImagePath = null;
        this.p2ImageRes = p2ImageRes;
    }

    public String getP1ImagePath() {
        return p1ImagePath;
    }

    public void setP1ImagePath(String p1ImagePath) {
        Uteis.salvarSharedPreference(getApplicationContext(),p1ImagePathKey,p1ImagePath);
        Uteis.deleteSharedPreference(getApplicationContext(), p1ImageResKey);
        this.p1ImageRes = -1;
        this.p1ImagePath = p1ImagePath;
    }

    public String getP2ImagePath() {
        return p2ImagePath;
    }

    public void setP2ImagePath(String p2ImagePath) {
        Uteis.salvarSharedPreference(getApplicationContext(),p2ImagePathKey,p2ImagePath);
        Uteis.deleteSharedPreference(getApplicationContext(),p2ImageResKey);
        this.p2ImageRes = -1;
        this.p2ImagePath = p2ImagePath;
    }

    public String getP1Name() {
        return p1Name;
    }

    public void setP1Name(String p1Name) {
        Uteis.salvarSharedPreference(getApplicationContext(),p1NameKey,p1Name);
        this.p1Name = p1Name;
    }

    public String getP2Name() {
        return p2Name;
    }

    public void setP2Name(String p2Name) {
        Uteis.salvarSharedPreference(getApplicationContext(),p2NameKey,p2Name);
        this.p2Name = p2Name;
    }

    public void clearP1Name(){
        Uteis.deleteSharedPreference(getApplicationContext(),p1NameKey);
        p1Name=Uteis.loadSharedPreference(getApplicationContext(),p1NameKey,"Player1");
    }

    public void clearP2Name(){
        Uteis.deleteSharedPreference(getApplicationContext(),p2NameKey);
        p2Name=Uteis.loadSharedPreference(getApplicationContext(),p2NameKey,"Player2");
    }
}
