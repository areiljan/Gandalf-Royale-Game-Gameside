package ee.taltech.gandalf.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.components.ItemTypes;
import ee.taltech.gandalf.entities.PlayerCharacter;
import ee.taltech.gandalf.network.messages.game.KeyPress;
import ee.taltech.gandalf.network.messages.game.MouseClicks;
import ee.taltech.gandalf.scenes.SettingsWindow;
import ee.taltech.gandalf.screens.GameScreen;

import java.util.Map;
import java.util.Objects;

public class PlayerInput implements InputProcessor {
    private GandalfRoyale game;
    private PlayerCharacter playerCharacter;
    private GameScreen screen;
    private KeyPress key;
    private MouseClicks mouse;

    /**
     * Construct PlayerInput.
     *
     * @param game            GandalfRoyale game instance
     * @param playerCharacter character who's input is read
     */
    public PlayerInput(GandalfRoyale game, PlayerCharacter playerCharacter, GameScreen screen) {
        this.game = game;
        this.playerCharacter = playerCharacter;
        this.screen = screen;
    }

    /**
     * Detect what key is pressed down.
     *
     * @param keycode keycode
     * @return false
     */
    @Override
    public boolean keyDown(int keycode) {
        if (playerCharacter.getHealth() != 0) {
            Map<String, Integer> keyBindings = game.screenController.getSettingsWindow().getKeyBindings();

            if (keyBindings.get("UP") == keycode) {
                key = new KeyPress(KeyPress.Action.UP, true);
            } else if (keyBindings.get("LEFT") == keycode) {
                key = new KeyPress(KeyPress.Action.LEFT, true);
            } else if (keyBindings.get("DOWN") == keycode) {
                key = new KeyPress(KeyPress.Action.DOWN, true);
            } else if (keyBindings.get("RIGHT") == keycode) {
                key = new KeyPress(KeyPress.Action.RIGHT, true);
            } else if (keyBindings.get("PICK") == keycode) {
                // Only if empty slot is selected
                if (playerCharacter.getInventory().get(playerCharacter.getSelectedSlot()) == null) {
                    key = new KeyPress(KeyPress.Action.INTERACT, true);
                }

                // Only if empty slot in not selected
                else if (playerCharacter.getInventory().get(playerCharacter.getSelectedSlot()) != null) {
                    Integer droppedItemID;
                    droppedItemID = playerCharacter.getInventory().get(playerCharacter.getSelectedSlot()).getId();
                    key = new KeyPress(KeyPress.Action.DROP, true, droppedItemID);
                }
            } else if (Input.Keys.NUM_1 == keycode) {
                playerCharacter.setSelectedSlot(0);
            } else if (Input.Keys.NUM_2 == keycode) {
                playerCharacter.setSelectedSlot(1);
            } else if (Input.Keys.NUM_3 == keycode) {
                playerCharacter.setSelectedSlot(2);
            }
            if (!Objects.equals(key, null)) {
                // Send LEFT to server
                game.nc.sendUDP(key);
                // Send LEFT to client
                playerCharacter.setMovement(key);
                key = null;
            }
        }

        // Open Menu Window || this has to work even if dead
        if (keycode == Input.Keys.ESCAPE && !screen.isSettingsWindowShown()) {
            screen.toggleMenuWindow();
        }
        return false;
    }

    /**
     * Detect what key is realised.
     *
     * @param keycode keycode
     * @return false
     */
    @Override
    public boolean keyUp(int keycode) {
        if (playerCharacter.getHealth() != 0) {
            Map<String, Integer> keyBindings = game.screenController.getSettingsWindow().getKeyBindings();

            if (keyBindings.get("UP") == keycode) {
                key = new KeyPress(KeyPress.Action.UP, false);
            } else if (keyBindings.get("LEFT") == keycode) {
                key = new KeyPress(KeyPress.Action.LEFT, false);
            } else if (keyBindings.get("DOWN") == keycode) {
                key = new KeyPress(KeyPress.Action.DOWN, false);
            } else if (keyBindings.get("RIGHT") == keycode) {
                key = new KeyPress(KeyPress.Action.RIGHT, false);
            }

            if (!Objects.equals(key, null)) {
                // Send LEFT to server
                game.nc.sendUDP(key);
                // Send LEFT to client
                playerCharacter.setMovement(key);
                key = null;
            }
        }
        return false;
    }

