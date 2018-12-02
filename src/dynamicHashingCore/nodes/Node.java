/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore.nodes;

/**
 *
 * @author Bugy
 */
public abstract class Node {
    protected Node father;
    protected int depth;

    public abstract String save(); 

    public Node getFather() {
        return father;
    }

    public int getDepth() {
        return depth;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
