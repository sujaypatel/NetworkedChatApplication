/**
 * NodeData.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Interface for Node information
 */
public interface NodeData {
	public int getId();
	public String toString();
	public ObjectInputStream getObjectInputStream();
	public ObjectOutputStream getObjectOutputStream();
	public boolean equals(Object anObject);
	public int hashCode();
}
