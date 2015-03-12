/**
 * NodeInfo.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Contains information about a node (client or server) on the network
 */
public class NodeInfo implements NodeData, Serializable {
	private static final long serialVersionUID = -3019147383562137053L;
	private int id;	
	private ObjectOutputStream nodeOutputStream;
	private ObjectInputStream nodeInputStream;

	/**
	 * Constructor for NodeInfo
	 * @param id ID of client or server
	 * @param nodeOutputStream ObjectOutputStream for node
	 * @param nodeInputStream ObjectInputStream for node
	 */
	public NodeInfo(int id, ObjectOutputStream nodeOutputStream, ObjectInputStream nodeInputStream) {
		this.id = id;
		this.nodeOutputStream = nodeOutputStream;
		this.nodeInputStream = nodeInputStream;
	}

	/**
	 * No argument constructor needed for deserialization
	 */
	public NodeInfo() {
		super();
	}
	
	/**
	 * Getter for nodeOutputStream
	 * @return the nodeOutputStream
	 */
	public ObjectOutputStream getObjectOutputStream() {
		return nodeOutputStream;
	}
	
	/**
	 * Getter for nodeInputStream
	 * @return the clientInputStream
	 */
	public ObjectInputStream getObjectInputStream() {
		return nodeInputStream;
	}

	/**
	 * Getter for id
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(ChatUtils.CLIENT_NAME);
		builder.append(id);		
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;
	    if (anObject instanceof NodeData) {
	    	NodeData that = (NodeData) anObject;
	        result = (this.getId() == that.getId());
	    }
	    return result;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override 
	public int hashCode() {
		return id;
    }
	
	/**
	 * Needed for serialization since ObjectInputStream and ObjectOutputStream
	 * are not serializable
	 * 
	 * @return NodeInfo without Input/Output stream
	 * @throws ObjectStreamException
	 */
	private Object writeReplace() throws ObjectStreamException {
		return new NodeInfo(id, null, null);
	}
}
