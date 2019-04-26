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

    ArrayList<Object> syntaxTree = new ArrayList<>();
    ArrayList<Integer> index = new ArrayList<Integer>();
    Node currentNode;
    ArrayList<Object> currentArrayList = syntaxTree; 
    Graph g = new SingleGraph("Tree");

    public TreeManager() {
        index.add(0);
    }

    public void displayTree() {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        g.addAttribute("ui.quality");
        g.addAttribute("ui.antialias");
        g.display(true);
    }

    public void addNewElement(String type, String value) {
        String temp;
        switch (type) {
            case "read":
                temp = "read (" + value + ")";
                addToArraylist(temp);
                addNode(temp);
                connectNode(temp);
                printIndex();
                break;
            case "write":
                temp = "write";
                addToArraylist(temp);
                addNode(temp);
                connectNode(temp);
                addToArraylist(new ArrayList<Object>());
                addNewElement("read", "x");
                pop();
                printIndex();
                break;
        }
    }

    public void addToArraylist(String s) {
        addToSyntax(s);
    }
    public void addToArraylist(ArrayList<Object> o) {
        addToSyntax(o);
    }
    public void addToSyntax(String s){
        ArrayList<Object> temp = syntaxTree;
        if (index.size() >= 2){
        for (int i = 0; i < index.size() - 2; i++){
            temp = (ArrayList<Object>) temp.get(i);
            currentArrayList = temp;
        }
        }
        else {
            currentArrayList = syntaxTree;
        }
        index.set(index.size() - 1, index.get(index.size() - 1) + 1);
        currentArrayList.add(s);
    }
    public void addToSyntax(ArrayList<Object> o){
        ArrayList<Object> temp = syntaxTree;
        if (index.size() >= 2){
        for (int i = 0; i < index.size() - 2; i++){
            temp = (ArrayList<Object>) temp.get(i);
            currentArrayList = temp;
        }
        }
        else{
            currentArrayList = syntaxTree;
        }
        index.add(0);
        printIndex();
        currentArrayList.add(o);
        for (int i = 0; i < index.size() - 2; i++){
            temp = (ArrayList<Object>) temp.get(i);
            currentArrayList = temp;
        }
    }

    public void addNode(String s) {
        String sTemp = generateCurrentID();
        g.addNode(sTemp);
        Node nTemp = g.getNode(sTemp);
        nTemp.addAttribute("ui.label", s);
        nTemp.addAttribute("ui.style", "shape:box;size:10px,30px;fill-color: white;size: 30px; text-alignment: center;");

    }

    public void connectNode(String s) {
        String sTemp = generateCurrentID();
        if (currentNode != null) {
            g.addEdge(currentNode.getId() + sTemp, currentNode.getId(), sTemp, true);
            currentNode = g.getNode(sTemp);
        } else {
            currentNode = g.getNode(sTemp);
        }
    }

    public String generateCurrentID() {
        String temp = "";
        for (int i = 0; i < index.size(); i++) {
            temp += index.get(i) + "-";
        }
        return temp;

    }
    public void printIndex(){
        for (int i = 0; i < index.size(); i++){
            System.out.print(index.get(i) + " ");
        }
        System.out.println();
    }
    void pop() {
        index.remove(index.size() - 1);
        String tempID = generateCurrentID();
        currentNode = g.getNode(tempID);
    }

}
