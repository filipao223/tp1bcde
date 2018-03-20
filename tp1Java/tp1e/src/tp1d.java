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
            } else {
                node.right = add(cartao, valor, node.right);
            }
        }
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

    public Node zig(Node node){
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        return temp;
    }

    public Node zag(Node node){
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        return temp;
    }

    public Node ZigZig(Node node){
        Node temp = zig(node);
        temp = zig(temp);
        return temp;
    }

    public Node ZagZag(Node node){
        Node temp = zag(node);
        temp = zag(temp);
        return temp;
    }

    public Node ZigZag(Node node){
        Node temp = zag(node);
        temp = zig(temp);
        return temp;
    }

    public Node ZagZig(Node node){
        Node temp = zig(node);
        temp = zag(temp);
        return temp;
    }

    public Node splay(Node root, Node node){
        if(root == node || root == node){ //Node is root
            return root;
        }

        if(root.left == node) return zig(node); //Node is left child of root
        if(root.right == node) return zag(node); //Node ir right child of root

        if(root.left != null && root.left.left == node) return ZigZig(node); //Node is left child of parent and parent is left child
        if(root.right != null && root.right.right == node) return ZagZag(node); //Node is right child of parent and parent is right child

        if(root.left != null && root.left.right == node) return ZagZig(node); //Node is right child of parent and parent is left child
        if(root.right != null && root.right.left == node) return ZigZag(node); //Node is left child of parent and parent is right child
    }
}


public class tp1d{

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
