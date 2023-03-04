package tree;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String name;
    private String response;
    protected Node parent;
    protected List<Node> children;

    public Node(String name, String response) {

        this.name = name;
        this.response = response;

        this.parent = null;
        this.children = new ArrayList<>();

    }
    public String returnResponse(){
        return response;
    }
}
