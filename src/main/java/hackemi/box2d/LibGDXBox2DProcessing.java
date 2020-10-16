package hackemi.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import processing.core.PApplet;
import processing.core.PVector;

public class LibGDXBox2DProcessing {
    PApplet parent;
    public World world;
    public float transX;
    public float transY;
    public float scaleFactor;
    public float yFlip;
    Body groundBody;
    LibGDXBox2DContactListener contactlistener;

    public LibGDXBox2DProcessing(PApplet p) {
        this(p, 10.0F);
    }

    public LibGDXBox2DProcessing(PApplet p, float sf) {
        this.parent = p;
        this.transX = (float)(this.parent.width / 2);
        this.transY = (float)(this.parent.height / 2);
        this.scaleFactor = sf;
        this.yFlip = -1.0F;
    }

    public void listenForCollisions() {
        this.contactlistener = new LibGDXBox2DContactListener(this.parent);
        this.world.setContactListener(this.contactlistener);
    }

    public void setScaleFactor(float scale) {
        this.scaleFactor = scale;
    }

    public void step() {
        float timeStep = 0.016666668F;
        this.step(timeStep, 10, 8);
        this.world.clearForces();
    }

    public void step(float timeStep, int velocityIterations, int positionIterations) {
        this.world.step(timeStep, velocityIterations, positionIterations);
    }

    public void setWarmStarting(boolean b) {
        this.world.setWarmStarting(b);
    }

    public void setContinuousPhysics(boolean b) {
        this.world.setContinuousPhysics(b);
    }

    public void createWorld() {
        Vector2 gravity = new Vector2(0.0F, -10.0F);
        this.createWorld(gravity);
        this.setWarmStarting(true);
        this.setContinuousPhysics(true);
    }

    public void createWorld(Vector2 gravity) {
        this.createWorld(gravity, true, true);
    }

    public void createWorld(Vector2 gravity, boolean warmStarting, boolean continous) {
        this.world = new World(gravity, continous);
        this.setWarmStarting(warmStarting);
        this.setContinuousPhysics(continous);
        BodyDef bodyDef = new BodyDef();
        this.groundBody = this.world.createBody(bodyDef);
    }

    public Body getGroundBody() {
        return this.groundBody;
    }

    public void setGravity(float x, float y) {
        this.world.setGravity(new Vector2(x, y));
    }

    public Vector2 coordWorldToPixels(Vector2 world) {
        return this.coordWorldToPixels(world.x, world.y);
    }

    public PVector coordWorldToPixelsPVector(Vector2 world) {
        Vector2 v = this.coordWorldToPixels(world.x, world.y);
        return new PVector(v.x, v.y);
    }

    public Vector2 coordWorldToPixels(float worldX, float worldY) {
        float pixelX = PApplet.map(worldX, 0.0F, 1.0F, this.transX, this.transX + this.scaleFactor);
        float pixelY = PApplet.map(worldY, 0.0F, 1.0F, this.transY, this.transY + this.scaleFactor);
        if (this.yFlip == -1.0F) {
            pixelY = PApplet.map(pixelY, 0.0F, (float)this.parent.height, (float)this.parent.height, 0.0F);
        }

        return new Vector2(pixelX, pixelY);
    }

    public Vector2 coordPixelsToWorld(Vector2 screen) {
        return this.coordPixelsToWorld(screen.x, screen.y);
    }

    public Vector2 coordPixelsToWorld(PVector screen) {
        return this.coordPixelsToWorld(screen.x, screen.y);
    }

    public Vector2 coordPixelsToWorld(float pixelX, float pixelY) {
        float worldX = PApplet.map(pixelX, this.transX, this.transX + this.scaleFactor, 0.0F, 1.0F);
        float worldY = pixelY;
        if (this.yFlip == -1.0F) {
            worldY = PApplet.map(pixelY, (float)this.parent.height, 0.0F, 0.0F, (float)this.parent.height);
        }

        worldY = PApplet.map(worldY, this.transY, this.transY + this.scaleFactor, 0.0F, 1.0F);
        return new Vector2(worldX, worldY);
    }

    public float scalarPixelsToWorld(float val) {
        return val / this.scaleFactor;
    }

    public float scalarWorldToPixels(float val) {
        return val * this.scaleFactor;
    }

    public Vector2 vectorPixelsToWorld(Vector2 v) {
        Vector2 u = new Vector2(v.x / this.scaleFactor, v.y / this.scaleFactor);
        u.y *= this.yFlip;
        return u;
    }

    public Vector2 vectorPixelsToWorld(PVector v) {
        Vector2 u = new Vector2(v.x / this.scaleFactor, v.y / this.scaleFactor);
        u.y *= this.yFlip;
        return u;
    }

    public Vector2 vectorPixelsToWorld(float x, float y) {
        Vector2 u = new Vector2(x / this.scaleFactor, y / this.scaleFactor);
        u.y *= this.yFlip;
        return u;
    }

    public Vector2 vectorWorldToPixels(Vector2 v) {
        Vector2 u = new Vector2(v.x * this.scaleFactor, v.y * this.scaleFactor);
        u.y *= this.yFlip;
        return u;
    }

    public PVector vectorWorldToPixelsPVector(Vector2 v) {
        PVector u = new PVector(v.x * this.scaleFactor, v.y * this.scaleFactor);
        u.y *= this.yFlip;
        return u;
    }

    public Body createBody(BodyDef bd) {
        return this.world.createBody(bd);
    }

    public Joint createJoint(JointDef jd) {
        return this.world.createJoint(jd);
    }

    public Vector2 getBodyPixelCoord(Body b) {
        Transform xf = b.getTransform();
        return this.coordWorldToPixels(xf.getPosition());
    }

    public PVector getBodyPixelCoordPVector(Body b) {
        Transform xf = b.getTransform();
        return this.coordWorldToPixelsPVector(xf.getPosition());
    }

    public void destroyBody(Body b) {
        this.world.destroyBody(b);
    }
}