package controls;

import utils.ConfigUI;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class TreeControl extends JTree {

    ConfigUI configUI = new ConfigUI();
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Questions");
    DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer(){
        @Override
        public Color getBackgroundNonSelectionColor() {
            return (null);
        }
        @Override
        public Color getBackgroundSelectionColor() {
            return (configUI.colorListSelectionBG);
        }
        @Override
        public Color getBackground() {
            return (null);
        }
        @Override
        public Font getFont() {
            return configUI.fontList;
        }
        @Override
        public Color getForeground() {
            return configUI.colorListFG;
        }
        @Override
        public Color getBorderSelectionColor() {
            return (null);
        }
    };

    public TreeControl(int width, int height) {
        super();

        setModel(treeModel);
        setCellRenderer(cellRenderer);
        setOpaque(false);
        setForeground(new Color(255,255,255));
        setPreferredSize(new Dimension(width, height));
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

            // expand all nodes again.
            // for some reason wasn't working...
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
            catch (Exception ignore){
                //newNode.setUserObject((String) newNode.getUserObject(), jsonObject.get(keyStr));
                //nodeObject nodeObject = new nodeObject((String) newNode.getUserObject(),(String) jsonObject.get(keyStr));
                //newNode.setUserObject(nodeObject);
            }
        }
    }
}