/*
 * Copyright 2016 Patrik Karlsson.
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

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import org.nbgames.core.api.DictNbg;
import org.nbgames.core.api.ui.GamePanel;
import org.nbgames.core.dice.DiceBoard;
import org.nbgames.hekaton.ScoreCardObservable.ScoreCardEvent;
import se.trixon.almond.nbp.dialogs.NbMessage;

/**
 *
 * @author Patrik Karlsson
 */
public class HekatonPanel extends GamePanel implements Observer {

    private DiceBoard mDiceBoard;
    private ScoreCard mScoreCard;

    /**
     * Creates new form HekatonPanel
     */
    public HekatonPanel() {
        initComponents();
    }

    @Override
    public void newGame() {
        initGame();
        mScoreCard.newGame();
        mDiceBoard.newTurn();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof DiceBoard.RollEvent) {
            switch ((DiceBoard.RollEvent) arg) {
                case PRE_ROLL:
                    if (mDiceBoard.getNumOfDice() == mDiceBoard.getNumOfSelectedDice()) {
                        mScoreCard.setEnabledLock(false);
                        mScoreCard.newRoll();
                        mDiceBoard.roll();
                    }
                    break;

                case POST_ROLL:
                    mScoreCard.parseDice(mDiceBoard.getValues());
                    break;
            }
        }

        if (arg instanceof ScoreCardEvent) {
            switch ((ScoreCardEvent) arg) {
                case GAME_OVER:
                    mDiceBoard.gameOver();
                    NbMessage.information(DictNbg.GAME_OVER.toString(), "it's over");
                    break;

                case HOLD:
                    mDiceBoard.setHandMode(mScoreCard.getHandedness());
                    mDiceBoard.newTurn();
                    break;

                case STOP:
                    mDiceBoard.setHandMode(mScoreCard.getHandedness());
                    break;
            }
        }
    }

    private void initGame() {
        removeAll();

        mScoreCard = new ScoreCard();
        mScoreCard.getObservable().addObserver(this);
        add(mScoreCard, BorderLayout.CENTER);

        mDiceBoard = new DiceBoard(2);
        mDiceBoard.addObserver(this);
        mDiceBoard.setDiceTofloor(1000);
        mDiceBoard.setMaxRollCount(999);
        add(mDiceBoard.getPanel(), BorderLayout.SOUTH);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
