import java.io.IOException;
import java.util.StringTokenizer;
import java.text.NumberFormat;
import java.util.*;


// classe que representa nó da árvore
// por simplicidade é "hard-coded" para o problema e com atributos de acesso público

class Node {
    public String cartao;
    public int saldo;
    public Node left;
    public Node right;
    public Node parent;
    public boolean red;

    // Construtores
    Node(String cartao, int saldo) {
        this.cartao = cartao;
        this.saldo = saldo;
        left = null;
        right = null;
        parent = null;
        red = true;
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

    int compareTo(Node otherNode) {
        return this.compareTo(otherNode.cartao);
    }

    public StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
        if(right!=null) {
            right.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(cartao + " " + (red?"r":"b")).append("\n");
        if(left!=null) {
            left.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
    }
}

//Objecto devolvido pelos metodos add e remove, para devolver 2 nodes de uma vez
//a raiz da arvore, e o novo node

class ReturnObj{
    Node newNode;
    Node rootNode;

    public ReturnObj(Node newNode, Node rootNode){
        this.newNode = newNode;
        this.rootNode = rootNode;
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
    public BST add(BST tree,String cartao, int saldo) {
        ReturnObj returnObj = add(cartao, saldo, root);
        if(returnObj.newNode == null) return tree;
        tree.root = returnObj.rootNode==null ? returnObj.newNode : returnObj.rootNode;
        addFix(tree, returnObj.newNode);
        return tree;
    }

    protected ReturnObj add(String cartao, int valor, Node node) {
        // contribuinte ainda nao existe, cria novo contribuinte.
        ReturnObj returnObj = null;
        if (node == null) {
            Node newNode =  new Node (cartao, valor);
            returnObj = new ReturnObj(newNode, null);
            return returnObj;
        }

        // contribuinte já existe, adiciona a valor acumulado
        if (node.compareTo(cartao) == 0) {
            node.saldo += valor;
            returnObj.newNode = null;
            // ainda nao encontrou. desce mais um nível
        } else {
            if (node.compareTo(cartao) > 0) {
                returnObj = add(cartao, valor, node.left);
                node.left = node.left==null?returnObj.newNode:node.left;
                node.left.parent = node;
                returnObj.rootNode = node;
            } else {
                returnObj = add(cartao, valor, node.right);
                node.right = node.right==null?returnObj.newNode:node.right;
                node.right.parent = node;
                returnObj.rootNode = node;
            }
        }
        return returnObj;
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
                aux.cartao = cartao; // saldo não precisa de ser mudado...
                no.left = remove(cartao, no.left);
            }
        } else {                                // continua a descer pela esq ou dir
            if (no.compareTo(cartao) > 0) {
                // pela esquerda...
                no.left = remove(cartao, no.left);
            } else {		// ou pela direita...
                no.right = remove(cartao, no.right);
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



    public Node addFix(BST tree, Node node){
        if(node.parent == null){
            node.red = false;
            return null;
        }
        else{
            if(node.parent.parent != null){
                //Verifica se tio é nulo|preto, ou vermelho
                if(hasRedUncle(node)){ //tio vermelho
                    node.parent.parent.red = node.parent.parent.red?false:true;
                    node.parent.red = node.parent.red?false:true;
                    recolorUncle(node);
                    if(node.parent == null){
                        tree.root = node;
                        tree.root.red = false;
                        return null;
                    }
                    else return addFix(tree, node.parent.parent);
                }
                else{ //tio preto
                    if(checkRightChild(node.parent)){ //Pai é right child
                        if(checkRightChild(node)){ //Filho é right child
                            recolorParents(node);
                            node = LeftRotation(node.parent.parent);
                            node.parent = node.left.parent;
                            if(node.parent != null){
                                if(checkRightChild(node)) node.parent.right = node;
                                else node.parent.left = node;
                            }
                            node.left.parent = node;
                            node.right.parent = node;
                            if(node.parent == null){
                                tree.root = node;
                                tree.root.red = false;
                                return null;
                            }
                            else return addFix(tree, node.parent);
                        }
                        else{ //Filho é left child
                            node = RightRotation(node.parent);
                            recolorParents(node);
                            node = LeftRotation(node.parent.parent);
                            node.parent = node.left.parent;
                            if(node.parent != null){
                                if(checkRightChild(node)) node.parent.right = node;
                                else node.parent.left = node;
                            }
                            node.left.parent = node;
                            node.right.parent = node;
                            if(node.parent == null){
                                tree.root = node;
                                tree.root.red = false;
                                return null;
                            }
                            else return addFix(tree, node.parent);
                        }
                    }
                    else{
                        //Pai é left child
                        if(checkRightChild(node)){ //filho é right child
                            node = LeftRotation(node.parent);
                            recolorParents(node);
                            node = RightRotation(node.parent.parent);
                            node.parent = node.right.parent;
                            if(node.parent != null){
                                if(checkRightChild(node)) node.parent.right = node;
                                else node.parent.left = node;
                            }
                            node.left.parent = node;
                            node.right.parent = node;
                            if(node.parent == null){
                                tree.root = node;
                                tree.root.red = false;
                                return null;
                            }
                            else return addFix(tree, node.parent);
                        }
                        else{ //filho é left child
                            recolorParents(node);
                            node = RightRotation(node.parent.parent);
                            node.parent = node.right.parent;
                            if(node.parent != null){
                                if(checkRightChild(node)) node.parent.right = node;
                                else node.parent.left = node;
                            }
                            node.left.parent = node;
                            node.right.parent = node;
                            if(node.parent == null){
                                tree.root = node;
                                tree.root.red = false;
                                return null;
                            }
                            else return addFix(tree, node.parent);
                        }
                    }
                }
            }
            return null;
        }
    }

    public Node LeftRotation(Node node){
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;

        return temp;
    }

    public Node RightRotation(Node node){
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;

        return temp;
    }

    public boolean hasRedUncle(Node node){
        if(node.parent.parent.right == null) return false;
        if(node.parent.parent.left == null) return false;
        return true;
    }

    public void recolorUncle(Node node){
        if(node.parent.parent.right != null){
            node.parent.parent.right.red = node.parent.parent.right.red?false:true;
        }
        else if(node.parent.parent.left != null){
            node.parent.parent.left.red = node.parent.parent.left.red?false:true;
        }
    }

    public void recolorParents(Node node){
        node.parent.red = node.parent.red?false:true;
        node.parent.parent.red = node.parent.parent.red?false:true;
    }

    public boolean checkRightChild(Node node){
        if(node == node.parent.right) return true;
        return false;
    }
}


public class tp1c{

    public static void main(String[] arguments) {

        String input, comando;
        int contribuinte, valor;
        String cartao;
        StringTokenizer st;
        long startTime = System.currentTimeMillis();

        BST tree = new BST();

        do {  // enquanto houver mais linhas para ler...
            input = readLn(200);
            st= new StringTokenizer(input.trim());
            comando = st.nextToken();
            if (comando.equals("UPDATE")){
                cartao = new String(st.nextToken());
                valor = Integer.parseInt(st.nextToken());
                tree = tree.add(tree, cartao, valor);
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
