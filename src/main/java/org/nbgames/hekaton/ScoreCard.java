/*
 * Copyright 2017 Patrik Karlsson.
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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import org.nbgames.core.api.DictNbg;
import org.nbgames.core.api.NbGames;
import org.nbgames.core.api.Player.Handedness;
import org.nbgames.core.api.ui.GameOverItem;
import org.nbgames.hekaton.ScoreCardObservable.ScoreCardEvent;
import se.trixon.almond.nbp.Almond;
import se.trixon.almond.util.CircularInt;
import se.trixon.almond.util.icons.IconColor;
import se.trixon.almond.util.icons.material.MaterialIcon;

/**
 *
 * @author Patrik Karlsson
 */
public class ScoreCard extends javax.swing.JPanel {

    private final ScoreCardObservable mObservable = new ScoreCardObservable();
    private final Options mOptions = Options.getInstance();
    private PlayerPanel[] mPlayerPanels;
    private final IconColor mIconColor = NbGames.getAlmondOptions().getIconColor();
    private CircularInt mCurrentPlayer;
    private int mActivePlayer;

    /**
     * Creates new form ScoreCard
     */
    public ScoreCard() {
        initComponents();
        holdButton.setIcon(MaterialIcon._Action.LOCK.get(Almond.ICON_LARGE * 2, mIconColor));
    }

    public ScoreCardObservable getObservable() {
        return mObservable;
    }

    ArrayList<GameOverItem> getGameOverItems() {
        ArrayList<GameOverItem> gameOverItems = new ArrayList<>();

        for (PlayerPanel playerPanel : mPlayerPanels) {
            gameOverItems.add(new GameOverItem(playerPanel.getPlayer(), playerPanel.getScore()));
        }

        return gameOverItems;
    }

    Handedness getHandedness() {
        return mPlayerPanels[mActivePlayer].getPlayer().getHandedness();
    }

    void newGame() {
        holderPanel.removeAll();
        int numOfPlayers = mOptions.getNumOfPlayers();
        mPlayerPanels = new PlayerPanel[numOfPlayers];
        mCurrentPlayer = new CircularInt(0, numOfPlayers - 1);
        Dimension d = new Dimension(128, 196);

        for (int i = 0; i < numOfPlayers; i++) {
            mPlayerPanels[i] = new PlayerPanel(mOptions.getPlayers()[i]);
            mPlayerPanels[i].setPreferredSize(d);
            mPlayerPanels[i].setActive(i == 0);
            holderPanel.add(mPlayerPanels[i]);
        }

        holdButton.setEnabled(false);
    }

    void newRoll() {
        //mPlayers.get(mActivePlayer).clearPreview();
    }

    void parseDice(LinkedList<Integer> values) {
        setEnabledHold(true);
        PlayerPanel playerPanel = mPlayerPanels[mActivePlayer];
        playerPanel.incNumOfRolls();

        boolean twoDice = mOptions.isTwoDice() && !mOptions.isBigPig();
        boolean bigPig = mOptions.isBigPig() && mOptions.isTwoDice();
        boolean pair = values.getFirst().intValue() == values.getLast().intValue() && (twoDice || bigPig);
        boolean stop = false;
        int score = 0;

//        System.out.println("");
//        for (Integer value : values) {
//            System.out.println("val=" + value);
//        }
//        System.out.println("two: " + twoDice);
//        System.out.println("big: " + bigPig);
//        System.out.println("pair: " + pair);
        for (Integer value : values) {
            score += value;
            stop = stop || value == 1;
        }
//        System.out.println("stop: " + stop);
        if (stop) {
            if (pair) {
                int pairScore = twoDice ? 0 : 25;
                playerPanel.addScore(pairScore);
            }
            playerPanel.stop(!pair);
            activateNextPlayer();
            mObservable.notify(ScoreCardEvent.STOP);
        } else {
            if (pair && twoDice) {
                holdButton.setEnabled(false);
            }

            if (bigPig && pair && !stop) {
                score *= 2;
            }

            playerPanel.addScore(score);
        }
        if (playerPanel.getScore() >= mOptions.getGoal()) {
            setEnabledHold(false);
            mObservable.notify(ScoreCardEvent.GAME_OVER);
        }
    }

    void setEnabledHold(boolean b) {
        holdButton.setEnabled(b);
    }

    private void activateNextPlayer() {
        mPlayerPanels[mActivePlayer].setActive(false);
        mActivePlayer = mCurrentPlayer.inc();
        mPlayerPanels[mActivePlayer].setActive(true);
        holdButton.setEnabled(false);
        repaint();
        revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        holderPanel = new javax.swing.JPanel();
        holdButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        holderPanel.setLayout(new java.awt.GridLayout(1, 0));
        add(holderPanel, new java.awt.GridBagConstraints());

        holdButton.setToolTipText(DictNbg.HOLD.toString());
        holdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                holdButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(24, 0, 0, 0);
        add(holdButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void holdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_holdButtonActionPerformed
        mPlayerPanels[mActivePlayer].hold();
        activateNextPlayer();
        mObservable.notify(ScoreCardEvent.HOLD);
    }//GEN-LAST:event_holdButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton holdButton;
    private javax.swing.JPanel holderPanel;
    // End of variables declaration//GEN-END:variables
}
