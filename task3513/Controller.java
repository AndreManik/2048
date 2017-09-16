package com.javarush.task.task35.task3513;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {

    private Model model;
    private View view;

    private static final int WINNING_TILE = 2048;

    public Tile[][] getGameTiles(){
        return model.getGameTiles();
    }

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    public View getView() {
        return view;
    }

    public int getScore(){
        return model.score;
    }

    public void resetGame(){
        model.score = 0;
        view.isGameLost = false;
        view.isGameWon = false;
        model.resetGameTiles();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
               resetGame();

        if (model.canMove() == false) view.isGameLost = true;

        if (view.isGameLost == false && view.isGameWon == false){

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    model.left();
                    break;
                case KeyEvent.VK_UP:
                    model.up();
                    break;
                case KeyEvent.VK_RIGHT:
                    model.right();
                    break;
                case KeyEvent.VK_DOWN:
                    model.down();
                    break;
            }
        }

        if (model.maxTile >= WINNING_TILE) view.isGameWon = true;
        view.repaint();
    }
}
