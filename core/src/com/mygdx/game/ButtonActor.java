package com.mygdx.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Random;

public class ButtonActor extends Actor {
    Texture current;
    Texture up;
    Texture down;
    Texture transparent;
    int xLoc;
    int yLoc;
    int priority;
    boolean inDeletionProcess;
    
    int index;    
    int prefW;
    int prefH;
    int xShift;
    int yShift;
    
    public ButtonActor() {
        Random rand = new Random();
        priority = (rand.nextInt(6) + 1);
        current = new Texture(new FileHandle("..\\assets\\" + priority + "priority.png"));
        up = current;
        down = new Texture(new FileHandle("..\\assets\\" + 0 + "priority.png"));   
        transparent = new Texture(new FileHandle("..\\assets\\transparent.png"));        
        
        inDeletionProcess = false;        
    }
    
    public void setup() {
        xLoc = (index / 11) * prefW;
        yLoc = (index % 11) * prefH;
        setBounds(xLoc, yLoc, prefW, prefH);
    }
    
    public boolean matches(int a) {
        return priority == a;
    }
   
    public boolean adjacent(int x, int y) {
        int a = Math.abs((index / 11) - x);
        int b = Math.abs((index % 11) - y);
        
        //System.out.println(a + " " + b);
        //System.out.println();       
        return a + b <= 1;
    }
    
    public int xDistanceTo(int x) {
        int a = Math.abs((index / 11) - x);
        
        return a;
    }
    
    public int yDistanceTo(int y) {
        int a = Math.abs((index % 11) - y);
        
        return a;
    }
    
    public int determineQuadrant() {
        if (xLoc < 6 && yLoc > 5) {
            return 0;
        }
        
        if (xLoc > 5 && yLoc > 4) {
            return 1;
        }
        
        if (xLoc > 4 && yLoc < 5) {
            return 2;
        }
        
        if (xLoc < 5 && yLoc > 6) {
            return 3;
        }        
        
        return 1;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(current, xLoc, yLoc, prefW, prefH);
    }
}