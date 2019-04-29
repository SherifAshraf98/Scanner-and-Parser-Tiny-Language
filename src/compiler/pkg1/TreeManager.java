/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.pkg1;

import java.util.ArrayList;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author Sherif Ashraf
 */
public class TreeManager {
    Graph g = new SingleGraph("Tree");
    public void displayTree(MyNode n) {
        if (drawTreeFromMyNode(n) != null){
            System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
            g.addAttribute("ui.quality");
            g.addAttribute("ui.antialias");
            g.display(true);
        }
        
    }

    private MyNode drawTreeFromMyNode(MyNode n1) {
        if (n1 == null){
            return null;
        }
        g.addNode(n1.uniqueID);
        Node nTemp1 = g.getNode(n1.uniqueID);
        nTemp1.addAttribute("ui.label", n1.data);
        nTemp1.addAttribute("ui.style", "shape:box;size:10px,30px;fill-color: white;size: 30px; text-alignment: center;");
            for (int i = 0; i < n1.children.size(); i++) {
                MyNode n2 = drawTreeFromMyNode(n1.children.get(i));
                Node nTemp2 = g.getNode(n2.uniqueID);
                g.addEdge(n1.uniqueID + " - " + n2.uniqueID, nTemp1, nTemp2);
            }
            if (n1.sibling != null) {
                MyNode n2 = drawTreeFromMyNode(n1.sibling);
                Node nTemp2 = g.getNode(n2.uniqueID);
                g.addEdge(n1.uniqueID + " - " + n2.uniqueID, nTemp1, nTemp2);
            }
        return n1;

    }

}
class MyNode {
    public String data;
    public static int uniqueIDCounter = 0;
    public String uniqueID = "thisisauniqueid";
    public MyNode parent;
    public ArrayList<MyNode> children;
    public MyNode sibling;
    public MyNode(String rootData) {
        this.data = rootData;
        this.children = new ArrayList<MyNode>();
        this.uniqueID += uniqueIDCounter;
        uniqueIDCounter++;
    }
}
