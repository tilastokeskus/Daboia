
package com.github.tilastokeskus.daboia.ui.button;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonListener implements MouseListener {
    
    private static Component lastEntered = null;
    
    private final Button parent;
    
    public ButtonListener(Button parent) {
        this.parent = parent;
    }
               
    @Override
    public void mousePressed(MouseEvent e) {        
        parent.setState(ButtonState.DOWN);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (lastEntered == parent) {
            parent.setState(ButtonState.HOVER);
        } else {
            parent.setState(ButtonState.DEFAULT);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (parent.isEnabled()) {
            parent.notifyListeners(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        lastEntered = e.getComponent();
        parent.setState(ButtonState.HOVER);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        lastEntered = null;
        parent.setState(ButtonState.DEFAULT);
    }

}
