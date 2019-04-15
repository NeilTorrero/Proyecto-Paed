package AVL;


import com.google.gson.Gson;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AVLTree<T extends Comparable<T>> {
    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        ArrayList<Integer> insertioTime = new ArrayList<Integer>();

        for (int i = 0; i < 100; i++) {
            int randomInt = (int)(Math.random()*100);
            tree.insert(randomInt);
            insertioTime.add(randomInt);
        }
        visualize(tree,insertioTime);
    }



    Node<T> root;

    //Implementacion de la busqueda

    public boolean search(T element){
        return searchInside(root,element);
    }

    private boolean searchInside(Node<T> node, T element) {
        if (node.data == element){
            return true;
        }
        if (node.leftChild != null){
            return searchInside(node.leftChild,element);
        }
        if (node.rightChild != null){
            return searchInside(node.rightChild,element);
        }
        return false;
    }


    //Implementacion de las rotaciones

    private Node<T> rotateLL(Node<T> root) {
        Node<T> leftBranchOfRoot = root.leftChild;
        root.leftChild = leftBranchOfRoot.rightChild;
        leftBranchOfRoot.rightChild = root;
        leftBranchOfRoot.factor = 0;
        root.factor = 0;
        return leftBranchOfRoot;
    }

    private Node<T> rotateRR(Node<T>  root) {
        Node<T> rightBranchOfRoot = root.rightChild;
        root.rightChild = rightBranchOfRoot.leftChild;
        rightBranchOfRoot.leftChild = root;
        rightBranchOfRoot.factor = 0;
        root.factor = 0;
        return rightBranchOfRoot;
    }


    private Node<T> rotateLR(Node<T>  root) {
        Node<T> leftBranchOfRoot = root.leftChild;
        Node<T> leftBranchOfRootRightBranch = leftBranchOfRoot.rightChild;
        root.leftChild = leftBranchOfRootRightBranch.rightChild;
        leftBranchOfRootRightBranch.rightChild = root;

        leftBranchOfRoot.rightChild = leftBranchOfRootRightBranch.leftChild;
        leftBranchOfRootRightBranch.leftChild = leftBranchOfRoot;

        if(leftBranchOfRootRightBranch.factor == -1){
            leftBranchOfRoot.factor = +1;
        }else{
            leftBranchOfRoot.factor = 0;
        }
        if (leftBranchOfRootRightBranch.factor == +1){
            root.factor = -1;
        }else{
            root.factor = 0;
        }
        leftBranchOfRootRightBranch.factor = 0;
        return leftBranchOfRootRightBranch;
    }

    private Node<T> rotateRL(Node<T>  root) {
        Node<T> rightBranchOfRoot = root.rightChild;
        Node<T> rightBranchOfRootLeftBranch = rightBranchOfRoot.leftChild;
        root.rightChild = rightBranchOfRootLeftBranch.leftChild;
        rightBranchOfRootLeftBranch.leftChild = root;

        rightBranchOfRoot.leftChild = rightBranchOfRootLeftBranch.rightChild;
        rightBranchOfRootLeftBranch.rightChild = rightBranchOfRoot;

        if(rightBranchOfRootLeftBranch.factor == -1){
            root.factor = 1;
        }else{
            root.factor = 0;
        }
        if (rightBranchOfRootLeftBranch.factor == 1){
            rightBranchOfRoot.factor = -1;
        }else{
            rightBranchOfRoot.factor = 0;
        }
        rightBranchOfRootLeftBranch.factor = 0;
        return rightBranchOfRootLeftBranch;
    }


    public void insert(T data) {
        Logical act = new Logical();
        try {
            root = insert(data, root,act);
        } catch (Exception e) {
            System.out.println("Eroor al añadir un item");
        }
    }

    //Implementacion de la insercion


    private Node<T> insert(T data, Node<T> node, Logical act) throws Exception {
        if (node == null){
            node = new Node<T>(data);
            act.bool = true;
        }else{
            int cmp = data.compareTo(node.data);
            if (cmp == 0) {
                throw new Exception("No se pueden introducir nodos repetidos");
            }
            if (cmp < 0) {
                node.leftChild = insert(data, node.leftChild,act);
                if (act.bool) {
                    if (node.factor == -1) {
                        node.factor = 0;
                        act.bool = false;
                    }
                    else if (node.factor == 0) {
                        node.factor = 1;
                        act.bool = true;
                    }
                    else if (node.factor == 1) {
                        if (node.leftChild.factor == 1) {
                            node = rotateLL(node);
                        } else {
                            node = rotateLR(node);
                        }
                        act.bool = false;
                    }
                }
            }
            if (cmp > 0) {
                node.rightChild = insert(data, node.rightChild,act);
                if (act.bool) {
                    if (node.factor == 1) {
                        node.factor = 0;
                        act.bool = false;
                    }
                    else if (node.factor == 0) {
                        node.factor = -1;
                        act.bool = true;
                    }
                    else if (node.factor == -1) {
                        if (node.rightChild.factor == 1) {
                            node = rotateRL(node);
                        } else {
                            node = rotateRR(node);
                        }
                        act.bool = false;
                    }
                }
            }
        }
        return node;
    }

    //TODO : Implementacion de la eliminacion


    //TODO : Implementacion checkBalanceo

    public boolean balanced(Node<T> node){
        int leftHeight;

        int rightHeight;

        if (node == null){
            return true;
        }

        leftHeight = height(node.leftChild);
        rightHeight = height(node.rightChild);

        return Math.abs(leftHeight - rightHeight) <= 1 && balanced(node.leftChild) && balanced(node.rightChild);

    }

    public int height(Node<T> node)
    {
        if (node == null)
            return 0;

        return 1 + Math.max(height(node.leftChild), height(node.rightChild));
    }


    // Implementacion de Visualizaciones


    public void preOrder(){
        preOrderPriv(root);
    }

    private void preOrderPriv(Node<T> node){
        System.out.println(node.data);
        if (node.leftChild != null){
            preOrderPriv(node.leftChild);
        }
        if (node.rightChild != null){
            preOrderPriv(node.rightChild);
        }
    }

    public void inOrder(){
        inOrderPriv(root);
    }

    private void inOrderPriv(Node<T> node){
        System.out.println(node.data);
        if (node.leftChild != null){
            inOrderPriv(node.leftChild);
        }
        if (node.rightChild != null){
            inOrderPriv(node.rightChild);
        }
    }

    public void postOrder(){
        postOrderPriv(root);
    }

    private void postOrderPriv(Node<T> node){
        System.out.println(node.data);
        if (node.leftChild != null){
            postOrderPriv(node.leftChild);
        }
        if (node.rightChild != null){
            postOrderPriv(node.rightChild);
        }
    }

    private class Logical {
        Boolean bool = false;
    }

    private static void visualize(AVLTree<Integer> tree,ArrayList insertioTime) {
        Gson gson = new Gson();
        try {
            FileWriter fw = new FileWriter("src/main/Visualizador/VisualizadorDeArbolAVL/index.html");
            String treeString = gson.toJson(tree.root);
            fw.write("<!DOCTYPE html>\n" +
                    "<html lang=\"\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>p5.js example</title>\n" +
                    "    <style> body {padding: 0; margin: 0;} </style>\n" +
                    "    <script src=\"../p5.min.js\"></script>\n" +
                    "    <script src=\"../addons/p5.dom.min.js\"></script>\n" +
                    "    <script src=\"../addons/p5.sound.min.js\"></script>\n" +
                    "    <script src=\"sketch.js\"></script>\n" +
                    "    <p id=\"tree\" >"+treeString+"</p>\n" +
                    "    <p id=\"insertioTime\" >"+insertioTime.toString()+"</p>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "  </body>\n" +
                    "</html>");
            fw.close();
            File file = new File("src/main/Visualizador/VisualizadorDeArbolAVL/index.html");
            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
