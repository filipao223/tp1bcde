import java.io.IOException;
import java.util.StringTokenizer;
import java.text.NumberFormat;
import java.util.*;
import java.util.Random;


// classe que representa nó da árvore
// por simplicidade é "hard-coded" para o problema e com atributos de acesso público

class Node {
    public String cartao;
    public int saldo;
    public Node next;
    public Node previous;
    public Node below;

    // Construtores
    Node(String cartao, int saldo) {
        this.cartao = cartao;
        this.saldo = saldo;
        next = null;
        previous = null;
    }

    Node (String cartao, int saldo, Node n, Node p) {
        this.cartao = cartao;
        this.saldo = saldo;
        next = n;
        previous = p;
    }

    // Comparadores. Unico critério é a String cartao. Restantes campos são ignorados
    int compareTo(String cartao) {
        return this.cartao.compareTo(cartao);
    }

    int compareTo(Node otherNode) {
        return this.compareTo(otherNode.cartao);
    }
}

class SkipListLevel{
    public Node root;
    public Node current = null;
    public boolean isEmpty;

    public SkipListLevel(){
        root = new Node("", 0, null, null);
        isEmpty = true;
    }

    public Node add(String cartao, int saldo){
        Node prox = root;

        if(isEmpty){
            root.next = new Node(cartao, saldo, null, root);
            isEmpty = false;
            return root.next;
        }
        else{
            for(current = root; prox!=null;){
                System.out.println(root);
                System.out.println(current);
                prox = current.next;
                if(current.compareTo(cartao) == 0){
                    current.saldo += saldo;
                    return null;
                }
                else if(prox == null){
                    current.next = new Node(cartao, saldo, null, current);
                    return current.next;
                }
                else if(prox.compareTo(cartao) > 0){
                    current.next = new Node(cartao, saldo, prox, current);
                    prox.previous = current.next;
                    return current.next;
                }
                current = prox;
            }
            return null;
        }
    }

    public Node get(String cartao, Node above){
        Node prox = root;
        for(current = above==null?root:above.below; prox!=null; current = prox){
            prox = current.next;
            if(prox!=null){
                if(current.compareTo(cartao) == 0 || prox==null || prox.compareTo(cartao)<0){
                    return current;
                }
            }
        }

        return null;
    }

    @Override
    public String toString(){
        String res="";
        for (current = root; current!=null; current = current.next){
            if(current == root) res += "ROOT";
            else res+=("--" + current.cartao + "--");
        }
        res+="NULL\n";
        return res;
    }
}

class SkipList {

    public int maxLevel;
    public SkipListLevel[] levels;
    public boolean isEmpty;
    public Random randomGen;


    //Construtores
    public SkipList(){
        this(4);
    }

    public SkipList(int n){
        this.maxLevel = 4;
        levels = new SkipListLevel[maxLevel];
        for(int i=0; i<maxLevel; i++) levels[i] = new SkipListLevel();
        for(int i=maxLevel-1; i>0; i--) levels[i].root.below = levels[i-1].root;
        isEmpty = true;
        randomGen = new Random(1);
    }

    // encontra determinado contribuinte na árvore (null se não encontrado)
    public Node get(String cartao) {
        int i=maxLevel-1;
        Node temp = null;

        while(i >= 0){
            temp = levels[i].get(cartao, temp);
            if(temp==null) continue;
            if(temp.compareTo(cartao) == 0) break;
        }

        return temp;
    }

    // adiciona novo nó à lista. Se já existe atualiza valor acumulado
    public void add(String cartao, int saldo) {
        Node aboveNode = null;
        Node belowNode = null;
        float num;

        //Coin flip
        int numberLevels = 1;
        while(numberLevels < maxLevel){
            num = randomGen.nextInt(100);
            if(num >50) numberLevels+=1;
            else break;
        }

        System.out.println("generated " + numberLevels + " levels");

        for (int i = numberLevels-1; i >= 0; i--) {
            if (aboveNode != null) aboveNode.below = belowNode;
            belowNode = levels[i].add(cartao, saldo);
            if (belowNode == null) break;
            aboveNode = belowNode;
        }
    }

    // remove contribuinte da arvore
    public void remove(Node no) {
        remove(no.cartao);
    }

    public void remove(String cartao){

    }

    protected Node remove(String cartao, Node no) {
        return null;
    }

    // imprime em ordem. para debugging e verificacao
    void printInOrder(){
        for(int i= maxLevel-1; i>=0; i--){
            System.out.println(levels[i].toString());
        }
    }
}


public class tp1d{

    public static void main(String[] arguments) {

        String input, comando;
        int contribuinte, valor;
        String cartao;
        StringTokenizer st;
        long startTime = System.currentTimeMillis();

        SkipList skiplist = new SkipList();

        do {  // enquanto houver mais linhas para ler...
            input = readLn(200);
            st= new StringTokenizer(input.trim());
            comando = st.nextToken();
            if (comando.equals("UPDATE")){
                cartao = new String(st.nextToken());
                valor = Integer.parseInt(st.nextToken());
                skiplist.add (cartao, valor);
            }
            else if (comando.equals("SALDO")){
                cartao = new String(st.nextToken());
                Node no = skiplist.get(cartao);
                if (no==null)
                    System.out.println(cartao + " INEXISTENTE");
                else
                    System.out.println(cartao + " SALDO " + no.saldo);
            }
            else if (comando.equals("REMOVE")){
                cartao = new String(st.nextToken());
                skiplist.remove(cartao);
            }
            else if (comando.equals("IMPRIME")){
                skiplist.printInOrder();
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
