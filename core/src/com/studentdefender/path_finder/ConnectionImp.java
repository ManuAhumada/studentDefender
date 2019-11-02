package com.studentdefender.path_finder;

import com.badlogic.gdx.ai.pfa.Connection;

public class ConnectionImp implements Connection<Node> {

    private Node fromNode;
    private Node toNode;
    private float cost;

    public ConnectionImp(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        cost = fromNode.getPosition().dst(toNode.getPosition());
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}