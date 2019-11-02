package com.studentdefender.path_finder;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.juego.GameScreen;

public class IndexedGraphImp implements IndexedGraph<Node> {

    private Array<ConnectionImp> connections;
    private Array<Node> nodes;

    public IndexedGraphImp(Array<Node> nodes) {
        this.nodes = nodes;

        // crearConexiones();
    }
    
    // Crea las conexiones entre nodos automaticamente si no hay una pared entre ellos
    private void crearConexiones() {
        connections = new Array<>();
        for (int i = 0; i < nodes.size; i++) {
            Node nodeA = nodes.get(i);
            for (int j = 0; j < nodes.size; j++) {
                if (i != j) {
                    Node nodeB = nodes.get(j);
                    GameScreen.world.rayCast(GameScreen.rayCastCallback, nodeA.getPosition(), nodeB.getPosition());
                    if (GameScreen.rayCastCallback.isHit()) {
                        connections.add(new ConnectionImp(nodeA, nodeB));
                        connections.add(new ConnectionImp(nodeB, nodeA));
                    }
                }
            }
        }
    }

    public Node getCloserNode(Vector2 position) {
        Node closerNode = null;
        float minDistance = 0;
        for (Node node : nodes) {
            if (!node.getPosition().epsilonEquals(position)) {
                GameScreen.world.rayCast(GameScreen.rayCastCallback, position, node.getPosition());
                if (GameScreen.rayCastCallback.isHit()) {
                    float distance = node.getPosition().dst(position);
                    if (distance < minDistance || closerNode == null) {
                        closerNode = node;
                        minDistance = distance;
                    }
                }
            }
        }
        return closerNode;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        Array<Connection<Node>> connectionsFromNode = new Array<>();
        for (Connection<Node> connection : connections) {
            if (connection.getFromNode().getPosition().epsilonEquals(fromNode.getPosition())) {
                connectionsFromNode.add(connection);
            }
        }
        return connectionsFromNode;
    }

    public Array<Node> getNodes() {
        return nodes;
    }

    public void setConnections(Array<ConnectionImp> connections) {
        this.connections = connections;
    }

    @Override
    public int getIndex(Node node) {
        return nodes.indexOf(node, true);
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    public Node getNodeByPosition(float x, float y) {
        return getNodeByPosition(new Vector2(x, y));
    }

    public Node getNodeByPosition(Vector2 position) {
        for (Node node : nodes) {
            if (node.getPosition().epsilonEquals(position))
                return node;
        }
        return null;
    }
}
