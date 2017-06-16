package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;
import java.util.Vector;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.core.NgMediaPlayer;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    //tileset var
    private Bitmap tileset, spritesheet, bullet, enemy, explosion;
    //end var

    public int sound_explosion;



    private NgMediaPlayer sound_background;

    private boolean enemyexist, exploded, turnbool;

    private int kareno, speedx, speedy, spritexcord, spriteycord, animationo, animationtype, touchx, touchy, speed, bulletoffsetx_temp, bulletoffsety_temp, bulletspeed, bulletx_temp, bullety_temp, explosionframeno;

    private int enemyspeedx, enemyspeedy, enemyx, enemyy, turningpoint;
    private Random enemyrand;
    private Rect tilesrc, tiledst, spritesrc, spritedst, bulletsrc, enemysrc, enemydst, explosionsrc, explosiondst;

    public Vector<Rect> bulletdst;
    public Vector<Integer>bulletx2, bullety2, bulletoffsetx2, bulletoffsety2, bulletspeedx2, bulletspeedy2, bulletspeed2;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {


        bulletdst = new Vector<Rect>();
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

//region enemy
        enemy = Utils.loadImage(root, "images/mainship03.png");
        enemysrc = new Rect();
        enemydst = new Rect();
        enemyexist = true;

        enemyspeedx = 10;
        enemyspeedy = 0;
        enemyx = getWidthHalf()-128;
        enemyy = getHeight()-256;
        turningpoint = getWidth();
        turnbool = true;
        enemyrand = new Random();
//endregion

        //get coordinates from tile set
        tilesrc = new Rect();
        spritesrc = new Rect();

        bulletsrc = new Rect();


        explosion = Utils.loadImage(root, "images/exp2_0.png");
        explosionsrc = new Rect();
        explosiondst = new Rect();
        explosionframeno = 0;
        exploded = false;

        try {
            sound_explosion = root.soundManager.load("sounds/se2.wav");
        } catch(Exception e){
            e.printStackTrace();
        }

        sound_background = new NgMediaPlayer(root);
        sound_background.load("sounds/m2.mp3");
        sound_background.setVolume(0.8f);
        sound_background.prepare();
        sound_background.start();
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

        if(turnbool){
            if(enemyspeedx > 0){
                turningpoint = enemyrand.nextInt(getWidth() -256 - enemyx - 50) + enemyx;
            }
            else if(enemyspeedx < 0){
                turningpoint = enemyrand.nextInt(enemyx);
            }
            turnbool = false;
        }
        if(enemyspeedx > 0 && enemyx > turningpoint){
            turnbool = true;
            enemyspeedx = -enemyspeedx;
        }
        else if (enemyspeedx < 0 && enemyx < turningpoint){
            turnbool = true;
            enemyspeedx = -enemyspeedx;
        }

        if(enemyexist){
        enemysrc.set(0, 0, 64, 64);
        //enemydst.set(getWidthHalf()-128, getHeight()-256, getWidthHalf()+128, getHeight());
        enemydst.set(enemyx, enemyy, enemyx+256, enemyy+256);
        }

        for(int i = 0; i< bulletdst.size(); i++){
            if(enemydst.contains(bulletdst.elementAt(i))){
                explosiondst.set(bulletx2.elementAt(i)-64, bullety2.elementAt(i)-64, bulletx2.elementAt(i)+64, bullety2.elementAt(i)+64);
                bulletx2.removeElementAt(i);
                bullety2.removeElementAt(i);
                bulletdst.removeElementAt(i);
                bulletspeedx2.removeElementAt(i);
                bulletspeedy2.removeElementAt(i);
                enemyexist = false;
                enemydst.set(0, 0, 0, 0);
                exploded = true;
                root.soundManager.play(sound_explosion);

            }
        }

        if(exploded){
            explosionsrc = getExplosionFrame(explosionframeno);
            explosionframeno+=2;
        }
        if(explosionframeno > 15){
            explosionframeno = 0;
            exploded = false;
        }
        spritexcord += speedx;
        spriteycord += speedy;

        enemyx += enemyspeedx;
        enemyy += enemyspeedy;

        if(enemyx+256 > getWidth() || enemyx < 0){
            enemyspeedx = -enemyspeedx;
        }

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
                bulletdst.removeElementAt(i);
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
        bulletdst.elementAt(i).set(bulletx2.elementAt(i), bullety2.elementAt(i), bulletx2.elementAt(i)+32, bullety2.elementAt(i)+32);
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


        for(int i = 0; i< bulletdst.size(); i++) {
            canvas.drawBitmap(bullet, bulletsrc, bulletdst.elementAt(i), null);
        }

        if(enemyexist){
        canvas.drawBitmap(enemy, enemysrc, enemydst, null);
        }

        if(exploded){
            canvas.drawBitmap(explosion, explosionsrc, explosiondst, null);
        }
    }


    public Rect getExplosionFrame(int frameNo){
        frameNo = 15-frameNo;
        Rect temp = new Rect();
        temp.set((frameNo%4)*64, ((frameNo/4)*64), ((frameNo%4) + 1)*64, ((frameNo/4)+1)*64);
        return temp;
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

            bulletdst.add(new Rect(bulletx_temp, bullety_temp, bulletx_temp +32, bullety_temp +32));
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
