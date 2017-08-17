package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static final int FIELD_WIDTH = 4;                                  //Приватная константа FIELD_WIDTH = 4, определяющая ширину игрового поля.

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];    //Приватный двумерный массив gameTiles состоящий из объектов класса Tile.

    protected int score; //счет
    protected int maxTile; //максимальный вес плитки

    //Конструктор без параметров инициализирующий игровое поле и заполняющий его пустыми плитками.
    public Model(){
        resetGameTiles();
        score = 0;
        maxTile = 2;

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

    private void addTile(){
        List<Tile> emptyTiles = getEmptyTiles();

        if (emptyTiles.size() > 0) {
            int tileIndex = (int) (Math.random() * emptyTiles.size());

            emptyTiles.get(tileIndex).value = Math.random() < 0.9 ? 2 : 4;
        }

    }

    private List<Tile> getEmptyTiles(){
        List<Tile> emptyTiles = new ArrayList<>();
        int localIndex = 1;

        for (int i = 0; i < FIELD_WIDTH; i ++){
            for (int j = 0; j < FIELD_WIDTH; j ++){
                if (gameTiles[i][j].isEmpty()){
                    emptyTiles.add(gameTiles[i][j]);
                }
                localIndex++;
            }
        }
        return emptyTiles;
    }

    private boolean compressTiles(Tile[] tiles){
        boolean flag = true;
        boolean t = false;
        while (flag) {
            flag = false;
            for (int y = 0; y < FIELD_WIDTH - 1; y++) {
                if (tiles[y + 1].value != 0 && tiles[y].value == 0) {
                    tiles[y] = tiles[y + 1];
                    tiles[y + 1] = new Tile();
                    flag = true;
                    t = true;
                }
            }
        }
        return t;
    }

    private boolean mergeTiles(Tile[] tiles){
        boolean change = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].value == tiles[i+1].value && !tiles[i].isEmpty() && !tiles[i+1].isEmpty()) {
                change = true;
                tiles[i].value *= 2;
                tiles[i+1] = new Tile();
                maxTile = maxTile > tiles[i].value ? maxTile : tiles[i].value;
                score += tiles[i].value;
                compressTiles(tiles);
            }
        }
        return change;
    }

    public void left(){

        boolean isChanged = false;
        for (int y = 0; y < FIELD_WIDTH; y++){
             if (compressTiles(gameTiles[y]) || mergeTiles(gameTiles[y])){
               isChanged = true;
            }
        }
        if(isChanged)
        addTile();
    }

    private void rotateToRight() {
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

    void right() {
        rotateToRight();
        rotateToRight();
        left();
        rotateToRight();
        rotateToRight();
    }
    void up() {
        rotateToRight();
        rotateToRight();
        rotateToRight();
        left();
        rotateToRight();
    }
    void down() {
        rotateToRight();
        left();
        rotateToRight();
        rotateToRight();
        rotateToRight();
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
