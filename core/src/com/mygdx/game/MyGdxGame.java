package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyGdxGame extends ApplicationAdapter {
Stage stage;
ButtonActor[][] actorSet;
LinkedList<ButtonActor> touchList;
int prefW;
int prefH;

boolean touched;
int touchnum;

boolean quadZero = false;
boolean quadOne = false;
boolean quadTwo = false;
boolean quadThree = false;

/* My current problem is that the bounds of a listener are set when the listener
   is created.

   I can problem create a custom listener and update its bounds when the button
   is moved.

   See here: http://stackoverflow.com/questions/24219170/libgdx-custom-click-listener
*///
    
    @Override
	public void create () {
            stage = new Stage();
            stage.addListener(new KeyProcessor());
            Gdx.input.setInputProcessor(stage);
            prefW = 30;
            prefH = 30;     
            
            touchList = new LinkedList<>();
            
            try {
                setup();
            } catch (IOException ex) {
                Logger.getLogger(MyGdxGame.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
        private void setup() throws IOException {
            BufferedReader br = new BufferedReader(new FileReader("..\\assets\\layout_physical.txt")); 
            
            String line = br.readLine();
            String[] token;         

            actorSet = new ButtonActor[11][11];
            while (line != null) {   
                token = line.split(" ");

                int index = Integer.parseInt(token[0]);
                int xShift = Integer.parseInt(token[1]);                
                int yShift = Integer.parseInt(token[2]);   
                int row = index / 11;
                int col = index % 11; 
                
                ButtonActor actor = new ButtonActor();                
                actorSet[row][col] = actor;
                actor.index = index;
                actor.prefW = prefW;
                actor.prefH = prefH;                   
                actor.xShift = xShift;
                actor.yShift = yShift;   
                actor.setup();               
                actor.addListener(new myListener(actor));
                stage.addActor(actor);
                
                line = br.readLine();            
            }    
        }
        
        /* Deleted tile placement when a row or column already has deleted tiles
           is occassionally problematic, but I'm not entirely sure why.
        
           This only occurs because I don't clean up white '0' (deletion) tiles.
           Once I start cleaning them up the logic should be fine.
        
           However, the game is kinda fun as is!  I might want to push this out,
           and to do so I need to fix this bug.
        */
        private void postDeletionMovement() {
            touchList.stream().forEach((e) -> {
                e.inDeletionProcess = true;                        
                
                int quadrant = e.determineQuadrant();
                switch (quadrant) {
                    case 0: 
                        quadZero = true;                             
                        break;
                    case 1:
                        quadOne = true;
                        break;
                    case 2:
                        quadTwo = true;
                        break;
                    case 3:
                        quadThree = true;
                        break;
                }                 
            });
            
            if (quadZero == true) { // UPPER LEFT
                for (int adjust = 0; adjust < 5; adjust++) {
                    LinkedList<Integer> priorityList = new LinkedList<>();

                    // Add the priority of each deleted button to a list.
                    // Reduce the list-size for each successively smaller tier.
                    for (int a = 5; a >= 0 + adjust; a--) {
                        if (!actorSet[a][6 + adjust].inDeletionProcess) {
                            priorityList.add(actorSet[a][6 + adjust].priority);
                        }
                    }

                    // Shift remaining buttons to appropriate positions
                    int length = priorityList.size();                
                    for (int a = 0; a < length; a++) {
                        actorSet[5 - a][6 + adjust].swapPriority(priorityList.removeFirst());
                    }

                    // Generate new buttons for empty squares
                    for (int a = 5 - length; a >= 0 + adjust; a--) {
                        actorSet[a][6 + adjust].swapPriority(0);
                    }
                }
            }
            
            if (quadOne == true) { // UPPER RIGHT
                for (int adjust = 0; adjust < 5; adjust++) {
                    LinkedList<Integer> priorityList = new LinkedList<>();

                    // Add the priority of each deleted button to a list.
                    // Reduce the list-size for each successively smaller tier.
                    for (int a = 5; a <= 10 - adjust; a++) {
                        if (!actorSet[6 + adjust][a].inDeletionProcess) {
                            priorityList.add(actorSet[6 + adjust][a].priority);
                        }
                    }
                                                       
                    // Shift remaining buttons to appropriate positions
                    int length = priorityList.size();                
                    for (int a = 0; a < length; a++) {
                        actorSet[6 + adjust][5 + a].swapPriority(priorityList.removeFirst());
                    }
 
                    // Generate new buttons for empty squares
                    for (int a = 5 + length; a <= 10 - adjust; a++) {
                        actorSet[6 + adjust][a].swapPriority(0);
                    }
                }               
            }       
            
            if (quadTwo == true) { // LOWER RIGHT
                for (int adjust = 0; adjust < 5; adjust++) {
                    LinkedList<Integer> priorityList = new LinkedList<>();

                    // Add the priority of each deleted button to a list.
                    // Reduce the list-size for each successively smaller tier.
                    for (int a = 5; a <= 10 - adjust; a++) {
                        if (!actorSet[a][4 - adjust].inDeletionProcess) {
                            priorityList.add(actorSet[a][4 - adjust].priority);
                        }
                    }                    

                    // Shift remaining buttons to appropriate positions                    
                    int length = priorityList.size();
                    for (int a = 0; a < length; a++) {
                        actorSet[5 + a][4 - adjust].swapPriority(priorityList.removeFirst());
                    }

                    // Generate new buttons for empty squares
                    for (int a = 5 + length; a <= 10 - adjust; a++) {
                        actorSet[a][4 - adjust].swapPriority(0);
                    }
                }
            }    
            
            if (quadThree == true) { // LOWER LEFT
                for (int adjust = 0; adjust < 5; adjust++) {
                    LinkedList<Integer> priorityList = new LinkedList<>();

                    // Add the priority of each deleted button to a list.
                    // Reduce the list-size for each successively smaller tier.
                    for (int a = 5; a >= 0 + adjust; a--) {
                        if (!actorSet[4 - adjust][a].inDeletionProcess) {
                            priorityList.add(actorSet[4 - adjust][a].priority);
                        }
                    }
                                                                   
                    // Shift remaining buttons to appropriate positions
                    int length = priorityList.size();                
                    for (int a = 0; a < length; a++) {
                        actorSet[4 - adjust][5 - a].swapPriority(priorityList.removeFirst());
                    }

                    // Generate new buttons for empty squares
                    for (int a = 5 - length; a >= 0 + adjust; a--) {
                        actorSet[4 - adjust][a].swapPriority(0);
                    }
                }               
            }            
            
            touchList.stream().forEach((e) -> {
                e.inDeletionProcess = false;    
            });        
            
            quadZero = false;
            quadOne = false;
            quadTwo = false;
            quadThree = false;
            
            touchList.clear();            
        }
        
        // How does the player VOID an action?  Currently they cannot.
        // If they could backtrack their selections and do less than three...?
        private class myListener extends ClickListener {
            ButtonActor actor;            
            
            public myListener(ButtonActor actor) {
                this.actor = actor;
            }     
            
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touched = true;
                touchnum = actor.priority;
                actor.current = actor.down;
                
                touchList.add(actor);                            
                                
                return true;
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touched = false; 
                touchnum = 0;

                if (touchList.size() > 2) {
                   postDeletionMovement();                    
                } else {
                    touchList.stream().forEach((e) -> {
                        e.current = e.up;
                    });
                    touchList.clear();
                }                
            }          
            
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (touched && actor.matches(touchnum) && actor.adjacent(touchList.getLast().index)) {
                    boolean inList = false;
                    
                    for (ButtonActor e : touchList) {
                        if (e.index == actor.index) {
                            inList = true;
                        }
                    }
                    
                    if (!inList) {
                        touchList.add(actor);
                        actor.current = actor.down;                        
                    }
                }                
                // If I'm going to run deletion logic based on quadrants I really don't need a lot of the above.
            }              
        }

        private class KeyProcessor extends InputListener {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.SPACE:
                        System.out.println("ROTATE");
                        rotate();
                }
                return true;
            }
        }

        private void rotate() {
            int zero = 1;
            int first = 4;
            int second = 8;
            int third = 12;
            int fourth = 16;
            int fifth = 20;
            int sixth = 20;
            
            int sentinel = 1;
            int row = 4;
            int col = 5;

            // Costlier to swap textures from the file, or from object references?
            while (sentinel < first) {  
                int xShift = actorSet[row][col].xShift;
                int yShift = actorSet[row][col].yShift;                
                
                int priorityA = actorSet[row][col].priority;
                int priorityB = actorSet[row + xShift][col + yShift].priority;
                
                actorSet[row][col].swapPriority(priorityB);
                actorSet[row + xShift][col + yShift].swapPriority(priorityA);
                
                row = row + xShift;
                col = col + yShift;
                sentinel++;                
            }
            
            sentinel = 1;
            row = 5;
            col = 7;
            while (sentinel < second) {      
                int xShift = actorSet[row][col].xShift;
                int yShift = actorSet[row][col].yShift;                
                
                int priorityA = actorSet[row][col].priority;
                int priorityB = actorSet[row + xShift][col + yShift].priority;
                
                actorSet[row][col].swapPriority(priorityB);
                actorSet[row + xShift][col + yShift].swapPriority(priorityA);
                
                row = row + xShift;
                col = col + yShift;
                sentinel++;                
            }        
            
            sentinel = 1;
            row = 6;
            col = 7;
            while (sentinel < third) {      
                int xShift = actorSet[row][col].xShift;
                int yShift = actorSet[row][col].yShift;                
                
                int priorityA = actorSet[row][col].priority;
                int priorityB = actorSet[row + xShift][col + yShift].priority;
                
                actorSet[row][col].swapPriority(priorityB);
                actorSet[row + xShift][col + yShift].swapPriority(priorityA);
                
                row = row + xShift;
                col = col + yShift;
                sentinel++;                
            }  
            
            sentinel = 1;
            row = 5;
            col = 8;
            while (sentinel < fourth) {      
                int xShift = actorSet[row][col].xShift;
                int yShift = actorSet[row][col].yShift;                
                
                int priorityA = actorSet[row][col].priority;
                int priorityB = actorSet[row + xShift][col + yShift].priority;
                
                actorSet[row][col].swapPriority(priorityB);
                actorSet[row + xShift][col + yShift].swapPriority(priorityA);
                
                row = row + xShift;
                col = col + yShift;
                sentinel++;                
            }  
            
            sentinel = 1;
            row = 5;
            col = 9;
            while (sentinel < fifth) {      
                int xShift = actorSet[row][col].xShift;
                int yShift = actorSet[row][col].yShift;                
                
                int priorityA = actorSet[row][col].priority;
                int priorityB = actorSet[row + xShift][col + yShift].priority;
                
                actorSet[row][col].swapPriority(priorityB);
                actorSet[row + xShift][col + yShift].swapPriority(priorityA);
                
                row = row + xShift;
                col = col + yShift;
                sentinel++;                
            }  
            
            sentinel = 1;
            row = 5;
            col = 10;
            while (sentinel < sixth) {      
                int xShift = actorSet[row][col].xShift;
                int yShift = actorSet[row][col].yShift;                
                
                int priorityA = actorSet[row][col].priority;
                int priorityB = actorSet[row + xShift][col + yShift].priority;
                
                actorSet[row][col].swapPriority(priorityB);
                actorSet[row + xShift][col + yShift].swapPriority(priorityA);
                
                row = row + xShift;
                col = col + yShift;
                sentinel++;                
            }              
        }
        
	@Override
	public void render () {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                        
            stage.act();
            stage.draw();
	}
}