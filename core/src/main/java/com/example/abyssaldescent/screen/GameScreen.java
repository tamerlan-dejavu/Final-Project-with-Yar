
 package com.example.abyssaldescent.screen;
 
 import com.badlogic.gdx.Screen;
 import com.badlogic.gdx.assets.AssetManager;
 import com.example.abyssaldescent.core.ScreenManager;
 
 public class GameScreen implements Screen {
 
     private final ScreenManager screenManager;
     private final AssetManager assetManager;
 
     public GameScreen(ScreenManager screenManager, AssetManager assetManager) {
         this.screenManager = screenManager;
         this.assetManager = assetManager;
     }
 
     @Override public void show() {}
     @Override public void render(float delta) {}
     @Override public void resize(int width, int height) {}
     @Override public void pause() {}
     @Override public void resume() {}
     @Override public void hide() {}
     @Override public void dispose() {}
 }

