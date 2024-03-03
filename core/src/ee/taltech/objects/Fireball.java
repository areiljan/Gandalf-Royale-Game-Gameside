package ee.taltech.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;


public class Fireball {

    private final Integer senderId;
    private double xPosition;
    private double yPosition;
    private final Integer id;
    private final Body body;

    /**
     * Construct fireball.
     *
     * @param senderId fireball sender
     * @param xPosition x position
     * @param yPosition y position
     * @param id fireball ID
     * @param world world where fireball is
     */
    public Fireball(Integer senderId, double xPosition, double yPosition, Integer id, World world) {
        this.senderId = senderId;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.id = id;

        // Create fireball body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((float) xPosition, (float) yPosition); // Initial position
//        bodyDef.bullet = true; // Set as bullet body
        body = world.createBody(bodyDef);

        // Create fixture for fireball hit box
        CircleShape shape = new CircleShape();
        shape.setRadius(20.0f); // Example hit box size
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        // Dispose shape
        shape.dispose();

        // Set user data to identify fireball
        body.setUserData("fireball");
    }

    /**
     * Get sender ID.
     *
     * @return senderId
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * Get x position.
     *
     * @return x position
     */
    public double getXPosition() {
        return xPosition;
    }

    /**
     * Get y position.
     *
     * @return y position
     */
    public double getYPosition() {
        return yPosition;
    }

    /**
     * Get ID.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get fireball body.
     *
     * @return body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Set x position.
     *
     * @param xPosition new x position
     */
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Set y position.
     *
     * @param yPosition new y position
     */
    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Set hit box position.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setHitBoxPosition(double x, double y) {
        body.setTransform((float) x, (float) y, body.getAngle());
    }
}
