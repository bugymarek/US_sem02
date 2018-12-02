/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore.nodes;

import dynamicHashingCore.ISavable;

/**
 *
 * @author Bugy
 */
public class Node implements ISavable{
    private Node father;
    private int depth;

    public Node(Node father, int depth) {
        this.father = father;
        this.depth = depth;
    }
    
    public Node getFather() {
        return this.father;
    }
    
    public int getDepth() {
        return this.depth;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String save() {
       return "no define";
    }
    
}
