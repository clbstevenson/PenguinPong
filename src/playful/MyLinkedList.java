package playful;

public class MyLinkedList<D> {

	private class Node {
		D data; // D for data!
		Node prev;
		Node next;
	}

	Node head, tail;
	Node current;
	private int size;

	public MyLinkedList() {
		head = tail = null;
		current = null;
		size = 0;
	}

	public D getCurrentData() {
		if (current != null)
			return current.data;
		else
			return null;
	}

	public D getDataAt(int id) {

		Node nd = getNodeAt(1, id);
		if (nd != null)
			return nd.data;
		else
			return null;
	}

	private Node getNodeAt(int start, int id) {
		Node n = head;

		if (n == null)
			return null;
		// if start point is too small (was mainly for extract, but now fixed)
		if (start < 1)
			throw new IllegalArgumentException(
					"Too small of a start point to get Node at");
		// if id is less than 1, ID is too small to give a Node
		if (id < 1)
			throw new IllegalArgumentException("Too small of an ID");
		// if id is greater than the size of the list, throw exception
		if (id > size)
			throw new IllegalArgumentException("ID is bigger than list size");

		int i = 1;
		while (i < id && n != tail) {
			n = n.next;
			i++;
		}

		// // if it gets to the end of the list and haven't reached ID, too
		// large
		// if (n == tail)
		// throw new IndexOutOfBoundsException("Too large of an ID");

		return n;
	}

	private void setCurrentNode(Node n) {
		current = n;
	}

	public int getCurrentPos() {
		if (head == null)
			return 0;
		int pos = 1;
		Node n = current;
		while (n != head) {
			n = n.prev;
			pos++;
		}
		return pos;
	}

	public int getSize() {
		return size;
	}

	public void addFirst(D newVal) {

		// make a new node with data of newVal
		Node n = new Node();
		n.data = newVal;
		// make n's next the current first Node
		n.next = head;
		n.prev = null; // because it is the new head
		if (head != null) {
			// make current head.prev equal to n
			head.prev = n;
			// now set head equal to new Node
			head = n;
		} else {
			// otherwise, this is the 1st node in the list
			head = n;
			// just double check tail & current are null (should be)
			if (tail == null)
				tail = n;
			if (current == null)
				current = n;
		}

		size++;

		// WRONG.. //simply use addAfter using the first Node
		// addAfter(newVal, head);
	}

	public void addLast(D newVal) {

		// if the list is empty, tail will be null
		// but this is handled in the addAfter method
		// if tail is not null, then add after tail (last element)
		addAfter(newVal, tail);
		// set current to the new tail;
		current = tail;
	}

	public void addAfterCurrent(D newVal) {
		if (current != null) {
			addAfter(newVal, current);
			// set the current Node to the newly added Node
			current = current.next;
		} else {
			// current is null, so this is the first Node
			addAfter(newVal, null);
			// addFirst(newVal);
		}
	}

	public void addBeforeCurrent(D newVal) {
		if (current != null) {
			Node n = new Node();
			n.data = newVal;
			// if current is head,
			if (current == head) {
				// make new Node the head and connect to current
				head = n;
				n.prev = null;
				n.next = current;
				current.prev = n;
				size++;
			} else {
				// if current is not head, then there is a Node before
				// where to add the new Node after
				addAfter(newVal, current.prev);
			}
		} else {
			// this is the first item since current is null
			addAfter(newVal, null);
		}
	}

	public void addListBeforeCurrent(MyLinkedList<D> newItems) {
		// should be able to use for current Node

		if (current != null) {
			// update the new list's head and tail into this list
			newItems.head.prev = current.prev;
			newItems.tail.next = current;

			// if the current is NOT head (so current.prev is NOT null)
			if (current != head)
				current.prev.next = newItems.head;
			else
				// current.prev is null (because it's the head)
				// so make this head newItems head
				head = newItems.head;
			// update this list to add the new list
			current.prev = newItems.tail;
		} else {
			// if there are not already others in the list,
			// make this head the newItems head
			head = newItems.head;
			// make this tail the newItemss tail
			tail = newItems.tail;
			current = tail;
		}

		size += newItems.size;
	}

	private void addAfter(D newVal, Node pos) {
		Node n = new Node();
		n.data = newVal;
		// if pos is not null, then there should already be a node in the list
		if (pos != null) {
			n.prev = pos;
			n.next = pos.next;

			if (pos != tail) {
				pos.next.prev = n;
				pos.next = n;
			} else {
				pos.next = n;
				tail = n;
			}

		} else {
			// otherwise, the new Node n is the FIRST node added
			if (head == null) {
				head = n;
				tail = n;
			}
			if (current == null)
				current = n;
			n.prev = null;
			n.next = null;
		}

		// added a new item, so increment size
		size++;
	}

	public void deleteFirst() {
		// use deleteNode but use head/first Node
		// deleteNode(head);
		if (current == head)
			current = head.next;
		deleteNodes(1, head);
	}

	public void deleteLast() {
		// use deleteNode but use tail/last Node
		// deleteNode(tail);
		if (current == tail)
			current = tail.prev;
		deleteNodes(1, tail);
	}

