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
import org.graphstream.ui.view.Viewer;

/**
 *
 * @author Sherif Ashraf
 */
public class TreeManager {

    Graph g = new SingleGraph("Tree");

    public int x = 0;
    public int y = 0;
    public int z = 0;

    public void displayTree(MyNode n) {
        if (drawTreeFromMyNode(n) != null) {
            System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
            g.addAttribute("ui.quality");
            g.addAttribute("ui.antialias");
            Viewer v = g.display();
            v.disableAutoLayout();
        }

    }

    private MyNode drawTreeFromMyNode(MyNode n1) {
        int currentY = y;
        if (n1 == null) {
            return null;
        }
        g.addNode(n1.uniqueID);
        Node nTemp1 = g.getNode(n1.uniqueID);
        nTemp1.addAttribute("ui.label", n1.data);
        if (n1.data.startsWith("read") || n1.data.startsWith("assign") || n1.data.startsWith("write") || n1.data.startsWith("if") || n1.data.startsWith("repeat")) {
            nTemp1.addAttribute("ui.style", "shape:box;fill-color: white;stroke-mode: plain;size: 60px, 30px; text-alignment: center;text-size: 10;");

        } else {
            nTemp1.addAttribute("ui.style", "shape:circle;fill-color: white;stroke-mode: plain;size: 50px, 30px; text-alignment: center;text-size: 10;");

        }
        nTemp1.setAttribute("xyz", x, currentY, z);
        y -= 1;
        for (int i = 0; i < n1.children.size(); i++) {
            if (i != 0) {
                x += 1;
            }
            MyNode n2 = drawTreeFromMyNode(n1.children.get(i));
            Node nTemp2 = g.getNode(n2.uniqueID);
            g.addEdge(n1.uniqueID + " - " + n2.uniqueID, nTemp1, nTemp2);
        }
        y = currentY;
        if (n1.sibling != null) {

            x += 1;
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
