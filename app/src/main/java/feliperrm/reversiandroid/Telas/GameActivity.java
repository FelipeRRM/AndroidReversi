package feliperrm.reversiandroid.Telas;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import feliperrm.reversiandroid.ArtificialInteligence.BaseIntelligence;
import feliperrm.reversiandroid.ArtificialInteligence.EasyIntelligence;
import feliperrm.reversiandroid.ArtificialInteligence.HardIntelligence;
import feliperrm.reversiandroid.Uteis.Application;
import feliperrm.reversiandroid.Interfaces.GameActivityInterface;
import feliperrm.reversiandroid.R;
import feliperrm.reversiandroid.Adapters.ReversiBoardAdapter;


public class GameActivity extends BaseActivity implements GameActivityInterface {

    RecyclerView recyclerView;
    ReversiBoardAdapter.MyDataSet[][] casas;
    ReversiBoardAdapter adapter;
    int boardSize;
    TextView p1Pieces, p2Pieces, whoIsPlaying;
    BaseIntelligence intelligence;
    ImageView p1PieceAnim, p2PieceAnim;
    FrameLayout frameAnim;
    LinearLayout linearLayoutBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        getThingsFromBundle();
        createInitialSetup();
        setUpAdapter();
        updatePlayerPlaying(1);
    }

    private void findViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        p1Pieces = (TextView) findViewById(R.id.p1Pieces);
        p2Pieces = (TextView) findViewById(R.id.p2Pieces);
        whoIsPlaying = (TextView) findViewById(R.id.whoIsPlaying);
        p1PieceAnim = (ImageView) findViewById(R.id.player_one_piece);
        p2PieceAnim = (ImageView) findViewById(R.id.player_two_piece);
        frameAnim = (FrameLayout) findViewById(R.id.frameLayoutAnim);
        linearLayoutBackground = (LinearLayout) findViewById(R.id.linearBackgroundLayout);
        if(((Application)getApplication()).getBgImageRes()!=-1) {
            linearLayoutBackground.setBackgroundResource(((Application) getApplication()).getBgImageRes());
        }
        else{
            //carrega do disco
        }
    }

    private void getThingsFromBundle(){
        boardSize = getIntent().getExtras().getInt("SIZE");

        switch(getIntent().getExtras().getInt("AI")){
            case 1:{
                intelligence = new EasyIntelligence(boardSize,boardSize);
                break;
            }
            case 2:{
                intelligence = new HardIntelligence(boardSize,boardSize);
                break;
            }
            default:{
                intelligence = null;
            }
        }
    }

    private void createInitialSetup(){
        int linhas = boardSize;
        int colunas = boardSize;
        casas = new ReversiBoardAdapter.MyDataSet[linhas][colunas];
        int i = 0;
        int j = 0;
        while(i<linhas){
            while(j<colunas){
                casas[i][j] = new ReversiBoardAdapter.MyDataSet(0);
                j++;
            }
            j=0;
            i++;
        }
        casas[(boardSize/2)-1][(boardSize/2)-1].owner=1;
        casas[boardSize/2][boardSize/2].owner=1;
        casas[(boardSize/2)-1][(boardSize/2)].owner=2;
        casas[(boardSize/2)][(boardSize/2)-1].owner=2;

    }

    private void setUpAdapter(){
        int linhas = boardSize;
        int colunas = boardSize;
        MediaPlayer flip = null;
        MediaPlayer fall = null;
        if(((Application)getApplication()).isClickSound()){
            flip = MediaPlayer.create(this,R.raw.flip_sound);
            fall = MediaPlayer.create(this,R.raw.piece_falling);
        }
        adapter = new ReversiBoardAdapter(casas,linhas,colunas,p1PieceAnim,p2PieceAnim,frameAnim,intelligence,flip,fall,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, colunas));
        recyclerView.setAdapter(adapter);
        adapter.updatePiecesNumber();
    }

    @Override
    public void updatePlayerPieces(int p1NumPieces, int p2NumPieces) {
        p1Pieces.setText(getNameFromApplication(1) + ": " + p1NumPieces + " " + getString(R.string.pieces));
        p2Pieces.setText(getNameFromApplication(2) + ": " + p2NumPieces + " " + getString(R.string.pieces));

    }

    @Override
    public void updatePlayerPlaying(int player) {
        String name = getNameFromApplication(player);
        whoIsPlaying.setText(name+" "+getString(R.string.is_playing));
    }

    private String getNameFromApplication(int playerNumber){
        if(playerNumber==1)
            return((Application)getApplication()).getP1Name();
        else
            if(intelligence==null)
                return((Application)getApplication()).getP2Name();
            else
                return intelligence.getName();
    }

    @Override
    public void noMoveAvailableForPlayer(int player) {
        Snackbar
                .make((View) recyclerView.getParent(), getString(R.string.no_available_move)+" "+getNameFromApplication(player)+". "+getString(R.string.play_again)+".", Snackbar.LENGTH_LONG)
                .show(); // Dont forget to show
    }

    @Override
    public void noMoveEndGame(int p1Pieces, int p2Pieces) {
        String winner;
        if(p1Pieces>p2Pieces)
            winner = getNameFromApplication(1);
        else if(p2Pieces>p1Pieces)
            winner = getNameFromApplication(2);
        else
            winner = "Empatou vish maria!";
        WinFragment fragment = WinFragment.createFragment(winner);
        fragment.show(getSupportFragmentManager(),"FragWinner");
    }

    @Override
    public Application getApplicationCallback() {
        return (Application) getApplication();
    }

    @Override
    public Resources getResourcesCallback() {
        return getResources();
    }


}
