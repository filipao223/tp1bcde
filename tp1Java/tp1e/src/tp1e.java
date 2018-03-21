import javax.xml.bind.SchemaOutputResolver;
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

    // Construtores
    Node(String cartao, int saldo) {
        this.cartao = cartao;
        this.saldo = saldo;
        left = null;
        right = null;
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
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(cartao +" "+saldo).append("\n");
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

    public Node get(Node root, String cartao) {
        return splay(root, cartao);
    }

    // adiciona novo nó à árvore. Se já existe atualiza valor acumulado

    protected Node add(String cartao, int valor, Node root) {
        // contribuinte ainda nao existe, cria novo contribuinte.
        if (root == null) {
            return new Node (cartao, valor);
        }

        Node newNode = null;

        root = splay(root, cartao);

        if(root.compareTo(cartao)==0){
            newNode = root;
            root.saldo += valor;
        }
        else if(root.compareTo(cartao) < 0){
            newNode = new Node(cartao, valor);

            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        else{
            newNode = new Node(cartao, valor);

            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        }

        return newNode;
    }

    // remove contribuinte da arvore
    public Node remove(String cartao){
        return remove(cartao, root);
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

    public Node RightRotation(Node node){
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        return temp;
    }

    public Node LeftRotation(Node node){
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        return temp;
    }

    public Node splay(Node root,String cartao){ //Caso especial se for SALDO?

        boolean hasChanged = true;
        Node temp=null;

        if(root == null || root.compareTo(cartao)==0) return null;

        if(root.compareTo(cartao) > 0){ //Chave é menor
            if(root.left == null) return null; //Nao existe

            if(root.left.compareTo(cartao) > 0){
                if(root.left.left != null){
                    temp = splay(root.left.left, cartao);
                    if(temp != null){
                        root.left.left = temp;
                        root = RightRotation(root);
                    }
                    else hasChanged = false;
                }
            }
            else if(root.left.compareTo(cartao) < 0){
                if(root.left.right != null){
                    temp = splay(root.left.right, cartao);
                    if(temp != null){
                        root.left.right = temp;
                        root.left = LeftRotation(root.left);
                    }
                    else hasChanged = false;
                }
            }
            else{
                //Node já existe
                return RightRotation(root);
            }

            return hasChanged?RightRotation(root):root;
        }
        else{
            if(root.right == null) return root;

            if(root.right.compareTo(cartao) < 0){
                if(root.right.right != null){
                    temp = splay(root.right.right, cartao);
                    if(temp != null){
                        root.right.right = temp;
                        root = LeftRotation(root);
                    }
                    else hasChanged = false;
                }
            }
            else if(root.right.compareTo(cartao) > 0){
                if(root.right.left != null){
                    root.right.left = splay(root.right.left, cartao);
                    if(temp != null){
                        root.right.left = temp;
                        root.right = RightRotation(root.right);
                    }
                    else hasChanged = false;
                }
            }
            else{
                //Node já existe
                return LeftRotation(root);
            }

            return hasChanged?LeftRotation(root):root;
        }
    }
}


public class tp1e{

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
                tree.root = tree.add (cartao, valor, tree.root);
            }
            else if (comando.equals("SALDO")){
                cartao = new String(st.nextToken());
                if(tree==null || tree.root==null){
                    System.out.println(cartao + " INEXISTENTE");
                }
                else{
                    Node oldRoot = tree.root;
                    tree.root = tree.get(tree.root, cartao);
                    if(oldRoot!=tree.root || tree.root.compareTo(cartao)==0){
                        System.out.println(oldRoot!=tree.root?"Root has changed":"Root hasnt changed");
                        System.out.println(cartao + " SALDO " + tree.root.saldo);
                    }
                    else
                        System.out.println(cartao + " INEXISTENTE");
                }
            }
            else if (comando.equals("REMOVE")){
                cartao = new String(st.nextToken());
                tree.root = tree.remove(cartao);
            }
            else if (comando.equals("IMPRIME")){
                tree.printInOrder();
                System.out.println(tree.root);
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
