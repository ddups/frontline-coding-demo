package com.frontline.demo.domain;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Record.java
 * 
 * Represents a (set of) string(s) grouped by a set of parentheses in the input
 * string passed to the web service. A Record's children are the values
 * contained within the parentheses, and its name is the value directly
 * preceding the parentheses. A top-level Record will have the name "". Because
 * the children are stored in a TreeMap and maintained in natural order, the
 * record can return its hierarchy - a mapping of the record and its
 * children/sub-record - which is utilized by the InputConversion class to
 * produce the converted input in alphabetical order.
 * 
 * @formatter:off
 * Example: inputString = "(id,employee(firstname,lastname))" 
 *   top-level Record = {name = ""; children = [employee, id]; subRecord = employeeRecord}
 *     employeeRecord = {name = "employee"; children = [firstname, lastname]; subRecord = null}
 * @formatter:on
 * 
 * @author Derek Dupuis
 *
 */
public class Record {
    private String name;
    private TreeSet<String> children = null;
    private Record subRecord = null;
    private boolean hasSubRecord = false;

    public Record(String parent) {
        this.name = parent;
        this.children = new TreeSet<>();
    }

    public TreeSet<String> getChildren() {
        return children;
    }

    public void setChildren(TreeSet<String> strings) {
        this.children = strings;
    }

    public void addChild(String string) {
        children.add(string);
    }

    public Record getSubRecord() {
        return subRecord;
    }

    public void setSubRecord(Record subRecord) {
        this.subRecord = subRecord;
        this.hasSubRecord = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns a list of strings representing the mapping of this
     * record and its children/sub-record. It first adds its own name, then the
     * name of its children, including its sub-record, if it has one, which is
     * then written completely before continuing to the next child.
     * 
     * @param level
     * @return
     */
    public ArrayList<String> getHierarchy(int level) {
        ArrayList<String> hierarchy = new ArrayList<>();
        StringBuilder bldr = new StringBuilder();

        // determine number of dashes to prepend based on level
        for (int i = level; i > 0; i--) {
            bldr.append('-');
        }
        String dashes = bldr.toString();

        // if the Record has a subRecord get its name and add it to the children
        String subRecordName = "";
        if (hasSubRecord) {
            subRecordName = subRecord.getName();
            children.add(subRecordName);
        }

        // iterate over children and if the child name is equal to subRecordName
        // call its getHierarchy() method with the next level of dashes
        for (String childName : children) {
            // create string with dashes prepended and add to hierarchy list
            bldr = new StringBuilder(dashes + " " + childName);
            hierarchy.add(bldr.toString());

            // if there is a subRecord, get its hierarchy before adding the
            // next child
            if (hasSubRecord) {
                if (childName.equals(subRecordName)) {
                    hierarchy.addAll(subRecord.getHierarchy(level + 1));
                }
            }
        }

        return hierarchy;
    }

    /**
     * Overridden so that Records are compared based on their hierarchy lists
     */
    @Override
    public boolean equals(Object o) {
        Record other = (Record) o;
        return this.getHierarchy(0).equals(other.getHierarchy(0));
    }
}
