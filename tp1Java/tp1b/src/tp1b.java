import java.io.IOException;
import java.util.StringTokenizer;
import java.text.NumberFormat;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


// classe que representa nó da árvore
// por simplicidade é "hard-coded" para o problema e com atributos de acesso público

class Node {
    public String cartao;
    public int saldo;
    public Node left;
    public Node right;

    public int weightLeft;
    public int weightRight;

    // Construtores
    Node(String cartao, int saldo) {
        this.cartao = cartao;
        this.saldo = saldo;
        left = null;
        right = null;
        this.weightLeft=0;
        this.weightRight=0;
    }

    Node(String cartao, int saldo, Node l, Node r) {
        this.cartao = cartao;
        this.saldo = saldo;
        left = l;
        right = r;
    }

    // Comparadores. Unico critério é a String cartao. Restantes campos são ignorados
    int compareTo(String cartao) {
        return this.cartao.compareTo(cartao);
    }

    public StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
        if(right!=null) {
            right.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(cartao).append("\n");
        if(left!=null) {
            left.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
    }

    int maxWeight(){
        return this.weightLeft > this.weightRight ? this.weightLeft : this.weightRight;
    }

    int maxWeight(Node node){
        if(node != null){
            return node.maxWeight();
        }
        else return -1;
    }

    int compareTo(Node otherNode) {
        return this.compareTo(otherNode.cartao);
    }
}



// classe que representa árvore binária. Implementacao hard-coded para o problema.

class BST {

    /* raiz da árvore. Null quando árvore vazia */
    protected Node root = null;

    /* construtores */

    public BST() {
        root = null;
    }

    public BST (Node no) {
        root = no;
    }

    public BST(String cartao, int saldo) {
        root = new Node(cartao, saldo);
    }

    // encontra determinado contribuinte na árvore (null se não encontrado)
    public Node get(Node no) {
        return this.get(no.cartao);
    }

    public Node get(String cartao) {
        Node no = root;
        while (no != null) {
            if (no.compareTo(cartao) == 0) {
                return no;
            }
            no = ((no.compareTo(cartao) > 0) ? no.left : no.right);
        }
        return null;
    }

    // adiciona novo nó à árvore. Se já existe atualiza valor acumulado
    public void add(String cartao, int saldo) {
        root = add(cartao, saldo, root);
        return;
    }

    protected Node add(String cartao, int valor, Node node) {
        // contribuinte ainda nao existe, cria novo contribuinte.
        if (node == null) {
            return new Node (cartao, valor);
        }

        // contribuinte já existe, adiciona a valor acumulado
        if (node.compareTo(cartao) == 0) {
            node.saldo += valor;
            // ainda nao encontrou. desce mais um nível
        } else {
            if (node.compareTo(cartao) > 0) {
                node.left = add(cartao, valor, node.left);
                node.weightLeft = node.left.maxWeight() + 1;
            } else {
                node.right = add(cartao, valor, node.right);
                node.weightRight = node.right.maxWeight() + 1;
            }
        }

        /*Verifica desequilibrio*/
        if(node.weightLeft - node.weightRight > 1) node = balanceBST(node, "left");
        else if(node.weightRight - node.weightLeft > 1) node = balanceBST(node, "right");

        return node;
    }

    // remove contribuinte da arvore
    public void remove(Node no) {
        remove(no.cartao);
    }

    public void remove(String cartao){
        root = remove(cartao, root);
    }

    protected Node remove(String cartao, Node no) {
        if (no == null) {	// arvore vazia ou no nao encontrado
            return null;
        }
        if (no.compareTo(cartao) == 0) {  // remove este no
            if (no.left == null) {              // um unico filho, "linka" e sai
                return no.right;
            } else if (no.right == null) {      // idem...
                return no.left;
            } else {                            // dois filhos...
                // troca valores com rigthmost e depois remove...
                Node aux = getRightmost(no.left);
                no.cartao = aux.cartao;
                no.saldo = aux.saldo;
                no.weightLeft -= 1;
                aux.cartao = cartao; // saldo não precisa de ser mudado...
                no.left = remove(cartao, no.left);
            }
        } else {                                // continua a descer pela esq ou dir
            if (no.compareTo(cartao) > 0) {
                // pela esquerda...
                no.left = remove(cartao, no.left);
                no.weightLeft -= 1;
            } else {        // ou pela direita...
                no.right = remove(cartao, no.right);
                no.weightRight -= 1;
            }
        }
        return no;
    }

