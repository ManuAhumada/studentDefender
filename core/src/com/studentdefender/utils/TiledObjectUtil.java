package com.studentdefender.utils;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class TiledObjectUtil {
    public static void parseTiledObjectLayer(World world, MapObjects objects) {
        for (MapObject object : objects) {
            Shape shape;

            if (object instanceof PolylineMapObject) {
                shape = createPolyline((PolylineMapObject) object);
            } else {
                continue;
            }

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.filter.categoryBits = Constants.BIT_PARED;
            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            body.createFixture(fd).setUserData("Wall");
            shape.dispose();
        }
    }

    private static ChainShape createPolyline(PolylineMapObject polyline) {
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }

    public static Array<GridPoint2> crearNodos(World world, MapObjects objects) {
        Array<GridPoint2> nodes = new Array<>();
        for (MapObject nodo : objects) {
            nodes.add(new GridPoint2((int) ((float) nodo.getProperties().get("x")),
                    (int) ((float) nodo.getProperties().get("y"))));
        }
        return nodes;
    }
}
