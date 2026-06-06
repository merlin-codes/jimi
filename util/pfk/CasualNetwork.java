package dev.levia.jimi.util.pfk;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class CasualNetwork {
    public interface Node {
        Node connect(Node node);
    }
    public record SimpleNode(Node node) implements Node {
        @Override public Node connect(Node node) {
            return (this.node == null) ? new SimpleNode(node) : new MoreNode(this.node, node);

        }
    }
    public record MoreNode(Node... nodes) implements Node {
        @Override public MoreNode connect(Node node) {
            switch (nodes.length) {
                case 2 -> new MoreNode(this.nodes[0], this.nodes[1], node);
                case 3 -> new MoreNode(this.nodes[0], this.nodes[1], this.nodes[2], node);
                case 4 -> new MoreNode(this.nodes[0], this.nodes[1], this.nodes[2], this.nodes[3], node);
                case 5 -> new MoreNode(this.nodes[0], this.nodes[1], this.nodes[2], this.nodes[4], node);
            }
            throw new RuntimeException("amount of connected nodes is higher than 5 from one node");
        }
    }

    // again limit for how many nodes can exist
    private Node[] nodes = new Node[1000];
    private int total_nodes = 0;
    // for now, it's limited to 100, if count of more nodes is higher than that it means
    // that the graph is too big or too stupid
    private int[] nodesToInterate = new int[300];
    private int count = 0;

    /**
     * creates really basic structure
     * 1, 2, 3 => connected using 1->3, 1->2
     */
    public CasualNetwork() {
        nodes[1] = new SimpleNode(null);
        nodes[2] = new SimpleNode(null);
        nodes[0] = new MoreNode(nodes[1], nodes[2]);
        nodesToInterate[0] = 0;
        nodesToInterate[1] = 1;
        nodesToInterate[2] = 2;
        count = 1;
        total_nodes = 3;
    }
    public void tick() {
        for (int i = 0; i < count; i++) {
            // do evolve
            // connect to new one
            nodes[total_nodes] = new SimpleNode(null);
            // remove one of the connections to one node


            // override the point with
        }
    }
}
