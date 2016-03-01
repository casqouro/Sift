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
            
            touchList = new LinkedList<ButtonActor>();
            
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

                actor.setBounds(row * prefW, col * prefH, prefW, prefH);                
                actor.addListener(new myListener(actor));

                stage.addActor(actor);
                
                line = br.readLine();            
            }    
        }
        
        private void postDeletionMovement() {
            // I could hard code this for all four quadrants and then revisit it later?
            
            /*
            for (ButtonActor e : touchList) {
                e.inDeletionProcess = true;
                e.current = e.transparent;
            }
            
            for (ButtonActor e : touchList) {
                int x = e.index / 11;
                int y = e.index % 11;
                
                    // find the next NON-NULL neighbor and move actor/texture
                    // move to the closest neighbor and repeat                 
                                                
                // MUST SORT THE INPUT BEFORE EVERY "IF" STATEMENT
                // otherwise in |1|ea|eb| you might handle ea before eb
                // if you handle |eb| first you can eliminate |ea|
  
                if (x < 6 && y > 5) { // Upper Left Quadrant
                    int sentinel = 0;
                    int adjust = 1;// could use adjust to make this confirm with distance between starting "X" and "0"
                    int edge = y - 6;
              
                    while ((sentinel == 0) && (x - adjust >= 0 + edge)) {
                        if (!actorSet[x - adjust][y].inDeletionProcess) {
                            sentinel = 1;
                            
                            e.current = actorSet[x - adjust][y].current;
                            actorSet[x - adjust][y].current = actorSet[x - adjust][y].transparent;
                            
                            //System.out.println("X/Y: (" + x + "," + y + ") Adjust: " + adjust);  
                        } else {
                            adjust++;
                            
                            if (x - adjust < 0 + edge) {
                                sentinel = 1;
                            }
                        }
                    }
                }
                
                if (x > 5 && y > 4) { // Upper Right Quadrant
                    int sentinel = 0;
                    int adjust = 1;
                    int edge = x - 6;
                                  
                    while ((sentinel == 0) && (y + adjust <= 10 - edge)) { // 10 - edge?
                        if (!actorSet[x][y + adjust].inDeletionProcess) {
                            sentinel = 1;
                            //System.out.println("X/Y: (" + x + "," + y + ") Adjust: " + adjust);  
                        } else {
                            adjust++;
                            
                            if (y + adjust > 10 - edge) {
                                sentinel = 1;
                            }
                        }
                    }                    
                }
                
                if (x > 4 && y < 5) { // Lower Right Quadrant
                    int sentinel = 0;
                    int adjust = 1;
                    int edge = y - 6;
              
                    while ((sentinel == 0) && (x + adjust <= 0 + edge)) {
                        if (!actorSet[x + adjust][y].inDeletionProcess) {
                            sentinel = 1;
                            //System.out.println("X/Y: (" + x + "," + y + ") Adjust: " + adjust);  
                        } else {
                            adjust++;
                            
                            if (x + adjust > 10 - edge) {
                                sentinel = 1;
                            }
                        }
                    } 
                }
                
                if (x < 5 && y < 6) { // Lower Left Quadrant
                    int sentinel = 0;
                    int adjust = 1;
                    int edge = x - 6;
                                  
                    while ((sentinel == 0) && (y - adjust <= 0 + edge)) {
                        System.out.println(x + " " + (y - adjust));
                        if (!actorSet[x][y - adjust].inDeletionProcess) {
                            sentinel = 1;
                            //System.out.println("X/Y: (" + x + "," + y + ") Adjust: " + adjust);  
                        } else {
                            adjust++;
                            
                            if (y - adjust < 0 + edge) {
                                sentinel = 1;
                            }
                        }
                    } 
                }
            }
            
            for (ButtonActor e : touchList) {
                e.inDeletionProcess = false;
            }            
            touchList.clear();
            */
        }
        
        // How does the player VOID an action?  Currently they cannot.
        // If they could backtrack their selections and do less than three...?
        private class myListener extends ClickListener {
            ButtonActor actor;            
            
            public myListener(ButtonActor actor) {
                this.actor = actor;
            }     
            
            // Does not work once touchDown/touchUp are implemented.
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");                               
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
                    for (ButtonActor e : touchList) {
                        e.current = e.up;
                    }
                    touchList.clear();
                }                
            }          
            
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (touched && actor.matches(touchnum) && actor.adjacent(touchList.getLast().index / 11, touchList.getLast().index % 11)) {
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
                // this code is important because I can call the listener methods from inside
                //myListener test = (myListener) actor.getListeners().get(1);
                //test.enter(event, x, y, pointer, fromActor);
            }
            
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Commenting this out eliminated a minor bug, but unsure if it'll introduce others
                
                //if(!touched) {
                //  actor.current = actor.up;
                //}
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

            // Why not just swap textures?  All I'm doing is mailing textures with a lot more work.
            while (sentinel < first) {      
                int saveIndex = actorSet[row][col].index;
                int saveX = actorSet[row][col].xShift;
                int saveY = actorSet[row][col].yShift;

                actorSet[row][col].index = actorSet[row + saveX][col + saveY].index;
                actorSet[row][col].setup();
                
                actorSet[row + saveX][col + saveY].index = saveIndex;
                actorSet[row + saveX][col + saveY].setup();

                row = row + saveX;
                col = col + saveY;
                sentinel++;
            }
            
            sentinel = 1;
            row = 5;
            col = 7;
            while (sentinel < second) {      
                int saveIndex = actorSet[row][col].index;
                int saveX = actorSet[row][col].xShift;
                int saveY = actorSet[row][col].yShift;

                actorSet[row][col].index = actorSet[row + saveX][col + saveY].index;
                actorSet[row][col].setup();

                actorSet[row + saveX][col + saveY].index = saveIndex;
                actorSet[row + saveX][col + saveY].setup();
                
                row = row + saveX;
                col = col + saveY;
                sentinel++;
            }        
            
            sentinel = 1;
            row = 6;
            col = 7;
            while (sentinel < third) {      
                int saveIndex = actorSet[row][col].index;
                int saveX = actorSet[row][col].xShift;
                int saveY = actorSet[row][col].yShift;

                actorSet[row][col].index = actorSet[row + saveX][col + saveY].index;
                actorSet[row][col].setup();

                actorSet[row + saveX][col + saveY].index = saveIndex;
                actorSet[row + saveX][col + saveY].setup();
                
                row = row + saveX;
                col = col + saveY;
                sentinel++;
            }  
            
            sentinel = 1;
            row = 5;
            col = 8;
            while (sentinel < fourth) {      
                int saveIndex = actorSet[row][col].index;
                int saveX = actorSet[row][col].xShift;
                int saveY = actorSet[row][col].yShift;

                actorSet[row][col].index = actorSet[row + saveX][col + saveY].index;
                actorSet[row][col].setup();

                actorSet[row + saveX][col + saveY].index = saveIndex;
                actorSet[row + saveX][col + saveY].setup();
                
                row = row + saveX;
                col = col + saveY;
                sentinel++;
            }  
            
            sentinel = 1;
            row = 5;
            col = 9;
            while (sentinel < fifth) {      
                int saveIndex = actorSet[row][col].index;
                int saveX = actorSet[row][col].xShift;
                int saveY = actorSet[row][col].yShift;

                actorSet[row][col].index = actorSet[row + saveX][col + saveY].index;
                actorSet[row][col].setup();

                actorSet[row + saveX][col + saveY].index = saveIndex;
                actorSet[row + saveX][col + saveY].setup();
                
                row = row + saveX;
                col = col + saveY;
                sentinel++;
            }  
            
            sentinel = 1;
            row = 5;
            col = 10;
            while (sentinel < sixth) {      
                int saveIndex = actorSet[row][col].index;
                int saveX = actorSet[row][col].xShift;
                int saveY = actorSet[row][col].yShift;

                actorSet[row][col].index = actorSet[row + saveX][col + saveY].index;
                actorSet[row][col].setup();

                actorSet[row + saveX][col + saveY].index = saveIndex;
                actorSet[row + saveX][col + saveY].setup();
                
                row = row + saveX;
                col = col + saveY;
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