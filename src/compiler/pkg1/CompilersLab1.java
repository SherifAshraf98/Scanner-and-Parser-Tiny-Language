/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.pkg1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Character.isDigit;
import java.util.ArrayList;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

/**
 *
 * @author ahmedalsai
 */
public class CompilersLab1 {
    public static void main(String[] args) throws IOException {
        File file = new File("code.txt");
        FileReader fr = new FileReader(file);
        ArrayList<Token> x = Scanner.getTokens(fr);
        for (int i = 0; i < x.size(); i++) {
            System.out.println(x.get(i).getValue());
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("parser_output.txt"));
        Parser p = new Parser(x, bw);
        MyNode n = p.getSyntaxTree();
        TreeManager tm = new TreeManager();
        tm.displayTree(n);
            

    
    }
}
