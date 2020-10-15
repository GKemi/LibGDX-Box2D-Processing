package com.company.hackemi.box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import processing.core.PApplet;

import java.lang.reflect.Method;

public class LibGDXBox2DContactListener implements ContactListener {
    PApplet parent;
    Method beginMethod;
    Method endMethod;
    Method postMethod;
    Method preMethod;

    LibGDXBox2DContactListener(PApplet p) {
        this.parent = p;

        try {
            this.beginMethod = this.parent.getClass().getMethod("beginContact", Contact.class);
        } catch (Exception var6) {
            System.out.println("You are missing the beginContact() method. " + var6);
        }

        try {
            this.endMethod = this.parent.getClass().getMethod("endContact", Contact.class);
        } catch (Exception var5) {
            System.out.println("You are missing the endContact() method. " + var5);
        }

        try {
            this.postMethod = this.parent.getClass().getMethod("postSolve", Contact.class, ContactImpulse.class);
        } catch (Exception var4) {
        }

        try {
            this.preMethod = this.parent.getClass().getMethod("preSolve", Contact.class, Manifold.class);
        } catch (Exception var3) {
        }

    }

    public void beginContact(Contact c) {
        if (this.beginMethod != null) {
            try {
                this.beginMethod.invoke(this.parent, c);
            } catch (Exception var3) {
                System.out.println("Could not invoke the \"beginContact()\" method for some reason.");
                var3.printStackTrace();
                this.beginMethod = null;
            }
        }

    }

    public void endContact(Contact c) {
        if (this.endMethod != null) {
            try {
                this.endMethod.invoke(this.parent, c);
            } catch (Exception var3) {
                System.out.println("Could not invoke the \"removeContact()\" method for some reason.");
                var3.printStackTrace();
                this.endMethod = null;
            }
        }

    }

    public void postSolve(Contact c, ContactImpulse ci) {
        if (this.postMethod != null) {
            try {
                this.postMethod.invoke(this.parent, c, ci);
            } catch (Exception var4) {
                System.out.println("Could not invoke the \"postSolve()\" method for some reason.");
                var4.printStackTrace();
                this.postMethod = null;
            }
        }

    }

    public void preSolve(Contact c, Manifold m) {
        if (this.preMethod != null) {
            try {
                this.preMethod.invoke(this.parent, c, m);
            } catch (Exception var4) {
                System.out.println("Could not invoke the \"preSolve()\" method for some reason.");
                var4.printStackTrace();
                this.preMethod = null;
            }
        }

    }
}
