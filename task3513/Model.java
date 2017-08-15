package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static final int FIELD_WIDTH = 4;                                  //Приватная константа FIELD_WIDTH = 4, определяющая ширину игрового поля.
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

    private void compressTiles(Tile[] tiles){
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int y = 0; y < FIELD_WIDTH - 1; y++) {
                if (tiles[y + 1].value != 0 && tiles[y].value == 0) {
                    tiles[y].value = tiles[y + 1].value;
                    tiles[y + 1].value = 0;
                    flag = true;
                }
            }
        }
    }

    private void mergeTiles(Tile[] tiles){
            for (int y = 0; y < FIELD_WIDTH - 1; y++) {
                if (tiles[y + 1].value == tiles[y].value ) {
                    tiles[y].value += tiles[y].value;
                    tiles[y + 1].value = 0;
                    if (tiles[y].value > maxTile){
                        maxTile = tiles[y].value;
                    }
                    score += tiles[y].value;
                }
            compressTiles(tiles);
        }
    }


}
