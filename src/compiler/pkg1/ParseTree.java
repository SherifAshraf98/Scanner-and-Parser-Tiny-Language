/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.pkg1;

import java.util.ArrayList;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.spriteManager.*;
import org.graphstream.ui.view.View;

import java.util.ArrayList;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.spriteManager.*;
import org.graphstream.ui.view.View;

public class ParseTree {

    private ArrayList<NodeTree> nodeList = new ArrayList<>();

    public ParseTree(ArrayList x) {
        nodeList = x;
    }

    public void draw() {

        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph tree = new SingleGraph("Parse Tree");
        Viewer viewer = tree.display();
        View view = viewer.getDefaultView();

        tree.addAttribute("ui.quality");
        tree.addAttribute("ui.antialias");
        
        for (int i = 0; i < nodeList.size(); i++) {
            tree.addNode(nodeList.get(i).getValue());
            Node n1 = tree.getNode(nodeList.get(i).getValue());
            
            n1.addAttribute("ui.style", "shape:box;size:10px,30px;fill-color: white;size: 30px; text-alignment: center;");
            n1.addAttribute("ui.label", "Node " + nodeList.get(i).getValue());
        }
    }
}
