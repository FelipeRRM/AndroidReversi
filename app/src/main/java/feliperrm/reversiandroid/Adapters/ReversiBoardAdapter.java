package feliperrm.reversiandroid.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import feliperrm.reversiandroid.ArtificialInteligence.BaseIntelligence;
import feliperrm.reversiandroid.Interfaces.GameActivityInterface;
import feliperrm.reversiandroid.R;
import feliperrm.reversiandroid.Uteis.Application;

/**
 * Created by Felipe on 05/09/2015.
 */
public class ReversiBoardAdapter extends RecyclerView.Adapter<ReversiBoardAdapter.BoardViewHolder> {

    private static final long CHANGE_ANIM_DURATION = 385;
    private static final long NEW_PIECE_ANIM_DURATION = 720;
    MyDataSet[][] casas;
        int height, width;
        int p1Pieces, p2Pieces;
        boolean p1HasMoves, p2HasMoves;
        GameActivityInterface callback;
        int playerPlaying;
        int otherPlayer;
        int jogadasPossiveis;
        ArrayList<MyDataSet> movesAvailable;
    BaseIntelligence intelligence;
    ImageView p1PieceAnim, p2PieceAnim;
    View frameAnim;
    Bitmap p1, p2;
    MediaPlayer flipSound;
    MediaPlayer fallSound;



    public int getP1Pieces() {
        return p1Pieces;
    }

    public void setP1Pieces(int p1Pieces) {
        this.p1Pieces = p1Pieces;
    }

    public int getP2Pieces() {
        return p2Pieces;
    }

    public void setP2Pieces(int p2Pieces) {
        this.p2Pieces = p2Pieces;
    }

