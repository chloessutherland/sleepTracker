import java.io.Serializable;

public class LinkedList<T> implements Serializable {

	private Node first;
	private Node last;
	private int size;

	public LinkedList() {
		first = null;
		last = null;
		size = 0;
	}

// Adding
	public void addToFront(T data) {
		Node newNode = new Node(data);
		// If the list isn't empty
		if(getSize() != 0) {
			Node currentFirst = getFirst();
			// New node next (currently null) = first
			newNode.setNext(currentFirst);
			// First previous (currently null) = new node, which is our new first
			currentFirst.setPrevious(newNode);
			// If the list is empty
		} else {
			// Set last (currently null) to the new node since it's also the back
			setLast(newNode);
		}
		// Regardless if first is null or previous, set first to new node
		setFirst(newNode);
		setSize(getSize() + 1);
	}

	public void addToBack(T data) {
		// If the list is empty, add to the front
		if(getSize() == 0) {
			addToFront(data);
			return;
		}
		// If the list isn't empty
		Node newNode = new Node(data);
		Node currentLast = getLast();
		// Last next (currently null) = new node, which is our new last
		currentLast.setNext(newNode);
		// New node previous (currently null) = old last
		newNode.setPrevious(currentLast);
		setLast(newNode);
		setSize(getSize() + 1);
	}

	public void add(T data, int index) {
		// If the index is the first index, add to the front
		if(index == 0) {
			addToFront(data);
			return;
			// If the index is the last index, add to the back
		} else if(index == getSize() - 1) {
			addToBack(data);
			return;
		}
		Node newNode = new Node(data);
		Node current = null;
		// If the index is closer to the front
		if(startFromFrontOrBack(index) == getFirst()) {
			current = getFirst();
			// Traverse list until we find the node AFTER where we need to insert
			for(int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			// If the index is closer to the back
		} else {
			current = getLast();
			/* Traverse list backwards until we find the node BEFORE where we
			need to insert */
			for(int i = getSize(); i > index; i--) {
				current = current.getPrevious();
			}
		}
		Node next = current.getNext();
		// New node next (currently null) = current position next
		newNode.setNext(next);
		// New node previous (currently null) = current position
		newNode.setPrevious(current);
		/* If there is a next node, which there should be or we'd be adding to
		back */
		if(next != null) {
			// Next node previous (currently "current") = new node
			next.setPrevious(newNode);
		}
		// Current next (currently "next") = new node
		current.setNext(newNode);
		setSize(getSize() + 1);
	}

// Removing
	public void removeFromFront() {
		// If the list is empty, nothing to delete
		if(getSize() == 0) {
			return;
		}
		Node currentFirst = getFirst();
		// New first may be null if we're deleting the last node
		Node newFirst = currentFirst.getNext();
		// First = next, may be null
		setFirst(newFirst);
		/* If there is a previous, which there shouldn't be since we're
		deleting the front? */
		if(currentFirst.getPrevious() != null) {
			// Remove the connection to previous
			currentFirst.setPrevious(null);
		}
		// If we're deleting the last node
		if(newFirst == null) {
			setLast(null);
			// If there's only one node
		} else if(newFirst.getNext() == null) {
			setLast(newFirst);
		}
		setSize(getSize() - 1);
	}

	public void removeFromBack() {
		// If the list is empty, nothing to delete
		if(getSize() == 0) {
			return;
			// If the list has 1 node (being both front and back), remove from front
		} else if(getSize() == 1) {
			removeFromFront();
		}
		Node currentLast = getLast();
		// New last = second-last
		Node newLast = currentLast.getPrevious();
		// New last next = null
		newLast.setNext(null);
		setLast(newLast);
		setSize(getSize() - 1);
	}

	public void remove(T objectToRemove) {
		// If the list is empty, nothing to delete
		if(getSize() == 0) {
			return;
		}
		int index = indexOf(objectToRemove);
		// If the index is the front, remove from front
		if(index == 0) {
			removeFromFront();
			return;
		}
		// If the index is the back, remove from back
		if(index == getSize() - 1) {
			removeFromBack();
			return;
		}
		Node nodeToRemove = null;
		// If index is closer to the front
		if(startFromFrontOrBack(index) == getFirst()) {
			nodeToRemove = getFirst();
			// Traverse the list until we get the node to be deleted
			for(int i = 0; i < index; i++) {
				nodeToRemove = nodeToRemove.getNext();
			}
			// If index is closer to the back
		} else {
			nodeToRemove = getLast();
			// Traverse the list backwards until we get the node to be deleted
			for(int i = getSize(); i > index + 1; i--) {
				nodeToRemove = nodeToRemove.getPrevious();
			}
		}
		Node previous = nodeToRemove.getPrevious();
		Node next = nodeToRemove.getNext();
		/* Previous node next = next of node to be removed, skips over the node
		to remove */
		previous.setNext(next);
		/* If there is a previous to next, which there always should be because
		that's the node to remove? */
		if(next.getPrevious() != null) {
			/* Next node previous = previous of node to be removed, skips over
			the node to remove */
			next.setPrevious(previous);
		}
		setSize(getSize() - 1);
	}

// Moving
	// To move, simply delete the node and re-add it to the desired position
	public void move(T data, int index) {
		// If the node is already at the desired position
		if(indexOf(data) == index) {
			return;
		}
		remove(data);
		add(data, index);
	}

	private Node startFromFrontOrBack(int desiredIndex) {
		/* If the desired index is closer to 0 (first) or the size of the index
		(last) */
		if(Math.abs(desiredIndex) < Math.abs(desiredIndex - getSize())) {
			return getFirst();
		} else {
			return getLast();
		}
	}

// Searching
	/* Traverse through the list until you find the object and return the index
	found */
	public int indexOf(T objectToFind) {
		Node current = getFirst();
		for(int i = 0; i < getSize(); i++) {
			if(current.getData() == objectToFind) {
				return i;
			}
			current = current.getNext();
		}
		// If the object isn't found, return -1
		return -1;
	}

	// Traverse through the list until you find the object and return the node
	public Node nodeOfObject(T objectToFind) {
		Node current = getFirst();
		for(int i = 0; i < getSize(); i++) {
			if(current.getData() == objectToFind) {
				return current;
			}
			current = current.getNext();
		}
		// If the object isn't found, return null
		return null;
	}

	// Traverse through the list until you get to the index and return the node
	public Node nodeOfIndex(int desiredIndex) {
		Node currentNode = getFirst();
		for(int currentIndex = 0; currentIndex < getSize(); currentIndex++) {
			if(currentIndex == desiredIndex) {
				return currentNode;
			}
			currentNode = currentNode.getNext();
		}
		return null;
	}

	public Node getFirst() {
		return first;
	}

	private void setFirst(Node first) {
		this.first = first;
	}

	public Node getLast() {
		return last;
	}

	private void setLast(Node last) {
		this.last = last;
	}

	public int getSize() {
		return size;
	}

	private void setSize(int size) {
		this.size = size;
	}

	public class Node implements Serializable {

		private final T data;
		private Node previous;
		private Node next;

		public Node(T data) {
			this.data = data;
			previous = null;
			next = null;
		}

		public T getData() {
			return data;
		}

		public Node getPrevious() {
			return previous;
		}

		protected void setPrevious(Node previous) {
			this.previous = previous;
		}

		public Node getNext() {
			return next;
		}

		protected void setNext(Node next) {
			this.next = next;
		}
	}
}