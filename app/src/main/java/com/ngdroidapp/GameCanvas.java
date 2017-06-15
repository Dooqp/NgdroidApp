package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Vector;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    //tileset var
    private Bitmap tileset, spritesheet, bullet;
    //end var



    private int kareno, speedx, speedy, spritexcord, spriteycord, animationo, animationtype, touchx, touchy, speed, bulletoffsetx_temp, bulletoffsety_temp, bulletspeed, bulletx_temp, bullety_temp;

    private Rect tilesrc, tiledst, spritesrc, spritedst, bulletsrc;

    public Vector<Rect>bulletdst2;
    public Vector<Integer>bulletx2, bullety2, bulletoffsetx2, bulletoffsety2, bulletspeedx2, bulletspeedy2, bulletspeed2;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {


        bulletdst2 = new Vector<Rect>();
        bulletx2 = new Vector<Integer>();
        bullety2 = new Vector<Integer>();
        bulletspeedx2 = new Vector<Integer>();
        bulletspeedy2 = new Vector<Integer>();
        bulletspeed2 = new Vector<Integer>();
        bulletoffsetx2 = new Vector<Integer>();
        bulletoffsety2 = new Vector<Integer>();

        //upload tile set into the RAM
        tileset = Utils.loadImage(root, "images/tilea2.png");
        //upload ends

        spritesheet = Utils.loadImage(root, "images/cowboy.png");

        bullet = Utils.loadImage(root, "images/bullet.png");

        //get coordinates from tile set
        tilesrc = new Rect();
        spritesrc = new Rect();

        bulletsrc = new Rect();



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

        bulletoffsetx_temp = 256;
        bulletoffsety_temp = 128;


        bulletspeed  = 0;

        bullety_temp = 0;
        bulletx_temp = 0;
    }



    public void update() {
        tilesrc.set(0,0,64,64);

        spritexcord += speedx;
        spriteycord += speedy;

        /*bulletx_temp += bulletspeedx;
        bullety_temp += bulletspeedy;*/


        for(int i = 0; i< bulletx2.size(); i++){
            bulletx2.set(i, bulletspeedx2.elementAt(i) + bulletx2.elementAt(i));
            bullety2.set(i, bulletspeedy2.elementAt(i) + bullety2.elementAt(i));

            if(bulletx2.elementAt(i) > getWidth() || bulletx2.elementAt(i) < 0 ||  bullety2.elementAt(i) > getHeight() ||  bullety2.elementAt(i) < 0 ){
                bulletx2.removeElementAt(i);
                bullety2.removeElementAt(i);
                /*bulletoffsetx2.removeElementAt(i);
                bulletoffsety2.removeElementAt(i);*/
                bulletdst2.removeElementAt(i);
                bulletspeedx2.removeElementAt(i);
                bulletspeedy2.removeElementAt(i);
            }
            Log.i("Control", String.valueOf(bulletx2.size()));
        }

        if(spritexcord + 256 > getWidth() || spritexcord < 0){
            speedx= 0;

        }

        if(spriteycord + 256 > getHeight() || spriteycord < 0){
            speedy = 0;

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
        /*if(speedx > 0){
                    animationtype = 0;
                }

                else if(speedy > 0){
                    animationtype = 9;
                }*/

        if(Math.abs(speedx) > 0 || Math.abs(speedy) > 0){
            animationo = 1;
        }

        else{
            animationo = 0;
        }

        spritesrc.set(kareno*128, animationtype*128, (kareno+1)*128, (animationtype+1)*128);
        spritedst.set(spritexcord, spriteycord, spritexcord + 256, spriteycord + 256);

        bulletsrc.set(0, 0, 70, 70);
        for(int i = 0 ; i<bulletx2.size(); i++){
        bulletdst2.elementAt(i).set(bulletx2.elementAt(i), bullety2.elementAt(i), bulletx2.elementAt(i)+32, bullety2.elementAt(i)+32);
        }
    }

    public void draw(Canvas canvas) {


        for(int i=0; i<getWidth(); i+=128) {
            for(int j=0; j<getHeight(); j+=128) {
                tiledst.set(i, j, i+128, j+128);
                canvas.drawBitmap(tileset, tilesrc, tiledst, null);
            }
        }

        canvas.drawBitmap(spritesheet, spritesrc, spritedst, null);


        for(int i = 0; i<bulletdst2.size(); i++) {
            canvas.drawBitmap(bullet, bulletsrc, bulletdst2.elementAt(i), null);
        }
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
            bulletspeed = 32;


            if(animationtype == 0){
                bulletspeedx2.add(bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp = 256;
                bulletoffsety_temp = 128;
            }
            else if(animationtype == 1){
                bulletspeedx2.add(-bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp = 0;
                bulletoffsety_temp = 128;
            }
            else if(animationtype == 9){

                bulletspeedy2.add(bulletspeed);
                bulletspeedx2.add(0);
                bulletoffsetx_temp = 128;
                bulletoffsety_temp = 256;
            }
            else if(animationtype == 5){
                bulletspeedx2.add(0);
                bulletspeedy2.add(-bulletspeed);
                bulletoffsetx_temp = 128;
                bulletoffsety_temp = 0;
            }
            bulletx2.add(spritexcord + bulletoffsetx_temp);
            bullety2.add(spriteycord + bulletoffsety_temp);

            bulletx_temp = spritexcord + bulletoffsetx_temp;
            bullety_temp = spriteycord + bulletoffsety_temp;

            bulletdst2.add(new Rect(bulletx_temp, bullety_temp, bulletx_temp +32, bullety_temp +32));
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