    /**
     * IGNORED.
     *
     * @param keyTyped ignored
     * @return false
     */
    @Override
    public boolean keyTyped(char keyTyped) {
        return false;
    }

    /**
     * Mouse pressed.
     *
     * @param screenX x coordinate
     * @param screenY y coordinate
     * @param pointer ignored
     * @param button  what button is pressed
     * @return false
     */
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int windowHeight = Gdx.graphics.getHeight();
        int windowWidth = Gdx.graphics.getWidth();
        if (playerCharacter.getHealth() != 0) {
            if (button == Buttons.LEFT) {
                // Get the character's position
                Vector2 characterPositionOnScreen = new Vector2((float) windowWidth / 2, (float) windowHeight / 2);

                // Create a vector representing the mouse position.
                // The y coordinate is inverted now to more logical coords.
                Vector2 mousePosition = new Vector2(screenX, windowHeight - screenY);
                // Subtract the character's position from the mouse position to get the relative position
                Vector2 relativeMousePosition = new Vector2(mousePosition).sub(characterPositionOnScreen);
                // Make the vector smaller.

                ItemTypes type;
                if (playerCharacter.getInventory().get(playerCharacter.getSelectedSlot()) != null) {
                    type = playerCharacter.getInventory().get(playerCharacter.getSelectedSlot()).getType();
                } else {
                    type = ItemTypes.NOTHING;
                }
                if (type == ItemTypes.HEALING_POTION) {
                    mouse = new MouseClicks(type, true,
                            relativeMousePosition.x,
                            relativeMousePosition.y,
                            playerCharacter.getInventory().get(playerCharacter.getSelectedSlot()).getId());
                } else {
                    mouse = new MouseClicks(type, true,
                            relativeMousePosition.x,
                            relativeMousePosition.y);
                }
                game.nc.sendUDP(mouse);
            }
        }
        return false;
    }

    /**
     * IGNORED.
     *
     * @param screenX ignored
     * @param screenY ignored
     * @param pointer ignored
     * @param button  ignored
     * @return false
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Update mouse coordinates.
     *
     * @param xMousePosition x coordinate
     * @param yMousePosition y coordinate
     * @return false
     */
    @Override
    public boolean mouseMoved(int xMousePosition, int yMousePosition) {
        int windowHeight = Gdx.graphics.getHeight();
        int windowWidth = Gdx.graphics.getWidth();
        if (playerCharacter.getHealth() != 0) {
            // Get the character's position
            Vector2 characterPositionOnScreen = new Vector2((float) windowWidth / 2, (float) windowHeight / 2);

            // Create a vector representing the mouse position
            Vector2 mousePosition = new Vector2(xMousePosition, windowHeight - yMousePosition);

            // Subtract the character's position from the mouse position to get the relative position
            Vector2 relativeMousePosition = new Vector2(mousePosition).sub(characterPositionOnScreen);

            // Check if the left mouse button is not pressed
            if (!Gdx.input.isButtonPressed(Buttons.LEFT)) {
                mouse = new MouseClicks(ItemTypes.NOTHING, false,
                        relativeMousePosition.x, relativeMousePosition.y);
                game.nc.sendUDP(mouse);
            }
        }
        return false;
    }

    /**
     * IGNORED.
     *
     * @param i  ignored
     * @param i1 ignored
     * @param i2 ignored
     * @param i3 ignored
     * @return false
     */
    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    /**
     * IGNORED.
     *
     * @param i  ignored
     * @param i1 ignored
     * @param i2 ignored
     * @return false
     */
    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    /**
     * Change selected inventory slot with scrolling.
     *
     * @param amountX ignored
     * @param amountY positive if scrolled up, negative if scrolled down
     * @return true
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (playerCharacter.getHealth() != 0) {
            if (amountY > 0 && playerCharacter.getSelectedSlot() != 2) { // Scrolling down
                playerCharacter.setSelectedSlot(playerCharacter.getSelectedSlot() + 1);

            } else if (amountY < 0 && playerCharacter.getSelectedSlot() != 0) { // Scrolling up
                playerCharacter.setSelectedSlot(playerCharacter.getSelectedSlot() - 1);

            }
        }
        return true;
    }
}
