package feliperrm.reversiandroid.ArtificialInteligence;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Random;

import feliperrm.reversiandroid.Adapters.ReversiBoardAdapter;

/**
 * Created by Felipe on 09/09/2015.
 */
public class HardIntelligence extends BaseIntelligence {

    private final int profundidade_max = 2;
    private int[][] scoreTable;
    private int height, width;

    public HardIntelligence(int boardHeight, int boardWidth) {
        super(boardHeight, boardWidth);
        super.name = super.name + " (CPU Hard)";
    }

    @Override
    public void play(final ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable, final ReversiBoardAdapter.MyDataSet[][] board, int height, int width) {
        this.height = height;
        this.width = width;
        setUpScoreTable(height);
        /* Executamos o MiniMax em uma thread asyncrona, de modo a n�o sobrecarregar a UI Thread */
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected Integer doInBackground(Void... params) {
                return miniMax(board,0);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if(integer>=movesAvailable.size()) {
                    Random random = new Random(System.currentTimeMillis());
                    integer = random.nextInt(movesAvailable.size());
                }
                movesAvailable.get(integer).getAttachedViewHolder().getMoveAvailable().performClick();

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private int miniMax(ReversiBoardAdapter.MyDataSet[][] board,int profundidade){
        int bestMovePosition = 0;
        int bestScore = -99;
        ReversiBoardAdapter.MyDataSet[][] boardCopy = copyGameBoard(board);
        ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable = configureBoardForNewRound(boardCopy,2 - (profundidade%2));
        int size = movesAvailable.size();
        int i = 0;
        while(i<size){
            ReversiBoardAdapter.MyDataSet[][] boardCopy2 = copyGameBoard(board);
            ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable2 = configureBoardForNewRound(boardCopy2,2 - (profundidade%2));
            makeMove(movesAvailable2.get(i), 2 - (profundidade % 2));
            int score  = computeScore(boardCopy2);
            if(score>bestScore){
                bestScore = score;
                bestMovePosition = i;
            }
            i++;

        }
        if(profundidade==profundidade_max){
            return computeScore(board);
        }
        while (i<size) {
            // Max
            if(profundidade==0)
                return bestMovePosition;
            if (profundidade % 2 == 0) {
                ReversiBoardAdapter.MyDataSet[][] boardCopy2 = copyGameBoard(board);
                ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable2 = configureBoardForNewRound(boardCopy2,2 - (profundidade%2));
                makeMove(movesAvailable2.get(i),2-(profundidade%2));
                int score = miniMax(boardCopy,profundidade+1);
                if(score>bestScore){
                    bestScore = score;
                    bestMovePosition = i;
                }
            }
            //Min
            else{
                ReversiBoardAdapter.MyDataSet[][] boardCopy2 = copyGameBoard(board);
                ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable2 = configureBoardForNewRound(boardCopy2,2 - (profundidade%2));
                makeMove(movesAvailable2.get(i),2-(profundidade%2));
                int score = miniMax(boardCopy,profundidade+1);
                if(score<bestScore){
                    bestScore = score;
                    bestMovePosition = i;
                }
            }
            i++;
        }
        return bestMovePosition;
    }

    private void makeMove(ReversiBoardAdapter.MyDataSet casa, int player){
        int i = 0;
        int size = casa.piecesToChangeOnClick.size();
        casa.owner = player;
        while(i<size){
            casa.piecesToChangeOnClick.get(i).owner = player;
            i++;
        }
    }

    private ArrayList<ReversiBoardAdapter.MyDataSet> configureBoardForNewRound(ReversiBoardAdapter.MyDataSet[][] casas, int playerPlaying){
        int i = 0;
        int j = 0;
        ArrayList<ReversiBoardAdapter.MyDataSet> movesAvailable = new ArrayList<>();
        while (i<height){
            while(j<width){
                ReversiBoardAdapter.MyDataSet setAtual = casas[i][j];
                testaHorizontais(setAtual,i,j,casas,playerPlaying);
                testaVertical(setAtual, i, j, casas, playerPlaying);
                testaDiagonalPrincipal(setAtual,i,j,casas,playerPlaying);
                if(setAtual.piecesToChangeOnClick.size()>0){
                    movesAvailable.add(setAtual);
                }
                j++;
            }
            j= 0;
            i++;
        }
        return movesAvailable;
    }

    private void testaHorizontais(ReversiBoardAdapter.MyDataSet dataSet, int linha, int coluna, ReversiBoardAdapter.MyDataSet[][] casas, int playerPlaying){
        /* Percorre para a esquerda at� chegar no limite ou achar uma pe�a da cor do jogador.
           Nesse mesmo loop, vai adicionando as pe�as do outro jogador em um array, pois caso
           encontremos uma pe�a do nosso jogador depois dessas pe�as, elas ser�o viradas na jogada
         */
        ReversiBoardAdapter.MyDataSet temp;
        int colunaTemp = coluna;
        boolean playerPieceInTheEnd = false;
        ArrayList<ReversiBoardAdapter.MyDataSet> tempDataSets = new ArrayList<>();

        while(colunaTemp>0){
            temp = casas[linha][colunaTemp-1];
            colunaTemp--;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
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
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }

    private void testaVertical(ReversiBoardAdapter.MyDataSet dataSet, int linha, int coluna, ReversiBoardAdapter.MyDataSet[][] casas, int playerPlaying){
        /* Percorre para cima at� chegar no limite ou achar uma pe�a da cor do jogador.
           Nesse mesmo loop, vai adicionando as pe�as do outro jogador em um array, pois caso
           encontremos uma pe�a do nosso jogador depois dessas pe�as, elas ser�o viradas na jogada
         */
        ReversiBoardAdapter.MyDataSet temp;
        int linhaTemp = linha;
        boolean playerPieceInTheEnd = false;
        ArrayList<ReversiBoardAdapter.MyDataSet> tempDataSets = new ArrayList<>();

        while(linhaTemp>0){
            temp = casas[linhaTemp-1][coluna];
            linhaTemp--;
            if(temp.owner!=playerPlaying) {
                if(temp.owner!=0)
                    tempDataSets.add(temp);
                else
                    break;
            }
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
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
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }

    private void testaDiagonalPrincipal(ReversiBoardAdapter.MyDataSet dataSet, int linha, int coluna, ReversiBoardAdapter.MyDataSet[][] casas, int playerPlaying){
        /* Percorre na diagonal \ para cima at� chegar no limite ou achar uma pe�a da cor do jogador.
           Nesse mesmo loop, vai adicionando as pe�as do outro jogador em um array, pois caso
           encontremos uma pe�a do nosso jogador depois dessas pe�as, elas ser�o viradas na jogada
         */
        ReversiBoardAdapter.MyDataSet temp;
        int linhaTemp = linha;
        int colunaTemp = coluna;
        boolean playerPieceInTheEnd = false;
        ArrayList<ReversiBoardAdapter.MyDataSet> tempDataSets = new ArrayList<>();

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
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
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
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }

    private void testaDiagonalSecundaria(ReversiBoardAdapter.MyDataSet dataSet, int linha, int coluna, ReversiBoardAdapter.MyDataSet[][] casas, int playerPlaying){
        /* Percorre na diagonal / para cima at� chegar no limite ou achar uma pe�a da cor do jogador.
           Nesse mesmo loop, vai adicionando as pe�as do outro jogador em um array, pois caso
           encontremos uma pe�a do nosso jogador depois dessas pe�as, elas ser�o viradas na jogada
         */
        ReversiBoardAdapter.MyDataSet temp;
        int linhaTemp = linha;
        int colunaTemp = coluna;
        boolean playerPieceInTheEnd = false;
        ArrayList<ReversiBoardAdapter.MyDataSet> tempDataSets = new ArrayList<>();

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
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
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
            else{ // Achamos uma pe�a do jogador que est� jogando, encerrar o loop
                playerPieceInTheEnd = true;
                break;
            }
        }
        if (!playerPieceInTheEnd)
            tempDataSets.clear();

        dataSet.piecesToChangeOnClick.addAll(tempDataSets);
        tempDataSets.clear();
    }


    private int computeScore(ReversiBoardAdapter.MyDataSet[][] board){
        int i = 0;
        int j = 0;
        int score=0;
        while (i<height){
            while(j<width){
                if(board[i][j].owner==2)
                    score = score + scoreTable[i][j];
                j++;
            }
            j=0;
            i++;
        }
        return score;
    }

    private ReversiBoardAdapter.MyDataSet[][] copyGameBoard(ReversiBoardAdapter.MyDataSet[][] board){
        /* Tentando fazer uma 'deep coopy' da matriz de pe�as, pois assim n�o alteraremos o estado original do jogo ao simularmos as jogadas */
        ReversiBoardAdapter.MyDataSet[][] b = new ReversiBoardAdapter.MyDataSet[height][width];
        int i=0;
        int j=0;
        while (i<height){
            while (j<width){
                b[i][j] = new ReversiBoardAdapter.MyDataSet(board[i][j]);
                j++;
            }
            j=0;
            i++;
        }
        /*----------------------------------------------------------------------*/
        return b;
    }

    private void setUpScoreTable(int size){
        if(size==9) {
            scoreTable = new int[][]{
                    {99, -8, 8, 6, 6, 6, 8, -8, 99},
                    {-8, -24, -4, -3, -3, -3, -4, -24, -8},
                    {8, -4, 7, 4, 4, 4, 7, -4, 8},
                    {6, -3, 4, 0, 0, 0, 4, -3, 6},
                    {6, -3, 4, 0, 0, 0, 4, -3, 6},
                    {6, -3, 4, 0, 0, 0, 4, -3, 6},
                    {8, -4, 7, 4, 4, 4, 7, -4, 8},
                    {-8, -24, -4, -3, -3, -3, -4, -24, -8},
                    {99, -8, 8, 6, 6, 6, 8, -8, 99}
            };
        }
        else if(size==8) {
            scoreTable = new int[][]{
                    {99, -8, 8, 6, 6, 8, -8, 99},
                    {-8, -24, -4, -3, -3, -4, -24, -8},
                    {8, -4, 7, 4, 4, 7, -4, 8},
                    {6, -3, 4, 0, 0, 4, -3, 6},
                    {6, -3, 4, 0, 0, 4, -3, 6},
                    {8, -4, 7, 4, 4, 7, -4, 8},
                    {-8, -24, -4, -3, -3, -4, -24, -8},
                    {99, -8, 8, 6, 6, 8, -8, 99}
            };
        }
        else if(size==7){
            scoreTable = new int[][]{
                    {99, -8, 8, 6, 8, -8, 99},
                    {-8, -24, -4,-3, -4, -24, -8},
                    {8, -4, 7, 4, 7, -4, 8},
                    {6, -3, 4, 0, 4, -3, 6},
                    {8, -4, 7, 4, 7, -4, 8},
                    {-8, -24, -4, -3, -4, -24, -8},
                    {99, -8, 8, 6, 8, -8, 99}
            };
        }
        else if(size==6){
            scoreTable = new int[][]{
                    {99, -8, 8, 6, -8, 99},
                    {-8, -24, -4, -4, -24, -8},
                    {8, -4, 7, 4, -4, 8},
                    {6, -3, 4, 0, -3, 6},
                    {-8, -24, -4, -4, -24, -8},
                    {99, -8, 8, 6, -8, 99}
            };
        }
        else if(size==5){
            scoreTable = new int[][]{
                    {99, -8, 8, -8, 99},
                    {8, -4, 7, -4, 8},
                    {6, -3, 0, -3, 6},
                    {-8, -24, -4, -24, -8},
                    {99, -8, 8, -8, 99}
            };
        }
        else if(size==4){
            scoreTable = new int[][]{
                    {99, -8, -8, 99},
                    {8, -4, -4, 8},
                    {6, -3, -3, 6},
                    {99, -8, -8, 99}
            };
        }
    }




}
