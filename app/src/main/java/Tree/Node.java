package Tree;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public String name;
    public String response;
    public Node parent;
    public List<Node> children;

    public Node(String name, String response,Node parent) {

        this.name = name;
        this.response = response;

        this.parent = parent;
        this.children = new ArrayList<>();

    }
    public String returnResponse(){
        return response;
    }
}
