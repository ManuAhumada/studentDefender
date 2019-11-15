package com.studentdefender.path_finder;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class RayCastCallbackImp implements RayCastCallback {

    private boolean hit;
    private Vector2 impactPoint;

    public RayCastCallbackImp() {
        hit = false;
        impactPoint = new Vector2();
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        if (fixture.getUserData() == "Wall") {
            hit = false;
            impactPoint = point;
        } else {
            hit = true;
        }
        return fraction;
    }

    public boolean isHit() {
        return hit;
    }

    public Vector2 getImpactPoint() {
        return impactPoint;
    }
}