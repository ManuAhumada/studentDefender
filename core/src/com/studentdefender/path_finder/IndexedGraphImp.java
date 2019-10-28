package com.studentdefender.path_finder;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class IndexedGraphImp implements IndexedGraph<Node> {

    public Array<ConnectionImp> connections;
    public Array<Node> nodes;

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        Array<Connection<Node>> connectionsFromNode = new Array<>();
        for (Connection<Node> connection : connections) {
            if (connection.getFromNode().getPosicion().epsilonEquals(fromNode.getPosicion())) {
                connectionsFromNode.add(connection);
            }
        }
        return connectionsFromNode;
    }

    @Override
    public int getIndex(Node node) {
        return nodes.indexOf(node, true);
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    public Node getCloserNode(Vector2 position) {
        Node closerNode = null;
        float minDistance = 0;
        for (Node node : nodes) {
            float distance = node.getPosition().dst(position);
            if (distance < minDistance || closerNode == null) {
                closerNode = node;
                minDistance = distance;
            }
        }
        return closerNode;
    }

    public Node getNodeByPosition(int x, int y) {
        return getNodeByPosition(new Vector2(x, y));
    }

    public Node getNodeByPosition(Vector2 position) {
        for (Node node : nodes) {
            if (node.getPosicion().epsilonEquals(position))
                return node;
        }
        return null;
    }
}
