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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyGdxGame extends ApplicationAdapter {
Stage stage;
ButtonActor[][] actorSet;
Actor one;
int prefW;
int prefH;

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
        
        private class myListener extends ClickListener {
            ButtonActor actor;            
            
            public myListener(ButtonActor actor) {
                this.actor = actor;
            }             
            
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.current = actor.down;
                System.out.println(actor.index);
                return true;
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                actor.current = actor.up;
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