	private void deleteNode(Node pos) {
		// can be used to delete current?

		if (pos == null)
			return;

		// Node p = pos.prev;
		// Node n = pos.next;
		if (pos.prev == null) {
			head = pos.next;
		} else {
			pos.prev.next = pos.next;
		}
		if (pos.next == null) {
			if (current == null) {

			}
			tail = pos.prev;
		} else {
			if (current == null) {

			}
			pos.next.prev = pos.prev;
		}

		/*
		 * if (pos == head) {
		 * head = pos.next;
		 * // pos.next.prev = null;
		 * } else {
		 * pos.prev.next = pos.next;
		 * }
		 * if (pos == tail) {
		 * tail = pos.prev;
		 * pos.prev.next = null;
		 * } else {
		 * // pos.prev
		 * }
		 */

		// pos.next.prev = pos.prev;

		// removed an item so decrease size;
		size--;
	}

	/**
	 * Checks if the given key value is within the MyLinkedList
	 * @param key the value to check for
	 * @return true if the key is in the list
	 */
	public boolean contains(D key) {

		// if the list is empty, auto-return false
		if (head == null)
			return false;

		// if only 1 element, check it individually
		if (head == tail) {
			if (head.data.equals(key))
				return true;
			else
				return false;
		}

		// if there are more than 1 node, start with head and move on from there
		Node curr = head.next;
		do {
			if (curr.data.equals(key))
				return true;
			curr = curr.next;
		} while (curr != tail);
		// if it goes through whole loop, but no match, return false
		if (curr == tail && !curr.data.equals(key)) {
			return false;
		} else {
			// there was a match, so return true (key is in the list)
			return true;
		}

	}

	public void moveCurrentPos(int delta) {

		if (current == null)
			return;

		Node setNode = current;

		if (delta < 0) { // if it's negative, move node backwards (up)
			// while there is still more distance backwards to go
			for (int i = delta; i < 0; i++) {
				if (setNode.prev == null)
					break;
				// move setNode 1 prev
				setNode = setNode.prev;
			}
		} else { // if it's positive, move node forwards (down)
			// while there is still more distance forwards to go
			for (int i = delta; i > 0; i--) {
				if (setNode.next == null)
					break;
				// move setNode 1 next
				setNode = setNode.next;
			}
		}

		setCurrentNode(setNode);

	}

	/**
	 * Deletes a number of nodes N starting at current position
	 * @param N number of lines to remove
	 */
	public void deleteNodes(int N) {

		// get the new end point node (could be null)
		Node newCurrent = deleteNodes(N, current);
		current = newCurrent;

	}

	/**
	 * Deletes a number of nodes N starting at Node pos
	 * @param N number of lines to delete
	 * @param pos starting Node to delete (deletes pos as well)
	 * @return end Node to set current Node to (mainly for setting current)
	 */
	private Node deleteNodes(int N, Node pos) {

		// make sure there is actually a Node to delete
		if (pos == null)
			return null;

		// holds the node prior to pos (startNode is SAVED (not deleted)
		Node startNode = pos.prev;
		// holds current node to be deleted
		Node endNode = pos;

		// if N is negative, throw IllegalArgException
		if (N <= 0) {
			throw new IllegalArgumentException(
					"Number of nodes to delete should be positive");
		}

		// assume positive N? or throw exception?
		for (int i = N; i > 0; i--) {
			size--;
			if (endNode.next == null) {
				endNode = null;
				break;
			}

			endNode = endNode.next;
			// if (endNode.next == tail) {
			// tail = endNode;
			// }

			// ? deleteNode(endNode);
		}
		// reconnect the new endNode to startNode
		if (startNode != null) {
			startNode.next = endNode;
		} else {
			head = endNode;
		}

		if (endNode != null) {
			endNode.prev = startNode;
			// if endNode is not null, return current to be the new endNode
			return endNode;
		} else {
			tail = startNode;
			// if endNode IS null, set current to starting node (could be null)
			return startNode;
		}
	}

	/**
	 * Extracts a list of nodes beginning at index start
	 * to index end (inclusive)
	 * @param start beginning index to start extracting
	 * @param end index to stop extracting 
	 * @return MyLinkedList<D> of extracted items
	 */
	public MyLinkedList<D> extract(int start, int end) {

		MyLinkedList<D> sublist = new MyLinkedList<D>();
		// int diff = end - start;
		Node startNode = null;
		Node endNode = null;

		// if start is less than 1, fix it so it's within range
		int nStart = 0;// (start < 1) ? 1 : start;
		int nEnd = 0;
		// make sure start is within the range of the list
		if (start < 1)
			nStart = 1;
		else if (start > size)
			nStart = size;
		else
			nStart = start;
		// make sure end is within the range of the list
		if (end < 1)
			nEnd = 1;
		else if (end > size)
			nEnd = size;
		else
			nEnd = end;
		int ndiff = nEnd - nStart + 1;
		// if start and end are the same, still need to delete 1 node

		// if end is greater than start, throw Exception or fix?
		// if (diff < 0) {
		// throw new
		// IllegalArgumentException("End id to cut to must be"
		// + " greater than the Start id");
		// OR
		// end = start;
		// }

		// start at index of start, go to (INCLUDING) end index
		for (int i = nStart; i <= nEnd; i++) {
			Node n = getNodeAt(1, i);
			if (i == nStart) {
				// get the starting Node using private method when i = start
				startNode = n;
			} else if (i == nEnd) {
				// get the ending Node using private method when i = end
				endNode = n;
			}
			sublist.addLast(n.data);
			// deleteNode(n);
		}

		// startNode = getNodeAt(start);
		// endNode = getNodeAt(end);

		// delete all nodes from start to end (including start and end Nodes)
		// deleteNodes() will return the next available Node to set current to
		current = deleteNodes(ndiff, startNode);

		return sublist;
	}
}
