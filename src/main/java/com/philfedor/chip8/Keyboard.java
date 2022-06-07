package com.philfedor.chip8;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Keyboard {
    private HashMap<Integer, Integer> keyMap;
    private HashMap<Integer, Boolean> keysPressed;
    private KeyWaiter keyWaiter;

    //key map from 0-15 to keyboard keyCodes
    public Keyboard(HashMap<Integer, Integer> keyMap) {
        this.keyMap = keyMap;
        this.keysPressed = new HashMap<>();
        this.keyWaiter = null;

        for (int i = 0; i < 16; i++) {
            this.keysPressed.put(i, false);
        }

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
                switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED: {
                        if (this.keyMap.containsKey(ke.getKeyCode())) {
                            int key = this.keyMap.get(ke.getKeyCode());

                            this.keyPressed(key);
                        }

                        break;
                    }

                    case KeyEvent.KEY_RELEASED: {
                        if (this.keyMap.containsKey(ke.getKeyCode())) {
                            int key = this.keyMap.get(ke.getKeyCode());

                            this.keyReleased(key);
                        }
                        break;
                    }
                }

                return false;
        });
    }

    synchronized public void keyPressed(int key) {
        this.keysPressed.put(key, true);

        if (this.keyWaiter != null) {
            this.keyWaiter.keyPressed(key);
            this.keyWaiter = null;
        }
    }

    synchronized public void keyReleased(int key) {
        this.keysPressed.put(key, false);
    }

    synchronized public boolean isKeyPressed(int keyNum) {
        return this.keysPressed.get(keyNum);
    }

    synchronized public void waitForKey(KeyWaiter keyWaiter) {
        this.keyWaiter = keyWaiter;
    }
}
