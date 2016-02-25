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
    int xLoc;
    int yLoc;
    
    int index;    
    int prefW;
    int prefH;
    int xShift;
    int yShift;
    
    public ButtonActor() {
        Random rand = new Random();
        current = new Texture(new FileHandle("..\\assets\\" + (rand.nextInt(6) + 1) + "priority.png"));
        up = current;
        down = new Texture(new FileHandle("..\\assets\\" + 0 + "priority.png"));        
    }
    
    public void setup() {
        xLoc = (index / 11) * prefW;
        yLoc = (index % 11) * prefH;
        setBounds(xLoc, yLoc, prefW, prefH);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(current, xLoc, yLoc, prefW, prefH);
    }
}