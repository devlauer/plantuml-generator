package de.elnarion.util.plantuml.generator.sequencediagram.internal;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple tree data structure.
 *
 * @param <E> the value type of the tree data structure
 */
public class TreeNode<E,F> {

    private E key;
    private F value;

    private TreeNode<E,F> parent;
    private List<TreeNode<E,F>> childNodes = new LinkedList<>();


    /**
     * Standard constructor for tree nodes.
     *
     * @param paramKey the key of the tree node
     * @param paramValue the value of the tree node
     */
    public TreeNode(E paramKey , F paramValue) {
        this.value = paramValue;
        this.key = paramKey;
    }

    private void setParent(TreeNode<E,F> paramParent) {
        parent = paramParent;
    }

    /**
     * Add a leaf to the tree.
     *
     * @param paramChild the child node of the tree aka leaf
     */
    public void addChildNode(TreeNode<E,F> paramChild) {
        childNodes.add(paramChild);
        paramChild.setParent(this);
    }

    /**
     * Get the value of this tree node
     *
     * @return F the value
     */
    public F getValue() {
        return value;
    }

    /**
     * Get the key of this tree node
     *
     * @return E the key
     */
    public E getKey() {
        return key;
    }

    /**
     * Retrieve all parents of the current tree node/leaf to the root of the tree. If this is the root of the tree the list is empty.
     *
     * @return List the parent tree node objects as list
     */
    public List<TreeNode<E,F>> getParents() {
        List<TreeNode<E,F>> parents = new LinkedList<>();
        if (parent != null) {
            parents.add(parent);
            parents.addAll(parent.getParents());
        }
        return parents;
    }

}