    protected Node getRightmost(Node no) {
        return ((no.right == null) ? no : getRightmost(no.right));
    }

    // imprime em ordem. para debugging e verificacao
    void printInOrder(){
        printInOrder(root);
    }

    void printInOrder(Node no){
        if (no==null)
            return;
        printInOrder(no.left);
        System.out.println(no.cartao + " SALDO " + no.saldo);
        printInOrder(no.right);
    }

    public Node balanceBST(Node node, String side){
        if(Objects.equals(side, "left")){ /*Se o desnivel for na esquerda*/
            if(node.left.weightLeft > node.left.weightRight){ /*Se o filho da esquerda for mais pesado*/
                node = RightRotation(node);
            }
            else{
                node = DRRotation(node);
            }
        }
        else{ /*Se o desnivel for na direita*/
            if(node.right.weightRight > node.right.weightLeft){ /*Se o filho da direita for mais pesado*/
                node = LeftRotation(node);
            }
            else{
                node = DLRotation(node);
            }
        }
        return node;
    }

    /*Rotations*/
    public Node LeftRotation(Node node){
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        if(temp.right != null){
            temp.right.weightRight = temp.right.maxWeight(temp.right.right) + 1; temp.right.weightLeft = temp.right.maxWeight(temp.right.left) + 1;
        }
        temp.weightRight = temp.maxWeight(temp.right) + 1;
        if(temp.left != null){
            temp.left.weightRight = temp.left.maxWeight(temp.left.right) + 1; temp.left.weightLeft = temp.left.maxWeight(temp.left.left) + 1;
        }
        temp.weightLeft = temp.maxWeight(temp.left) + 1;

        return temp;
    }

    public Node RightRotation(Node node){
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        if(temp.right != null){
            temp.right.weightRight = temp.right.maxWeight(temp.right.right) + 1; temp.right.weightLeft = temp.right.maxWeight(temp.right.left) + 1;
        }
        temp.weightRight = temp.maxWeight(temp.right) + 1;
        if(temp.left != null){
            temp.left.weightRight = temp.left.maxWeight(temp.left.right) + 1; temp.left.weightLeft = temp.left.maxWeight(temp.left.left) + 1;
        }
        temp.weightLeft = temp.maxWeight(temp.left) + 1;

        return temp;
    }

    public Node DLRotation(Node node){
        node.right = RightRotation(node.right);

        node = LeftRotation(node);

        return node;
    }

    public Node DRRotation(Node node){
        node.left = LeftRotation(node.left);

        node = RightRotation(node);

        return node;
    }
}



// classe que representa nó da árvore
// por simplicidade é "hard-coded" para o problema e com atributos de acesso público



// classe que representa árvore binária. Implementacao hard-coded para o problema.



class tp1b{

    public static void main(String[] arguments) {

        String input, comando;
        int contribuinte, valor;
        String cartao;
        StringTokenizer st;

        BST tree = new BST();

        long startTime = System.currentTimeMillis();

        do {  // enquanto houver mais linhas para ler...
            input = readLn(200);
            st= new StringTokenizer(input.trim());
            comando = st.nextToken();
            if (comando.equals("UPDATE")){
                cartao = new String(st.nextToken());
                valor = Integer.parseInt(st.nextToken());
                tree.add (cartao, valor);
            }
            else if (comando.equals("SALDO")){
                cartao = new String(st.nextToken());
                Node no = tree.get(cartao);
                if (no==null)
                    System.out.println(cartao + " INEXISTENTE");
                else
                    System.out.println(cartao + " SALDO " + no.saldo);
            }
            else if (comando.equals("REMOVE")){
                cartao = new String(st.nextToken());
                tree.remove(cartao);
            }
            else if (comando.equals("IMPRIME")){
                tree.printInOrder();
                //System.out.println(tree.root);
            }
            else if (comando.equals("TERMINA")){
                return;
            }
        } while (true);
    }

    // leitura do input
    static String readLn (int maxLg){ //utility function to read from stdin
        byte lin[] = new byte [maxLg];
        int lg = 0, car = -1;
        String line = "";
        try {
            while (lg < maxLg){
                car = System.in.read();
                if ((car < 0) || (car == '\n')) break;
                lin [lg++] += car;
            }
        }
        catch (IOException e){
            return (null);
        }
        if ((car < 0) && (lg == 0)) return (null);  // eof
        return (new String (lin, 0, lg));
    }
}