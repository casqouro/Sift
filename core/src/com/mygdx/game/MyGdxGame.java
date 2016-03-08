package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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

/* FEATURE BRAIN-STORMING

   0.  Backtracking from selections is a must. (DONE)

   1.  Scoring
       A.  Designing some prototypes for scoring logic
       B.  Desgining where scores will be located relative to the game

   2.  Animation
       A.  Investigating animations for rotate/deletion.

   3.  SoundFX
       A.  Try to attach a sound to one action to get a feel for the process.  (DONE)

       - Some very basic audio is in but I don't entirely understand the manipulations.

       - A bit advanced for where I am but it might hold good insights:
       http://steigert.blogspot.ca/2012/03/8-libgdx-tutorial-sound-and-music.html

   4.  Start Screen
       A.  Add a start/splash screen before jumping into the game.

   5.  Check for possible matches.  If the game doesn't have matches for the
       player to make then something has to happen.

       - Can't think of any easy way to do this without brute-force checking.

       - Could be better solved by including the ability to pass by pressing 
         "SPACE", which is already in.
    
   ?   Is it better to swap references from file, or from a static declaration? (DONE)
        
       - There seemed to be no noticeable difference, even when running 1000's of tests at a time.

       ? There DID see to be a growing memory issue when testing at larger values.  I need to use dispose().
*/

public class MyGdxGame extends ApplicationAdapter {
Stage stage;
ButtonActor[][] actorSet;
LinkedList<ButtonActor> touchList;
int prefW;
int prefH;

Sound sound;
float numSelected;

boolean touched;
int touchnum;
int currentTile;

boolean quadZero = false;
boolean quadOne = false;
boolean quadTwo = false;
boolean quadThree = false;
    
    @Override
	public void create () {
            stage = new Stage();
            stage.addListener(new KeyProcessor());
            Gdx.input.setInputProcessor(stage);
            prefW = Gdx.graphics.getWidth() / 11;
            prefH = Gdx.graphics.getHeight() / 11;
                        
            sound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.ogg"));

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
        private boolean checkForMatches() {
            // careful with the white ones right now!  They match!
            
            // this is going to take some serious consideration
            
            return false;
        }
        
        private void postDeletionMovement() {
            LinkedList<ButtonActor> newTileList = new LinkedList<>();
            
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
            
            touchList.stream().forEach((e) -> {
                System.out.println(e.index);
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
                        newTileList.add(actorSet[a][6 + adjust]);
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
                        newTileList.add(actorSet[6 + adjust][a]);                        
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
                        newTileList.add(actorSet[a][4 - adjust]);                        
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
                        newTileList.add(actorSet[4 - adjust][a]);                        
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
            
            newTileList.stream().forEach((e) -> {
                e.newTile();    
            }); 
            
            touchList.clear();
            newTileList.clear();            
            rotate();
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
                numSelected = 1;
                long id = sound.play();
                sound.setPitch(id, 0.25f);                
                
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
                numSelected = 0;

                if (touchList.size() > 2) {
                   postDeletionMovement();                    
                } else {
                    touchList.stream().forEach((e) -> {
                        e.current = e.up;
                        System.out.println(e.index);
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
                        
                        numSelected++;
                        long id = sound.play();
                        sound.setPitch(id, 0.25f + 0.05f * numSelected);
                        System.out.println(0.25f + 0.05f * numSelected);
                    }
                    
                    if (inList) {
                        touchList.getLast().current = touchList.getLast().up;
                        touchList.removeLast();
                        
                        numSelected--;
                        long id = sound.play();
                        sound.setPitch(id, 0.25f + 0.05f * numSelected);
                        System.out.println(0.25f + 0.05f * numSelected);
                    }
                }
            }              
        }

        private class KeyProcessor extends InputListener {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.SPACE:
                        rotate();
                        break;
                }
                return true;
            }
        }

        private void rotate() {
            int first = 4;
            int second = 8;
            int third = 12;
            int fourth = 16;
            int fifth = 20;
            int sixth = 20;
            
            int sentinel = 1;
            int row = 4;
            int col = 5;

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