    public ReversiBoardAdapter(MyDataSet[][] casas, int height, int width, ImageView p1Anim, ImageView p2Anim, View animTudo, BaseIntelligence artificialIntelligence, MediaPlayer flipSound, MediaPlayer fallSound,  GameActivityInterface callback) {
        this.casas = casas;
        this.height = height;
        this.width = width;
        this.callback = callback;
        this.playerPlaying = 1;
        this.otherPlayer = 2;
        movesAvailable = new ArrayList<>();
        this.intelligence = artificialIntelligence;
        this.p1PieceAnim = p1Anim;
        this.p2PieceAnim = p2Anim;
        this.frameAnim = animTudo;
        this.fallSound = fallSound;
        this.flipSound = flipSound;
        if(callback.getApplicationCallback().getP1ImageRes() != -1){
            p1 = BitmapFactory.decodeResource(callback.getResourcesCallback(),callback.getApplicationCallback().getP1ImageRes());
        }
        else{
            // carregar p1 do disco;
        }

        if(callback.getApplicationCallback().getP2ImageRes() != -1){
            p2 = BitmapFactory.decodeResource(callback.getResourcesCallback(),callback.getApplicationCallback().getP2ImageRes());
        }
        else{
            // carregar p2 do disco;
        }
        this.p1PieceAnim.setImageBitmap(p1);
        this.p2PieceAnim.setImageBitmap(p2);

    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_one_square, viewGroup, false);
        BoardViewHolder myViewHolder = new BoardViewHolder(v,p1,p2);
        return myViewHolder;
    }

    private int getColunaFromPosition(int position){
        while(position>(width-1)){
            position = position-width;
        }
        return position;
    }

    private int getLinhafromPosition(int position, int coluna){
        return (position-coluna)/height;
    }

    @Override
    public void onBindViewHolder(BoardViewHolder boardViewHolder, int position) {
        /* Como o Adapter funciona com posi��es unidimensionais,
         devemos transformar essa posi��o em duas dimens�es para acessarmos nossa matriz de dados*/
        int coluna = getColunaFromPosition(position);
        int linha = getLinhafromPosition(position, coluna);
       // Log.d("Position", String.valueOf(position));
       // Log.d("Coluna", String.valueOf(coluna));
       // Log.d("Linha", String.valueOf(linha));


        MyDataSet setAtual = casas[linha][coluna];
        setAtual.linha = linha;
        setAtual.coluna = coluna;
        setAtual.attachedViewHolder=boardViewHolder;
        switch (setAtual.owner){
            case 0:{
                boardViewHolder.getPlayerOnePiece().setVisibility(View.GONE);
                boardViewHolder.getPlayerTwoPiece().setVisibility(View.GONE);
                testaHorizontais(setAtual,linha,coluna);
                testaVertical(setAtual,linha,coluna);
                testaDiagonalPrincipal(setAtual,linha,coluna);
                testaDiagonalSecundaria(setAtual,linha,coluna);
                if(setAtual.piecesToChangeOnClick.size()>0) {
                    enableClick(boardViewHolder, linha, coluna);
                    jogadasPossiveis++;
                    movesAvailable.add(setAtual);
                }
                else
                    disableClick(boardViewHolder);
                break;
            }
            case 1:{
                boardViewHolder.getPlayerOnePiece().setVisibility(View.VISIBLE);
                boardViewHolder.getPlayerTwoPiece().setVisibility(View.GONE);
                disableClick(boardViewHolder); // Já tem dono, logo desligamos o click.
                break;
            }
            case 2:{
                boardViewHolder.getPlayerOnePiece().setVisibility(View.GONE);
                boardViewHolder.getPlayerTwoPiece().setVisibility(View.VISIBLE);
                disableClick(boardViewHolder); // Já tem dono, logo desligamos o click.
                break;
            }
        }

        boardViewHolder.debugNumber.setText(position + "[" + linha + "]" + "[" + coluna + "]");
        if(position==((height*width)-1)){
            if(jogadasPossiveis==0) {
                if (playerPlaying == 1)
                    p1HasMoves = false;
                else if (playerPlaying == 2)
                    p2HasMoves = false;
                if ((!p1HasMoves) && (!p2HasMoves)) {
                    callback.noMoveEndGame(p1Pieces, p2Pieces);
                }
                else {
                    if(p1Pieces+p2Pieces!=height*width)
                        callback.noMoveAvailableForPlayer(playerPlaying);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            endTurn();
                        }
                    });
                }
            }
            else{
                if (playerPlaying == 1)
                    p1HasMoves = true;
                else if (playerPlaying == 2) {
                    p2HasMoves = true;
                    if (intelligence!=null){
                        intelligence.play(movesAvailable,casas,height,width);
                    }
                }
            }
        }
    }

    private void enableClick(BoardViewHolder holder, final int linha, final int coluna){

        holder.moveAvailable.setVisibility(View.VISIBLE);
        holder.moveAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllClickListeners();
                int i = 0;
                int size = casas[linha][coluna].piecesToChangeOnClick.size();
                while (i < size) {
                    animateOwnerChange(casas[linha][coluna].piecesToChangeOnClick.get(i));
                    i++;
                }
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateNewPieceArriving(casas[linha][coluna]);

                    }
                }, 2 * CHANGE_ANIM_DURATION + 100);

            }
        });
    }

    private void removeAllClickListeners(){
        int tam = movesAvailable.size();
        int i = 0;
        while (i<tam){
            disableClick(movesAvailable.get(i).attachedViewHolder);
            i++;
        }
        movesAvailable.clear();
    }


    private void animateNewPieceArriving(final MyDataSet casa){
        final View peca = casa.attachedViewHolder.getNewOwnerPiece(playerPlaying);
      //  peca.setVisibility(View.VISIBLE);
      //  casa.attachedViewHolder.getMoveAvailable().setVisibility(View.GONE);
        View v = (View) peca.getParent();
        frameAnim.setScaleX(3);
        frameAnim.setScaleY(3);
        frameAnim.setX(v.getX());
        frameAnim.setY(v.getY());
        frameAnim.getLayoutParams().height = v.getHeight();
        frameAnim.getLayoutParams().width = v.getWidth();

        if(playerPlaying==1) {
            p1PieceAnim.setVisibility(View.VISIBLE);
        }
        else if(playerPlaying==2) {
            p2PieceAnim.setVisibility(View.VISIBLE);
        }
        if(fallSound!=null)
            fallSound.start();
        frameAnim.animate().scaleX(1f).scaleY(1f).setInterpolator(new BounceInterpolator()).setDuration(NEW_PIECE_ANIM_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                p1PieceAnim.setVisibility(View.GONE);
                p2PieceAnim.setVisibility(View.GONE);
                peca.setVisibility(View.VISIBLE);
                casa.owner = playerPlaying;
                endTurn();

            }
        });

    }

    private void endTurn(){
        jogadasPossiveis=0;
        updatePiecesNumberAndClearHistory();
        swapPlayers();
        notifyDataSetChanged();
    }

    private void animateOwnerChange(final MyDataSet casa){
        if(flipSound!=null)
            flipSound.start();
        casa.attachedViewHolder.getOldOwnerPiece(casa).animate().setDuration(CHANGE_ANIM_DURATION).rotationY(90f).setInterpolator(new LinearInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                casa.attachedViewHolder.getOldOwnerPiece(casa).setVisibility(View.GONE);
                View peca = casa.attachedViewHolder.getNewOwnerPiece(playerPlaying);
                peca.setVisibility(View.VISIBLE);
                peca.setRotationY(90f);
                peca.animate().setDuration(CHANGE_ANIM_DURATION).setInterpolator(new LinearInterpolator()).rotationY(0f);
            }
        });
        casa.owner=playerPlaying;
    }

    private void clearAllChangeHistory() {
        int i=0;
        int j=0;
        while(i<height){
            while (j<width){
                casas[i][j].piecesToChangeOnClick.clear();
                j++;
            }
            j=0;
            i++;
        }
    }

    private void disableClick(BoardViewHolder holder){
        holder.moveAvailable.setVisibility(View.GONE);
        holder.moveAvailable.setOnClickListener(null);
    }

    private void swapPlayers() {
        if(playerPlaying==1) {
            playerPlaying = 2;
            otherPlayer = 1;
        }
        else {
            playerPlaying = 1;
            otherPlayer = 2;
        }
        callback.updatePlayerPlaying(playerPlaying);
    }

    private void testaHorizontais(MyDataSet dataSet, int linha, int coluna){
        /* Percorre para a esquerda até chegar no limite ou achar uma peça da cor do jogador.
           Nesse mesmo loop, vai adicionando as peças do outro jogador em um array, pois caso
           encontremos uma peça do nosso jogador depois dessas peças, elas serão viradas na jogada
         */
        MyDataSet temp;
        int colunaTemp = coluna;
        boolean playerPieceInTheEnd = false;
        ArrayList<MyDataSet> tempDataSets = new ArrayList<>();

        while(colunaTemp>0){
            temp = casas[linha][colunaTemp-1];
            colunaTemp--;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
        playerPieceInTheEnd = false;
        colunaTemp = coluna;
        while(colunaTemp<width-1){
            temp = casas[linha][colunaTemp+1];
            colunaTemp++;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }

    private void testaVertical(MyDataSet dataSet, int linha, int coluna){
        /* Percorre para cima até chegar no limite ou achar uma peça da cor do jogador.
           Nesse mesmo loop, vai adicionando as peças do outro jogador em um array, pois caso
           encontremos uma peça do nosso jogador depois dessas peças, elas serão viradas na jogada
         */
        MyDataSet temp;
        int linhaTemp = linha;
        boolean playerPieceInTheEnd = false;
        ArrayList<MyDataSet> tempDataSets = new ArrayList<>();

        while(linhaTemp>0){
            temp = casas[linhaTemp-1][coluna];
            linhaTemp--;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
        playerPieceInTheEnd = false;
        linhaTemp = linha;
        while(linhaTemp<height-1){
            temp = casas[linhaTemp+1][coluna];
            linhaTemp++;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }

    private void testaDiagonalPrincipal(MyDataSet dataSet, int linha, int coluna){
        /* Percorre na diagonal \ para cima até chegar no limite ou achar uma peça da cor do jogador.
           Nesse mesmo loop, vai adicionando as peças do outro jogador em um array, pois caso
           encontremos uma peça do nosso jogador depois dessas peças, elas serão viradas na jogada
         */
        MyDataSet temp;
        int linhaTemp = linha;
        int colunaTemp = coluna;
        boolean playerPieceInTheEnd = false;
        ArrayList<MyDataSet> tempDataSets = new ArrayList<>();

        while(linhaTemp>0 && colunaTemp>0){
            temp = casas[linhaTemp-1][colunaTemp-1];
            linhaTemp--;
            colunaTemp--;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
        playerPieceInTheEnd = false;
        linhaTemp = linha;
        colunaTemp = coluna;
        while(linhaTemp<height-1 && colunaTemp<width-1){
            temp = casas[linhaTemp+1][colunaTemp+1];
            linhaTemp++;
            colunaTemp++;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }

    private void testaDiagonalSecundaria(MyDataSet dataSet, int linha, int coluna){
        /* Percorre na diagonal / para cima até chegar no limite ou achar uma peça da cor do jogador.
           Nesse mesmo loop, vai adicionando as peças do outro jogador em um array, pois caso
           encontremos uma peça do nosso jogador depois dessas peças, elas serão viradas na jogada
         */
        MyDataSet temp;
        int linhaTemp = linha;
        int colunaTemp = coluna;
        boolean playerPieceInTheEnd = false;
        ArrayList<MyDataSet> tempDataSets = new ArrayList<>();

        while(linhaTemp>0 && colunaTemp<width-1){
            temp = casas[linhaTemp-1][colunaTemp+1];
            linhaTemp--;
            colunaTemp++;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
        playerPieceInTheEnd = false;
        linhaTemp = linha;
        colunaTemp = coluna;
        while(linhaTemp<height-1 && colunaTemp>0){
            temp = casas[linhaTemp+1][colunaTemp-1];
            linhaTemp++;
            colunaTemp--;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma peça do jogador que está jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return height*width;
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        private ImageView playerOnePiece;
        private ImageView playerTwoPiece;
        private TextView debugNumber;
        private ImageView moveAvailable;

        public BoardViewHolder(View itemView) {
            super(itemView);
            playerOnePiece = (ImageView) itemView.findViewById(R.id.player_one_piece);
            playerTwoPiece = (ImageView) itemView.findViewById(R.id.player_two_piece);
            debugNumber = (TextView) itemView.findViewById(R.id.debugNumber);
            moveAvailable = (ImageView) itemView.findViewById(R.id.moveAvailable);
            playerOnePiece.setDrawingCacheEnabled(true);
            playerTwoPiece.setDrawingCacheEnabled(true);
            moveAvailable.setDrawingCacheEnabled(true);

        }

        public BoardViewHolder(View itemView, Bitmap p1, Bitmap p2) {
            super(itemView);
            playerOnePiece = (ImageView) itemView.findViewById(R.id.player_one_piece);
            playerTwoPiece = (ImageView) itemView.findViewById(R.id.player_two_piece);
            debugNumber = (TextView) itemView.findViewById(R.id.debugNumber);
            moveAvailable = (ImageView) itemView.findViewById(R.id.moveAvailable);
            playerOnePiece.setDrawingCacheEnabled(true);
            playerTwoPiece.setDrawingCacheEnabled(true);
            moveAvailable.setDrawingCacheEnabled(true);
            playerOnePiece.setImageBitmap(p1);
            playerTwoPiece.setImageBitmap(p2);

        }

        public View getOldOwnerPiece(MyDataSet set){
            if (set.owner==0)
                return null;
            else if(set.owner==1)
                return playerOnePiece;
            else return playerTwoPiece;
        }

        public View getNewOwnerPiece(int player){
            if (player==1)
                return playerOnePiece;
            else return playerTwoPiece;
        }

        public ImageView getMoveAvailable() {
            return moveAvailable;
        }

        public void setMoveAvailable(ImageView moveAvailable) {
            this.moveAvailable = moveAvailable;
        }

        public TextView getDebugNumber() {
            return debugNumber;
        }

        public void setDebugNumber(TextView debugNumber) {
            this.debugNumber = debugNumber;
        }

        public ImageView getPlayerOnePiece() {
            return playerOnePiece;
        }

        public void setPlayerOnePiece(ImageView playerOnePiece) {
            this.playerOnePiece = playerOnePiece;
        }

        public ImageView getPlayerTwoPiece() {
            return playerTwoPiece;
        }

        public void setPlayerTwoPiece(ImageView playerTwoPiece) {
            this.playerTwoPiece = playerTwoPiece;
        }
    }

    public void updatePiecesNumberAndClearHistory(){
        int i = 0;
        int j = 0;
        p1Pieces = 0;
        p2Pieces = 0;
        while(i<height){
            while(j<width){
                if(casas[i][j].owner==1) p1Pieces++;
                else if(casas[i][j].owner==2) p2Pieces++;
                casas[i][j].piecesToChangeOnClick.clear();
                j++;
            }
            j=0;
            i++;
        }
        callback.updatePlayerPieces(p1Pieces,p2Pieces);
    }

    public void updatePiecesNumber(){
        int i = 0;
        int j = 0;
        p1Pieces = 0;
        p2Pieces = 0;
        while(i<height){
            while(j<width){
                if(casas[i][j].owner==1) p1Pieces++;
                else if(casas[i][j].owner==2) p2Pieces++;
                j++;
            }
            j=0;
            i++;
        }
        callback.updatePlayerPieces(p1Pieces,p2Pieces);
    }

    public static class MyDataSet {
        public int owner; //0 = Vazio, 1 = Player1, 2 = Player 2;
        public ArrayList<MyDataSet> piecesToChangeOnClick;
        private BoardViewHolder attachedViewHolder;
        int linha, coluna;

        public MyDataSet(int owner) {
            this.owner = owner;
            piecesToChangeOnClick = new ArrayList<>();
        }

        public MyDataSet(MyDataSet dataSet){
            this.owner = dataSet.owner;
            this.piecesToChangeOnClick = new ArrayList<>();
            int size = dataSet.piecesToChangeOnClick.size();
            int i = 0;
            while(i<size){
                this.piecesToChangeOnClick.add(new MyDataSet(dataSet.piecesToChangeOnClick.get(i)));
                i++;
            }
        }

        public int getLinha() {
            return linha;
        }

        public void setLinha(int linha) {
            this.linha = linha;
        }

        public int getColuna() {
            return coluna;
        }

        public void setColuna(int coluna) {
            this.coluna = coluna;
        }

        public void setPiecesToChangeOnClick(ArrayList<MyDataSet> piecesToChangeOnClick) {
            this.piecesToChangeOnClick = piecesToChangeOnClick;
        }

        public BoardViewHolder getAttachedViewHolder() {
            return attachedViewHolder;
        }

        public void setAttachedViewHolder(BoardViewHolder attachedViewHolder) {
            this.attachedViewHolder = attachedViewHolder;
        }
    }

}
