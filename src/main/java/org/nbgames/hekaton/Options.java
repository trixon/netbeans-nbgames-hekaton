/* 
 * Copyright 2018 Patrik Karlström.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nbgames.hekaton;

import java.awt.Color;
import java.util.prefs.Preferences;
import org.nbgames.core.api.Player;
import org.openide.util.NbPreferences;
import se.trixon.almond.util.GraphicsHelper;

/**
 *
 * @author Patrik Karlström
 */
public class Options {

    public static final String KEY_BIG_PIG = "bigPig";

    public static final String KEY_GAME_TYPE_ID = "gameType";
    public static final String KEY_GOAL = "goal";
    public static final String KEY_NUM_OF_PLAYERS = "numOfPlayers";
    public static final String KEY_TWO_DICE = "twoDice";
    private static final boolean DEFAULT_BIG_PIG = false;
    private static final String DEFAULT_COLOR_BACKGROUND = "#333333";
    private static final String DEFAULT_GAME_TYPE_ID = "default";
    private static final String DEFAULT_GAME_VARIANT = "standard";
    private static final int DEFAULT_GOAL = 99;
    private static final int DEFAULT_NUM_OF_PLAYERS = 2;
    private static final boolean DEFAULT_TWO_DICE = false;

    private static final Preferences mPreferences = NbPreferences.forModule(Options.class);
    private Player[] mPlayers;
    private final Preferences mPreferencesColors = NbPreferences.forModule(getClass()).node("colors");

    public static Options getInstance() {
        return Holder.INSTANCE;
    }

    public static Preferences getPreferences() {
        return mPreferences;
    }

    private Options() {
        init();
    }

    public Color getColor(ColorItem colorItem) {
        return Color.decode(mPreferencesColors.get(colorItem.getKey(), colorItem.getDefaultColorAsString()));
    }

    public String getGameTypeId() {
        return mPreferences.get(KEY_GAME_TYPE_ID, DEFAULT_GAME_TYPE_ID);
    }

    public int getGoal() {
        return mPreferences.getInt(KEY_GOAL, DEFAULT_GOAL);
    }

    public int getNumOfPlayers() {
        return mPreferences.getInt(KEY_NUM_OF_PLAYERS, DEFAULT_NUM_OF_PLAYERS);
    }

    public Player[] getPlayers() {
        return mPlayers;
    }

    public Preferences getPreferencesColors() {
        return mPreferencesColors;
    }

    public boolean isBigPig() {
        return mPreferences.getBoolean(KEY_BIG_PIG, DEFAULT_BIG_PIG);
    }

    public boolean isTwoDice() {
        return mPreferences.getBoolean(KEY_TWO_DICE, DEFAULT_TWO_DICE);
    }

    public void setBigPig(boolean state) {
        mPreferences.putBoolean(KEY_BIG_PIG, state);
    }

    public void setColor(ColorItem colorItem, Color color) {
        mPreferencesColors.put(colorItem.getKey(), GraphicsHelper.colorToString(color));
    }

    public void setGameTypeId(String typeId) {
        mPreferences.put(KEY_GAME_TYPE_ID, typeId);
    }

    public void setGoal(int goal) {
        mPreferences.putInt(KEY_GOAL, goal);
    }

    public void setNumOfPlayers(int players) {
        mPreferences.putInt(KEY_NUM_OF_PLAYERS, players);
    }

    public void setPlayers(Player[] players) {
        mPlayers = players;
    }

    public void setTwoDice(boolean state) {
        mPreferences.putBoolean(KEY_TWO_DICE, state);
    }

    private void init() {
    }

    public enum ColorItem {

        BACKGROUND(DEFAULT_COLOR_BACKGROUND);

        private final String mDefaultColor;

        ColorItem(String defaultColor) {
            mDefaultColor = defaultColor;
        }

        public Color getDefaultColor() {
            return Color.decode(mDefaultColor);
        }

        public String getDefaultColorAsString() {
            return mDefaultColor;
        }

        public String getKey() {
            return name().toLowerCase();
        }
    }

    private static class Holder {

        private static final Options INSTANCE = new Options();
    }
}
