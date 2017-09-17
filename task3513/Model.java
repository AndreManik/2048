package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {

    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

    protected int score;
    protected int maxTile;

    protected Stack<Tile[][]> previousStates = new Stack<>();
    protected Stack previousScores = new Stack();
    protected boolean isSaveNeeded = true;

    private void saveState(Tile[][] tiles){

        Tile[][] stackGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++){
                stackGameTiles[i][j] = new Tile();
                stackGameTiles[i][j].value = tiles[i][j].value;
            }
        }
        previousStates.push(stackGameTiles);
        previousScores.push(score);
        isSaveNeeded = false;

    }

    public void rollback(){
        if (!previousStates.isEmpty() && !previousScores.isEmpty()) {
            gameTiles = (Tile[][]) previousStates.pop();
            score = (int) previousScores.pop();
        }
    }

    public Model(){
        resetGameTiles();
        score = 0;
        maxTile = 2;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    protected void resetGameTiles(){
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++){
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private List<Tile> getEmptyTiles(){
        List<Tile> listEmpty = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++){
                if (gameTiles[i][j].value == 0){
                    listEmpty.add(gameTiles[i][j]);
                }
            }
        }
        return listEmpty;
    }

    private void addTile(){
        List<Tile> emptyList = getEmptyTiles();
        if (emptyList.size() > 0){
            emptyList.get((int)(Math.random() * emptyList.size())).value = (Math.random() < 0.9 ? 2 : 4);
        }
    }

    private boolean compressTiles(Tile[] tiles){
        boolean flag = true;
        boolean isChanged = false;
        while (flag) {
            flag = false;
            for (int i = 0; i < FIELD_WIDTH - 1; i++) {
                if (tiles[i].value == 0 && tiles[i + 1].value != 0) {
                    tiles[i] = tiles[i + 1];
                    tiles[i + 1] = new Tile();
                    flag = true;
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }

    private boolean mergeTiles(Tile[] tiles){
        boolean isChanged = false;

        for (int i = 0; i < FIELD_WIDTH - 1; i++){
                if (tiles[i].value == tiles[i + 1].value && !tiles[i].isEmpty()){
                    tiles[i].value += tiles[i + 1].value;
                    tiles[i + 1].value = 0;
                    score += tiles[i].value;
                    isChanged = true;
                }
                if (maxTile < tiles[i].value){
                    maxTile = tiles[i].value;
                }
                compressTiles(tiles);
            }

        return isChanged;
    }

    protected void left(){

        if (isSaveNeeded){
            saveState(gameTiles);
        }

        boolean isChanged = false;

        for (int i = 0; i < FIELD_WIDTH; i++){
            if (compressTiles(gameTiles[i]) || mergeTiles(gameTiles[i])){
                isChanged = true;
            }
        }
        if (isChanged){
            addTile();
        }
        isSaveNeeded = true;
    }

     private void rotateClockWay(){
         Tile tmp;
         for (int i = 0; i < FIELD_WIDTH / 2; i++) {
             for (int j = i; j < FIELD_WIDTH - 1 - i; j++) {
                 tmp = gameTiles[i][j];
                 gameTiles[i][j] = gameTiles[FIELD_WIDTH-j-1][i];
                 gameTiles[FIELD_WIDTH-j-1][i] = gameTiles[FIELD_WIDTH-i-1][FIELD_WIDTH-j-1];
                 gameTiles[FIELD_WIDTH-i-1][FIELD_WIDTH-j-1] = gameTiles[j][FIELD_WIDTH-i-1];
                 gameTiles[j][FIELD_WIDTH-i-1] = tmp;
             }
         }
    }

     void right(){
         saveState(gameTiles);
         rotateClockWay();
         rotateClockWay();
         left();
         rotateClockWay();
         rotateClockWay();
    }

     void down(){
         saveState(gameTiles);
        rotateClockWay();
        left();
        rotateClockWay();
        rotateClockWay();
        rotateClockWay();
    }

     void up(){
        saveState(gameTiles);
        rotateClockWay();
        rotateClockWay();
        rotateClockWay();
        left();
        rotateClockWay();
    }
    public boolean canMove(){
        if (!getEmptyTiles().isEmpty()) return true;

        for (int i = 0; i < FIELD_WIDTH; i ++){
            for (int j = 1; j < FIELD_WIDTH; j ++){
                if (gameTiles[i][j].value == gameTiles[i][j-1].value) return true;
            }
        }

        for (int i = 0; i < FIELD_WIDTH; i ++){
            for (int j = 1; j < FIELD_WIDTH; j ++){
                if (gameTiles[j][i].value == gameTiles[j-1][i].value) return true;
            }
        }

        return false;
    }

    public void randomMove(){
        switch (((int) (Math.random() * 100)) % 4){
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
         }
    }

    private boolean hasBoardChanged(){
        boolean flag = false;
        Tile[][] temp = previousStates.peek();
        int weight1 = 0;
        int weight2 = 0;

        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++){
                weight1 += gameTiles[i][j].value;
                weight2 += temp[i][j].value;
            }
        }
        return weight1 != weight2;
    }

    private MoveEfficiency getMoveEfficiency(Move move){
        MoveEfficiency moveEfficiency;
        move.move();

        if (hasBoardChanged()){
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        } else {
            moveEfficiency = new MoveEfficiency(-1, 0, move);
        }
        rollback();

        return moveEfficiency;
    }

    public void autoMove(){
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue(4, Collections.reverseOrder());

        priorityQueue.offer(getMoveEfficiency(this::left));
        priorityQueue.offer(getMoveEfficiency(this::right));
        priorityQueue.offer(getMoveEfficiency(this::up));
        priorityQueue.offer(getMoveEfficiency(this::down));

        Move move = priorityQueue.peek().getMove();
        move.move();
    }
}
