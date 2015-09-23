package feliperrm.reversiandroid.ArtificialInteligence;

import java.util.ArrayList;
import java.util.Random;

import feliperrm.reversiandroid.Adapters.ReversiBoardAdapter;

/**
 * Created by Felipe on 09/09/2015.
 */
public abstract class BaseIntelligence {

    String name;
    int height, width;

    public BaseIntelligence(int boardHeight, int boardWidth) {
        this.height = boardHeight;
        this.width = boardWidth;
        name = getRandomName();
    }

    private String getRandomName(){
        String[] names= new String[]{
                "Darth Vader", "Yoda", "Palpatine",
                "Frodo", "Legolas", "Gandalf"
        };
        int size = names.length;
        Random rand = new Random(System.currentTimeMillis());
        return names[rand.nextInt(size)];
    }

    abstract public void play(ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable, ReversiBoardAdapter.MyDataSet[][] board, int height, int width);

    public String getName() {
        return name;
    }
}
