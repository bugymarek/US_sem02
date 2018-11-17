/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashing;

/**
 *
 * @author Bugy
 */
public class InternalNode extends Node{
    private Node leftNode;
    private Node rightNode;
    
    public InternalNode(Node father, int depth) {
        super(father, depth);
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }    
}
