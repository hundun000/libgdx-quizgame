package com.mygdx.game.html.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.game.html.GwtPreferencesSaveTool;

import hundun.gdxgame.quigame.MyGdxGame;



public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
            return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener() {
            return new MyGdxGame(new GwtPreferencesSaveTool("demo-html-save"));
        }
}