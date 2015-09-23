package feliperrm.reversiandroid.ArtificialInteligence;

import java.util.ArrayList;
import java.util.Random;

import feliperrm.reversiandroid.Adapters.ReversiBoardAdapter;

/**
 * Created by Felipe on 09/09/2015.
 */
public class EasyIntelligence extends BaseIntelligence {


    public EasyIntelligence(int boardHeight, int boardWidth) {
        super(boardHeight, boardWidth);
        super.name = super.name + " (CPU Easy)";
    }

    @Override
    public void play(ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable, ReversiBoardAdapter.MyDataSet[][] board, int height, int width) {
        int size = movesAvailable.size();
        Random random = new Random(System.currentTimeMillis());
        ReversiBoardAdapter.MyDataSet myMove = movesAvailable.get(random.nextInt(size));
        myMove.getAttachedViewHolder().getMoveAvailable().performClick();

    }
}
