package test.sketch

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef
import hackemi.box2d.*
import processing.core.PApplet
import processing.core.PConstants
import java.util.ArrayList

object Sketch : PApplet() {
    init {
        this.runSketch()
    }

    override fun settings() {
        super.settings()
        size(1000, 1000, PConstants.P2D)
        pixelDensity(2)
    }

    // The Nature of Code
    // <http://www.shiffman.net/teaching/nature>
    // Spring 2011
    // PBox2D example

    // Basic example of falling rectangles
    // A reference to our box2d world
    lateinit var box2d: LibGDXBox2DProcessing

    // A list we'll use to track fixed objects
    var boundaries = mutableListOf<Boundary>()
    // A list for all of our rectangles
    var boxes = mutableListOf<Box>()

    override fun setup() {
        randomSeed(5)

        // Initialize box2d physics and create the world
        box2d = LibGDXBox2DProcessing(this, 10f)
        box2d.createWorld()
        // We are setting a custom gravity
        box2d.setGravity(0f, -20f)

        // Create ArrayLists
        boxes = ArrayList()
        boundaries = ArrayList()

        // Add a bunch of fixed boundaries
        boundaries.add(Boundary(width / 4f, height - 5f, width / 2 - 50f, 10f))
        boundaries.add(Boundary(3 * width / 4f, height - 50f, width / 2 - 50f, 10f))
    }

    override fun draw() {
        background(255)

        // We must always step through time!
        box2d.step(1.0f / 60, 10, 10)

        // Boxes fall from the top every so often
        if (random(1f) < 1) {
            val p = Box(width / 2f, 30f)
            boxes.add(p)
        }

        // Display all the boundaries
        for (wall in boundaries) {
            wall.display()
        }

        // Display all the boxes
        for (b in boxes) {
            b.display()
        }

        // Boxes that leave the screen, we delete them
        // (note they have to be deleted from both the box2d world and our list
        for (i in boxes.indices.reversed()) {
            val b = boxes[i]
            if (b.done()) {
                boxes.removeAt(i)
            }
        }

//        if (frameCount >= 908) {
//            System.exit(0)
//        }


    }

}

// The Nature of Code
// <http://www.shiffman.net/teaching/nature>
// Spring 2010
// PBox2D example

// A fixed boundary class
class Boundary(// A boundary is a simple rectangle with x,y,width,and height
        var x: Float, var y: Float, var w: Float, var h: Float) {

    // But we also have to make a body for box2d to know about it
    var b: Body

    init {

        // Create the body
        val bd = BodyDef()
        bd.position.set(Sketch.box2d.coordPixelsToWorld(x, y))
        b = Sketch.box2d.createBody(bd)

        // Figure out the box2d coordinates
        val box2dW = Sketch.box2d.scalarPixelsToWorld(w / 2)
        val box2dH = Sketch.box2d.scalarPixelsToWorld(h / 2)

        // Define the polygon
        val sd = PolygonShape()
        sd.setAsBox(box2dW, box2dH)

        val fd = FixtureDef()
        fd.shape = sd
        fd.density = 0f
        fd.friction = 0.3f
        fd.restitution = 0.5f


        b.createFixture(fd)

        val cs: ChainShape
        //cs.create


    }

    // Draw the boundary, if it were at an angle we'd have to do something fancier
    fun display() = Sketch.run {
        fill(0)
        stroke(0)
        rectMode(PApplet.CENTER)
        rect(x, y, w, h)
    }

}

// The Nature of Code
// <http://www.shiffman.net/teaching/nature>
// Spring 2010
// PBox2D example

// A rectangular box
class Box(x: Float, y: Float) {

    // We need to keep track of a Body and a width and height
    lateinit var body: Body
    var w: Float = 0.toFloat()
    var h: Float = 0.toFloat()

    init {
        w = Sketch.random(4f, 16f)
        h = Sketch.random(4f, 16f)
        // Add the box to the box2d world
        makeBody(Vector2(x, y), w, h)
    }

    // This function removes the particle from the box2d world
    fun killBody() {
        val f = body.getFixtureList()
        //println(f);

        Sketch.box2d.destroyBody(body)
    }

    // Is the particle ready for deletion?
    fun done(): Boolean {
        // Let's find the screen position of the particle
        val pos = Sketch.box2d.getBodyPixelCoord(body)
        // Is it off the bottom of the screen?
        if (pos.y > Sketch.height + w * h) {
            killBody()
            return true
        }
        return false
    }

    // Drawing the box
    fun display() = Sketch.run {
        // We look at each body and get its screen position
        val pos = box2d.getBodyPixelCoord(body)
        // Get its angle of rotation
        val a = body.getAngle()

        rectMode(PApplet.CENTER)
        pushMatrix()
        translate(pos.x, pos.y)
        rotate(-a)
        fill(175)
        stroke(0)
        rect(0f, 0f, w, h)
        popMatrix()
    }

    // This function adds the rectangle to the box2d world
    fun makeBody(center: Vector2, w_: Float, h_: Float) {

        // Define a polygon (this is what we use for a rectangle)
        val sd = PolygonShape()
        val box2dW = Sketch.box2d.scalarPixelsToWorld(w_ / 2)
        val box2dH = Sketch.box2d.scalarPixelsToWorld(h_ / 2)
        sd.setAsBox(box2dW, box2dH)

        // Define a fixture
        val fd = FixtureDef()
        fd.shape = sd
        // Parameters that affect physics
        fd.density = 1f
        fd.friction = 0.3f
        fd.restitution = 0.5f

        // Define the body and make it from the shape
        val bd = BodyDef()
        bd.type = BodyDef.BodyType.DynamicBody
        bd.position.set(Sketch.box2d.coordPixelsToWorld(center))

        val cs = CircleShape()

        val djd: DistanceJointDef

        body = Sketch.box2d.createBody(bd)
        body.createFixture(fd)
        //body.setMassFromShapes();

        // Give it some initial random velocity
        body.setLinearVelocity(Vector2(Sketch.random(-5f, 5f), Sketch.random(2f, 5f)))
        body.setAngularVelocity(Sketch.random(-5f, 5f))
    }
}

fun main() {
    Sketch
}