package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

    protected int score;
    protected int maxTile;

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
        boolean isChanged = false;

        for (int i = 0; i < FIELD_WIDTH; i++){
            if (compressTiles(gameTiles[i]) || mergeTiles(gameTiles[i])){
                isChanged = true;
            }
        }
        if (isChanged){
            addTile();
        }
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
         rotateClockWay();
         rotateClockWay();
         left();
         rotateClockWay();
         rotateClockWay();
    }

     void down(){
        rotateClockWay();
        left();
        rotateClockWay();
        rotateClockWay();
        rotateClockWay();
    }

     void up(){
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


}
