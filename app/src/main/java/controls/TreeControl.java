package controls;

import utils.ConfigUI;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

public class TreeControl extends JTree {

    ConfigUI configUI = new ConfigUI();
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Questions");
    DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    public JScrollPane listScroller = new JScrollPane();

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

    public TreeControl() {
        super();

        setModel(treeModel);
        setCellRenderer(cellRenderer);
        setOpaque(false);
        setForeground(new Color(255,255,255));
        //setPreferredSize(new Dimension(width, height));
        treeModel.reload();

        // define the list scroller for the list control.
        listScroller.setViewportView(this);
        listScroller.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if(listScroller!=null){
            listScroller.getViewport().setBackground(bg);
        }
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
                // add the value to the last node possible.
                String nodeObject = (newNode.getUserObject() + " : " + jsonObject.get(keyStr));
                newNode.setUserObject(nodeObject);
            }
        }
    }

    public JSONObject convertTreeToJson(DefaultMutableTreeNode node) {
        JSONObject obj = new JSONObject();
        Enumeration<TreeNode> children = node.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            if (child.isLeaf()) {
                obj.put(child.toString(), "");
            } else {
                obj.put(child.toString(), convertTreeToJson(child));
            }
        }
        return obj;
    }

    public void writeJsonToFile(JSONObject json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}