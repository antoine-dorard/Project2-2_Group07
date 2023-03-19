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

    public TreeControl() {
        super();
        DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Questions");
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        setModel(treeModel);
        treeModel.reload();

        try {
            JSONParser parser = new JSONParser(); // create a json parser
            // read the file and parse the data
            String file = "./app/src/main/resources/skills/actions.json";
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) obj;

            populate(rootNode, jsonObject);

        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }

        treeModel.reload();

    }

    //@Override
    //public void setForeground(Color color){
    //    (DefaultTreeCellRenderer) cellRenderer.setForeground(color);
    //}

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