package dev.levia.jimi.util.pfk;

import java.util.ArrayList;
import java.util.List;

public class UniverseGraph {
    private List<Rule> rules = new ArrayList<>();
    public static class Rule {
        private Node[] applies = new Node[10000];
        private int count = 0;
        private UniverseGraph universe;
        public Rule(UniverseGraph universe) {
            this.universe = universe;
        }
        public void reset() {
            applies = new Node[50];
            count = 0;
        }
        public void check(Node root) {
            // check if node should be updated next tick
            var id = -1;
            var count = this.count;
            for (int i = 0; i < count; i++) {
                if (applies[i].equals(root)) { id = i; break; }
            }
            if (root.count > 1) {
                if (id == -1) {
                    applies[this.count++] = root;
                }
            } else if (id > -1) {
                applies[id] = applies[this.count-1];
                applies[this.count-1] = null;
                this.count--;
            }
        }
        public void apply() {
            var count = this.count;
            for (int i = 0; i < count; i++) {
                var node2 = applies[i].nodes[0];
                var node3 = applies[i].nodes[1];

                var node4 = new Node();
                universe.nodes[universe.count++] = node4;
                applies[i].replace(1, node4);
                node2.pair(node4);
                node3.pair(node4);
                // check every node if it's applicable by this rule
                // if so append them to applies

                this.check(applies[i]);
                this.check(node2);
                this.check(node3);
                this.check(node4);
            }
        }
    }
    static void main() {
        var universe = new UniverseGraph();
        universe.rules.add(new Rule(universe));
        universe.nodes[0] = new Node();
        universe.nodes[1] = new Node();
        universe.nodes[2] = new Node();

        universe.nodes[0].pair(universe.nodes[1]);
        universe.nodes[0].pair(universe.nodes[2]);
        universe.count = 3;

        for (int i = 0; i < universe.count; i++) {
            universe.rules.getFirst().check(universe.nodes[i]);
        }

        for (int i = 0; i < 12; i++) universe.rules.getFirst().apply();

        for (int i = 0; i < universe.count; i++) {
            System.out.print(universe.nodes[i].toString());
        }
    }
    private Node[] nodes = new Node[10000];
    private int count = 0;
    public static class Node {
        private Node[] nodes = new Node[20];
        public int count = 0;
        public int id;
        public static int ID = 0;

        @Override
        public String toString() {
            var sb = new StringBuilder();
            for (int i = 0; i < count; i++) {
                sb.append(id).append(" ").append(nodes[i].id).append("\n");
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Node && ((Node) obj).id == this.id;
        }
        public void remove(int id) {
            if (count < 2) {
                nodes = new Node[20];
                count = 0;
                return;
            }
            if (id-1 != count) {
                nodes[id] = nodes[count-1];
            }
            nodes[count-1] = null;

            count--;
        }

        public Node() {
            this.id = ID++;
        }
        public void replace(int id, Node node) {
            this.nodes[id] = node;
        }
        public Node pair(Node node) {
            // System.out.println("pair "+id+" "+node.id);
            this.nodes[count++] = node;
            return this;
        }
    }
//    public void rule(Function<Node, Boolean> check, ) {
//        var rules = rule.split("=>");
//
//        // query is executed on all nodes at beginning and on end of tick on every updated node
//        var query = rules[0].split(",");
//
//        // this is how the graph will change based on the rules,
//        // if rule selects anything and is not contain in this array too, then its destroyed
//        var result = rules[1].split(",");
//
//        var selfDestroying = true;
//
//        if (query.length <= result.length) {
//            for (int i = 0; i < query.length; i++) {
//                if (!query[i].equals(result[i])) break;
//                selfDestroying = false;
//            }
//        }
        // we know, when the rule is self-destroying
        // that means that the we need to destroy some nodes
//     }
}
