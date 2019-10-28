package com.studentdefender.utils;

import static com.studentdefender.utils.Constants.PPM;

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
import com.badlogic.gdx.utils.Array;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.path_finder.ConnectionImp;
import com.studentdefender.path_finder.Node;

public class TiledObjectUtil {
    public static void parseTiledObjectLayer(World world, MapObjects objects) {
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
            fd.filter.categoryBits = Constants.BIT_PARED;
            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            body.createFixture(fd).setUserData("Wall");
            shape.dispose();
        }
    }

    private static Shape createPolygon(PolygonMapObject polygon) {
        float[] vertices = polygon.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
        }
        ChainShape cs = new ChainShape();
        cs.createLoop(worldVertices);
        return cs;
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

    public static Array<Node> crearNodos(World world, MapObjects objects) {
        Array<Node> nodes = new Array<>();
        for (MapObject nodo : objects) {
            nodes.add(new Node((int) (float) nodo.getProperties().get("x"), (int) (float) nodo.getProperties().get("y"),
                    false, nodes.size));
        }
        return nodes;
    }

    public static Array<ConnectionImp> crearConexiones(World world, MapObjects objects) {
        Array<ConnectionImp> connections = new Array<>();
        for (MapObject object : objects) {
            if (object instanceof PolylineMapObject) {
                float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
                Node firstNode = GameScreen.indexedGraphImp.getNodeByPosition((int) vertices[0], (int) vertices[1]);
                Node secondNode = GameScreen.indexedGraphImp.getNodeByPosition((int) vertices[2], (int) vertices[3]);
                connections.add(new ConnectionImp(firstNode, secondNode));
                connections.add(new ConnectionImp(secondNode, firstNode));
            }
        }
        return connections;
    }
}
