package dynamicHashingCore;

import dynamicHashingCore.nodes.ExternalNode;
import dynamicHashingCore.nodes.InternalNode;
import dynamicHashingCore.nodes.Node;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Bugy
 */
public class Storage {

    private static String fileType = ".txt";

    /**
     * Saves data from ArrayList to specified file
     *
     * @param arrayList
     * @param name
     * @param <T>
     * @return
     */
    public static <T> boolean saveArray(ArrayList<T> arrayList, String path, String name) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path + name + fileType);
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }

        for (T e : arrayList) {
            writer.append(((ISavable) e).save());
            writer.append(System.lineSeparator());
        }
        writer.close();
        return true;
    }

    static boolean loadTrie(DynamicHashing dh, String path, String name) {
        boolean result = true;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(path + name + fileType));
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        int depth = 0;
        Node root = null;
        LinkedList<Node> nodesLevel = new LinkedList<>();
        if (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");
            String nodeString = line[0];
            String[] params = nodeString.split("|");
            if (params[0].equals("E")) {
                Node extNode = new ExternalNode(null, depth);
                ((ExternalNode) extNode).setValidRecordsCount(Integer.parseInt(params[1]));
                ((ExternalNode) extNode).setAddressBlock(Integer.parseInt(params[2]));
                root = extNode;
            } else {
                root = new InternalNode(null, depth);
            }
            nodesLevel.add(root);
        }

        while (sc.hasNextLine()) {
            depth++;
            String[] line = sc.nextLine().split(";");
            LinkedList<Node> nextLevelNodes = new LinkedList<>();
            
            for (int i = 0; i < line.length; i++) {
                String nodeString = line[i];
                String[] params = nodeString.split("|");
                if (params[0].equals("E")) {
                    Node extNode = new ExternalNode(null, depth);
                    ((ExternalNode) extNode).setValidRecordsCount(Integer.parseInt(params[1]));
                    ((ExternalNode) extNode).setAddressBlock(Integer.parseInt(params[2]));
                    nextLevelNodes.add(extNode);
                } else {
                    nextLevelNodes.add(new InternalNode(null, depth));
                }
            }
            
            while (!nodesLevel.isEmpty()) {
                Node node = nodesLevel.poll();
                
                Node nodeLeft = nextLevelNodes.peek();
                Node nodeRight = nextLevelNodes.peek();
                
                ((InternalNode) node).setLeftNode(nodeLeft);
                ((InternalNode) node).setRightNode(nodeRight);
                
                nodeLeft.setFather(node);
                nodeRight.setFather(node);
            }
            
            for (int i = 0; i < nextLevelNodes.size(); i++) {
                Node nodeNext = nextLevelNodes.poll();
                if(nodeNext instanceof InternalNode){
                    nodesLevel.add(nodeNext);
                }
            }
        }
        sc.close();

        dh.setRoot(root);
        return result;
    }
}
