package feliperrm.reversiandroid.Interfaces;

import android.content.res.Resources;

import feliperrm.reversiandroid.Uteis.Application;

/**
 * Created by Felipe on 06/09/2015.
 */
public interface GameActivityInterface{
    public void updatePlayerPieces(int p1Pieces, int p2Pieces);
    public void updatePlayerPlaying(int player);
    public void noMoveAvailableForPlayer(int player);
    public void noMoveEndGame(int p1Pieces, int p2Pieces);
    public Application getApplicationCallback();
    public Resources getResourcesCallback();
}