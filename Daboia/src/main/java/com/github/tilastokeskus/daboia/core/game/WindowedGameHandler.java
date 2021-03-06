
package com.github.tilastokeskus.daboia.core.game;

import com.github.tilastokeskus.daboia.ui.GameWindow;
import javax.swing.Timer;

public abstract class WindowedGameHandler extends AbstractGameHandler<SavedStateGame> {
    
    protected GameWindow window;

    public WindowedGameHandler(SavedStateGame game) {
        super(game);
    }

    public WindowedGameHandler(SavedStateGame game, GameWindow window) {
        super(game);
        this.window = window;
        this.addObserver(this.window);
    }
    
    public void setWindow(GameWindow window) {
        this.deleteObserver(this.window);
            
        this.window = window;
        this.addObserver(this.window);
    }
    
    public GameWindow getWindow() {
        return this.window;
    }
    
    public void showWindow() {
        this.window.showWindow();
    }
    
    @Override
    public void startGame(int refreshrate) {
        super.startGame(refreshrate);
        this.showWindow();
    }
    
    @Override
    public void stopGame() {
        this.stopGame(0);
    }
    
    /**
     * Stops and shuts down the running game.
     * @param delay Time to wait, in milliseconds, until the window is closed.
     */
    public void stopGame(int delay) {
        super.stopGame();
        
        /* Set a timer to close the game window after specified delay */
        Timer timer = new Timer(delay, e -> getWindow().closeWindow());
        
        timer.setRepeats(false); /* set timer to fire only once */
        timer.start();
    }
    
}
