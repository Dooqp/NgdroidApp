package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    //tileset var
    private Bitmap tileset, spritesheet;
    //end var



    private int kareno, speedx, speedy, spritexcord, spriteycord, animationo, animationtype, touchx, touchy, speed;

    private Rect tilesrc, tiledst, spritesrc, spritedst;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {

        //upload tile set into the RAM
        tileset = Utils.loadImage(root, "images/tilea2.png");
        //upload ends

        spritesheet = Utils.loadImage(root, "images/cowboy.png");

        //get coordinates from tile set
        tilesrc = new Rect();
        spritesrc = new Rect();

        //we'll draw dst
        tiledst = new Rect();
        spritedst = new Rect();

        kareno = 0;
        speedx = 0;
        speedy = 0;
        speed = 16;

        spritexcord = 0;
        spriteycord = 0;
        animationo = 1;
        animationtype = 0;
    }



    public void update() {



    }

    public void draw(Canvas canvas) {

        tilesrc.set(0,0,64,64);
        for(int i=0; i<getWidth(); i+=128) {
            for(int j=0; j<getHeight(); j+=128) {
                tiledst.set(i, j, i+128, j+128);
                canvas.drawBitmap(tileset, tilesrc, tiledst, null);
            }
        }



        spritexcord += speedx;
        spriteycord += speedy;

        if(spritexcord + 256 > getWidth()){
            spritexcord = getWidth()-256;
            animationo = 0;
        }

        if(spriteycord + 256 > getHeight()){
            spriteycord = getHeight() - 256;
            animationo = 0;
        }

        if(animationo == 1) {
            kareno++;
        }

        else if(animationo == 0){
            kareno = 0;
        }

        if(kareno > 8) {
            kareno = 1;
        }

       /* if(speedx > 0){
            animationtype = 0;
        }

        else if(speedy > 0){
            animationtype = 9;
        }*/

        spritesrc.set(kareno*128, animationtype*128, (kareno+1)*128, (animationtype+1)*128);
        spritedst.set(spritexcord, spriteycord, spritexcord + 256, spriteycord + 256);

        canvas.drawBitmap(spritesheet, spritesrc, spritedst, null);
    }


    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y) {
        touchx = x;
        touchy = y;
    }

    public void touchMove(int x, int y) {
    }

    public void touchUp(int x, int y) {
        if(x - touchx > 100){
            speedx = speed;
            speedy = 0;
            animationo = 1;
            animationtype = 0;
        }
        else if(touchx - x > 100){
            speedx = -speed;
            speedy = 0;
            animationo = 1;
            animationtype = 1;

        }
        else if(touchy - y > 100){
            speedy = -speed;
            speedx = 0;
            animationo = 1;
            animationtype = 5;
        }

        else if(y - touchy > 100){
            speedy = speed;
            speedx = 0;
            animationo = 1;
            animationtype = 9;
        }
        else{
            speedx = 0;
            speedy = 0;
            animationo = 0;
        }
    }


    public void pause() {

    }


    public void resume() {

    }


    public void reloadTextures() {

    }


    public void showNotify() {
    }

    public void hideNotify() {
    }

}
