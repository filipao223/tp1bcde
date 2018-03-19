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
    public Node above;

    Node (String cartao, int saldo, Node n, Node p) {
        this.cartao = cartao;
        this.saldo = saldo;
        next = n;
        previous = p;
        below = null;
        above = null;
    }

    // Comparadores. Unico critério é a String cartao. Restantes campos são ignorados
    int compareTo(String cartao) {
        return this.cartao.compareTo(cartao);
    }

    int compareTo(Node otherNode) {
        return this.compareTo(otherNode.cartao);
    }
}

class ReturnObj{
    Node newNode;
    Node lastAccessedNode;

    public ReturnObj(){
        newNode= null;
        lastAccessedNode=null;
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

    public ReturnObj add(String cartao, int saldo, Node below){
        Node prox = root;
        Node temp=null;
        ReturnObj toReturn = new ReturnObj();

        if(isEmpty){
            root.next = new Node(cartao, saldo, null, root);
            isEmpty = false;
            toReturn.lastAccessedNode = root.next;
            toReturn.newNode = root.next;
            return toReturn;
        }
        else{
            //Checks if below node has an above node
            if(below!=null){
                if(below.previous.above == null){
                    //Nao tem, procura o mais perto que tenha
                    for(current = below.previous; current!=null; current = current.previous){
                        //System.out.print("Current below is " + (below==null?null:below.cartao));
                        //System.out.println(" and its above is " + (below==null?null:(below.above==null?null:below.above.cartao)));
                        if(current.above!=null){
                            temp = current;
                            break;
                        }
                    }
                }
            }
            for(current = below==null?root:(temp==null?below.previous.above:temp.above); prox!=null; current = prox){
                //System.out.println(root);
                //System.out.println(current);
                prox = current.next;
                if(current.compareTo(cartao) == 0){
                    current.saldo += saldo;
                    toReturn.lastAccessedNode = current;
                    toReturn.newNode = null;
                    return toReturn;
                }
                else if(prox == null){
                    current.next = new Node(cartao, saldo, null, current);
                    toReturn.lastAccessedNode = current.next;
                    toReturn.newNode = current.next;
                    return toReturn;
                }
                else if(prox.compareTo(cartao) > 0){
                    current.next = new Node(cartao, saldo, prox, current);
                    prox.previous = current.next;
                    toReturn.lastAccessedNode = current.next;
                    toReturn.newNode = current.next;
                    return toReturn;
                }
            }
            return toReturn;
        }
    }

    public Node get(String cartao, Node above){
        Node prox = root;
        //System.out.println("Started at " + (above==null?root.cartao:above.cartao));
        for(current = above==null?root:above.below; prox!=null; current = prox){
            if(current!=null){
                prox = current.next;
                //System.out.println("atual" + current);
                //System.out.println("Proximo" + prox);
                if(current.compareTo(cartao) == 0 || prox == null || prox.compareTo(cartao)>0){
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
        for(int i=0; i<maxLevel-1; i++) levels[i].root.above = levels[i+1].root;
        isEmpty = true;
        randomGen = new Random(1);
    }

    // encontra determinado contribuinte na árvore (null se não encontrado)
    public Node get(String cartao) {
        int i=maxLevel-1;
        Node temp = null;

        while(i >= 0){
            //System.out.println("Entered get in level " + i);
            temp = levels[i].get(cartao, temp);
            //System.out.println(temp);
            i-=1;
            if(temp==null) continue;
            //System.out.println("temp number is " + temp.cartao);
            if(Objects.equals(cartao, temp.cartao)) return temp;
        }

        return null;
    }

    // adiciona novo nó à lista. Se já existe atualiza valor acumulado
    public void add(String cartao, int saldo) {
        Node belowNode = null;
        Node newNode;
        Node oldestNodeWithAbove = null;
        ReturnObj returnObj = null;
        int num;

        //Coin flip
        int numberLevels = 1;
        while(numberLevels < maxLevel){
            num = randomGen.nextInt(100);
            if(num >50) numberLevels+=1;
            else break;
        }

        //System.out.println("generated " + numberLevels + " levels");

        for (int i = 0; i < numberLevels; i++) {
            //System.out.println("Adding in level " + i);
            //System.out.print("Below node is " + (belowNode==null?belowNode:belowNode.cartao) + "and its previous is " + (belowNode==null?belowNode:belowNode.previous.cartao));
            //System.out.println(" and its above is " + (belowNode==null?belowNode:(belowNode.previous.above==null?"null":belowNode.previous.above.cartao)));
            returnObj = levels[i].add(cartao, saldo, belowNode);
            //System.out.println("new node number is " + newNode.cartao);
            if (returnObj.newNode != null){
                if(belowNode == null) belowNode = returnObj.newNode;
                else{
                    returnObj.newNode.below = belowNode;
                    belowNode.above = returnObj.newNode;
                    belowNode = returnObj.newNode;
                }
            }
            else{
                //Significa que esta a atualizar
                Node temp;
                /*System.out.print("Prestes a atualizar, returnObj.lastACcessedNode = ");
                System.out.print(returnObj.lastAccessedNode + " , and its value ");
                System.out.print(returnObj.lastAccessedNode.cartao + " , and its above ");
                System.out.println(returnObj.lastAccessedNode.above);*/
                for(temp = returnObj.lastAccessedNode.above; temp!=null; temp = temp.above){
                    //System.out.printf("Atualizou um preço");
                    temp.saldo = returnObj.lastAccessedNode.saldo;
                }
                break;
            }
        }
    }

    public void remove(String cartao){
        Node temp = null;
        Node toRemove = null;
        for(int i=maxLevel-1; i>=0; i--){
            temp = levels[i].get(cartao, temp);
            if(temp==null) continue;
            if(toRemove != null){
                temp.above = null;
                toRemove.below = null;
            }
            if(Objects.equals(cartao, temp.cartao)){
                temp.previous.next = temp.next;
                if(temp.next != null) temp.next.previous = temp.previous;
                toRemove = temp;
                temp = null;
            }
        }
    }

    // imprime em ordem. para debugging e verificacao
    void printInOrder(){
        Node current;
        for(current = levels[0].root.next; current!=null; current = current.next){
            System.out.println(current.cartao + " SALDO " + current.saldo);
        }
        /*int i=maxLevel-1;
        while(i>=0){
            System.out.println(levels[i]);
            i-=1;
        }*/
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
