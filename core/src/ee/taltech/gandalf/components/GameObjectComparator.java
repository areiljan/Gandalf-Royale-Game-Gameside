package ee.taltech.gandalf.components;

import ee.taltech.gandalf.entities.Entity;
import ee.taltech.gandalf.entities.PlayerCharacter;
import ee.taltech.gandalf.world.TileData;

import java.util.Comparator;

public class GameObjectComparator implements Comparator<Entity> {
    /**
     * Entities with higher y are rendered first.
     * Player's position is modified to position the comparison cord to legs.
     *
     * @param o1 the Entity object to be compared.
     * @param o2 the Entity object to be compared.
     * @return sorting order
     */
    @Override
    public int compare(Entity o1, Entity o2) {
        if (o1 instanceof PlayerCharacter) {
            // Player's position is modified to position the comparison cord to legs.
            return Float.compare(o1.getYPosition() - 1f, o2.getYPosition());
        } else if (o2 instanceof PlayerCharacter) {
            // Player's position is modified to position the comparison cord to legs.
            return Float.compare(o1.getYPosition(), o2.getYPosition() - 1f);
        } else if (o1 instanceof TileData && o2 instanceof TileData) {
            // Props on props need to be placed on top of other props.
            if (((TileData) o1).getTileName().equals("props on props")) {
                return Float.compare(o1.getYPosition() - 3, o2.getYPosition());
            } else if (((TileData) o2).getTileName().equals("props on props")) {
                return Float.compare(o1.getYPosition(), o2.getYPosition() -3);
            }
        }
        return Float.compare(o1.getYPosition(), o2.getYPosition());
    }
}
