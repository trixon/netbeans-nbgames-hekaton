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

import java.awt.image.BufferedImage;
import org.nbgames.core.api.GameCategory;
import org.nbgames.core.api.GameController;
import org.nbgames.core.api.options.NbgOptionsPanel;
import org.nbgames.core.api.ui.GamePanel;
import org.nbgames.core.api.ui.NewGamePanel;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProviders(value = {
    @ServiceProvider(service = GameController.class)
})
public class Hekaton extends GameController {

    public static final String LOG_TITLE = "Hekaton";

    private HekatonPanel mGamePanel;
    private HekatonNewGamePanel mNewGamePanel;
    private OptionsPanel mOptionPanel;

    public Hekaton() {
    }

    @Override
    public GameCategory getCategory() {
        return GameCategory.DICE;
    }

    @Override
    public String getHelp() {
        return getHelp(Hekaton.class);
    }

    @Override
    public BufferedImage getImage() {
        return super.getImage();
    }

    @Override
    public NewGamePanel getNewGamePanel() {
        if (mNewGamePanel == null) {
            mNewGamePanel = new HekatonNewGamePanel();
        }

        return mNewGamePanel;
    }

    @Override
    public NbgOptionsPanel getOptionsPanel() {
        if (mOptionPanel == null) {
            mOptionPanel = new OptionsPanel();
        }

        return null;//mOptionPanel;
    }

    @Override
    public GamePanel getPanel() {
        if (mGamePanel == null) {
            mGamePanel = new HekatonPanel(this);
        }

        return mGamePanel;
    }

    @Override
    public void onRequestNewGameStart() {
        getPanel().newGame();
    }
}
