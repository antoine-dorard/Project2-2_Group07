package controls;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class TreeControl extends JTree {

    //DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Questions");
    DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

    public TreeControl() {
        super();
        setModel(treeModel);
        setOpaque(false);
        setForeground(new Color(255,255,255));
        treeModel.reload();
    }

    public void parseJSONtoTree(String jsonFile){
        try {
            JSONParser parser = new JSONParser(); // create a json parser
            // read the file and parse the data
            Object obj = parser.parse(new FileReader(jsonFile));
            JSONObject jsonObject = (JSONObject) obj;
            // start with an empty tree again.
            rootNode.removeAllChildren();
            // populate the entire tree again.
            populate(rootNode, jsonObject);
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        treeModel.reload();
    }

    protected void populate(DefaultMutableTreeNode node, JSONObject jsonObject) {
        for (Object keyStr : jsonObject.keySet()) {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(keyStr);
            node.add(newNode);
            try {
                JSONObject newJSONObject = (JSONObject) jsonObject.get(keyStr);
                if (newJSONObject.keySet().size() > 0) {
                    populate(newNode, newJSONObject);
                }
            }
            catch (Exception ignore){}
        }
    }

}