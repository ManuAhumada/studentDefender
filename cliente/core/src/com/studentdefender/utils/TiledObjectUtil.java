package com.studentdefender.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class TiledObjectUtil {
    public static void parseTiledObjectLayer(World world, MapObjects objects, boolean enemyCanPass) {
        for (MapObject object : objects) {
            Shape shape;

            if (object instanceof PolylineMapObject) {
                shape = createPolyline((PolylineMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = createPolygon((PolygonMapObject) object);
            } else {
                continue;
            }

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.filter.categoryBits = Global.BIT_PARED;
            fd.filter.maskBits = Global.BIT_LUZ;
            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            body.createFixture(fd);

            shape.dispose();
        }
    }

    private static Shape createPolygon(PolygonMapObject polygon) {
        float[] vertices = polygon.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / 32f, vertices[i * 2 + 1] / 32f);
        }
        ChainShape cs = new ChainShape();
        cs.createLoop(worldVertices);
        return cs;
    }

    private static ChainShape createPolyline(PolylineMapObject polyline) {
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / 32f, vertices[i * 2 + 1] / 32f);
        }

        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }
}
