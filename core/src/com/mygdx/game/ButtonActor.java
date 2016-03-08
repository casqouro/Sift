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
        
    int index;        
    int xShift;
    int yShift;   
    
    int prefW;
    int prefH;
    int xLoc;
    int yLoc;    
    int priority;   
    
    boolean inDeletionProcess;    
    
    public ButtonActor() {
        Random rand = new Random();
        priority = (rand.nextInt(5) + 1);
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
    
    public void newTile() {
        Random rand = new Random();
        priority = (rand.nextInt(5) + 1);
        current = new Texture(new FileHandle("..\\assets\\" + priority + "priority.png"));
        up = current;        
    }
    
    public void newTile(int predetermined) {
        priority = predetermined;
        current = new Texture(new FileHandle("..\\assets\\" + priority + "priority.png"));
        up = current;          
    }
    
    public boolean matches(int priority) {
        return this.priority == priority;
    }
              
    public boolean adjacent(int index) {
        int difference = Math.abs(this.index - index);
        return difference == 1 || difference == 11;
    }   
    
    public void swapPriority(int newPriority) {
        this.priority = newPriority;
        current = new Texture(new FileHandle("..\\assets\\" + priority + "priority.png"));
        up = new Texture(new FileHandle("..\\assets\\" + priority + "priority.png"));        
    }
            
    public int determineQuadrant() {
        if ((index / 11) < 6 && (index % 11) > 5) {
            return 0; // Upper Left Quadrant
        }
        
        if ((index / 11) > 5 && (index % 11) > 4) {
            return 1; // Upper Right Quadrant
        }
        
        if ((index / 11) > 4 && (index % 11) < 5) {
            return 2; // Lower Right Quadrant
        }
        
        return 3; // Lower Left Quadrant;      
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(current, xLoc, yLoc, prefW, prefH);
    }
}