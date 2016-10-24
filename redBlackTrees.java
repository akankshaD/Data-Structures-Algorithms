class Node{

	// Red Black Tree Visualisation Link : https://www.cs.usfca.edu/~galles/visualization/RedBlack.html

	public Node()
	{
		this.left = null;
		this.right = null;
		this.parent = null;
	}

	int key;
	Node left;
	Node right;
	Node parent;        // parent
	int color;     // 0 for RED, 1 for BLACK
}


public class redBlackTrees{

	Node root = new Node();
	
	public redBlackTrees()
	{
		
		this.root = null;
	}

//**************************************************** Rotations ***************************************************************

// LEFT ROTATION

void leftRotate(redBlackTrees t, Node x)
{
	Node y;
	y = x.right;                    // set y
	x.right = y.left;               // right of x would point to left subtree of y
	if(y.left != null)
		y.left.parent = x;
	y.parent = x.parent ;                // link x's parent to y
	if(x.parent == null)
		t.root = y;
	else if(x == x.parent.left)
		x.parent.left = y;
	else
		x.parent.right = y;

	y.left = x;                             // put x on y's left
	x.parent = y;

}

// RIGHT ROTATION

void rightRotate(redBlackTrees t, Node y)
{
	Node x;
	x = y.left;
	y.left = x.right;
	if(x.right != null)
		x.right.parent = y;
	x.parent = y.parent;
	if(y.parent == null)
		t.root = x;
	else if(y == y.parent.left)
		y.parent.left = x;
	else
		y.parent.right = x;

	x.right = y;
	y.parent = x;

}


// *************************************************************** INSERTION *********************************************************

void insert(redBlackTrees t, Node z)
{
	Node y = null;
	Node x = t.root;
	while(x != null)
	{
		y = x ;
		if(z.key > x.key)
			x = x.right;
		else
			x = x.left;
	}

	z.parent = y ;

	if(y == null)
		t.root = z;
	else if( z.key > y.key )
		y.right = z;
	else
		y.left = z;

	z.left = null;
	z.right = null;
	z.color =  0;     //  coloring newly inserted node red (safe color)
	solveDoubleRed(t, z);      // calling the insert fixup method to balance the red black tree
}

void solveDoubleRed(redBlackTrees t, Node z)
{

	Node y;

	while((z.parent != null)&&(z.parent.color == 0))  // double red problem
	{
		if(z.parent == z.parent.parent.left)                // z's parent is in left subtree
		{
			y = z.parent.parent.right;        // uncle of z and is right child of z's grandparent

			if((y != null) && (y.color == 0))           // CASE 1: Uncle is red
			{
				z.parent.color = 1;        // z's parent black
				y.color = 1 ;       // setting uncle's color black
				z.parent.parent.color = 0;    // setting grandparent's color black
				z = z.parent.parent;
			} 
			else         // CASE 2: Uncle is black and z is right child of it's parent
			{
				if(z == z.parent.right) 
				{
					z =  z.parent;
					leftRotate(t, z);
				}
			
				if(z.parent != null)
				{
					z.parent.color = 1;          // coloring the parent black         // CASE 3: Uncle is black and z is left child of it's parent
					if(z.parent.parent != null)
					{      
						z.parent.parent.color = 0;    // coloring the grandparent red
					}
				}
				if(z.parent != null && z.parent.parent != null)
				{
					rightRotate(t, z.parent.parent);
				}  
			}    
			
		}

		else    // SYMMETRIC CASE :      z.parent = z.parent.parent.right
		{

			y = z.parent.parent.left;

			if( (y!=null) && (y.color == 0))            // CASE 1: Uncle is red 
			{
				z.parent.color = 1;
				y.color = 1;
				z.parent.parent.color = 0;
				z = z.parent.parent;
			}		
			else 		// CASE 2: Uncle is black and z is left child of it's parent
			{
				if(z == z.parent.left)	
				{
					z = z.parent;
					rightRotate(t, z);
				}
			
				if( z.parent != null)
				{
					z.parent.color = 1;                // CASE 3: Uncle is black and z is right child of it's parent
					if(z.parent.parent != null)
					{             
						z.parent.parent.color = 0;
					}
				}
				if((z.parent != null) && (z.parent.parent != null))
				{
					leftRotate(t, z.parent.parent);
				}
			}
			
		}		
	}

	t.root.color = 1;   // Coloring the root black
}

// **********************************************************************************************************************************

// **************************************************** DELETION ********************************************************************

// Replaces the subtree rooted at u by subtree rooted at v

void replaceSubTree(redBlackTrees t, Node u, Node v)     
{
	if(u.parent == null)
		t.root = v ;
	else if(u == u.parent.left)
		u.parent.left = v;
	else 
		u.parent.right = v;

	if(v != null)
		v.parent = u.parent;
}

Node findMinimum(redBlackTrees t, Node x)
{
	while( x.left != null)
	{
		x = x.left ;
	}
	return x;

}

Node findMaximum(redBlackTrees t, Node x)
{
	while( x.right != null)
	{
		x = x.right ;
	}
	return x;

}


void delete(redBlackTrees t, int key)
{
	// searching the node in the tree]
	Node z = new Node();
	Node temp = t.root;
	while(temp != null)
	{
		if(key == temp.key)
		{
			z = temp;
			break;
		}
		else
		{
			if(key > temp.key)
			{
				temp = temp.right;
			}
			else
			{
				temp = temp.left;
			}
		}
	}

	System.out.println("After Search: z: " + z.key + " z's lc: "+ z.left.key+ " z's rc: " + z.right.key);

	Node y = z ;
	Node x;
	int yOriginalColor = y.color;            // Noting down the original color
	if(z.left == null)                        // with only one child i.e. right child
	{	
		x = z.right;
		replaceSubTree(t, z, z.right);        // replacement with left child
	}
	else if(z.right == null)                  // with only one child i.e. left child
	{
		x = z.left;
		replaceSubTree(t, z, z.left);           // replacement with left child
	}
	else
	{
		//y = findMinimum(t, z.right);            // finding out the inorder successor of z
		y = findMaximum(t, z.left);               // finding out the inorder predecessor of z
		System.out.println("Inorder succ: "+ y.key);
		yOriginalColor = y.color;
		//x = y.right;
		x = y.left;
		if((y.parent == z) && (x != null))
			x.parent = y;
		else
		{
			//replaceSubTree(t, y, y.right);
			replaceSubTree(t, y, y.left);
			/*y.right  = z.right;
			y.right.parent = y;*/
			y.left = z.left;
			y.left.parent = y;
		}

		replaceSubTree(t, z, y);
		/*y.left = z.left;
		y.left.parent = y;*/
		y.right = z.right;
		y.right.parent = y;
		y.color = z.color;
	}

	System.out.println("Inorder : ");
	displayTree(t.root);

	System.out.println("PreOrder: ");
	displaypreOrder(t.root);

	if((yOriginalColor == 1) && (x != null))                      // If color of y was BLACK, delete fixup is called  
	{
		System.out.println("Fixup called! ");
		solveDoubleBlack(t, x);                // DoubleBlack
	}	
}

void solveDoubleBlack(redBlackTrees t, Node x)
{
	Node w ;
	while((t.root != x) && (x.color == 1))
	{
		if(x == x.parent.left)
		{
			w = x.parent.right;     // x's sibling

			if(w.color == 0)            // CASE 1: x's sibling is RED in color
			{
				w.color = 1;        // setting the sibling's color to black
				x.parent.color = 0;    // setting the parent's color to red
				leftRotate(t, x.parent);
				w = x.parent.right;      // setting the new sibling

			}
			if((w.left.color == 1) && (w.right.color == 1))            // CASE 2: x's sibling is BLACK in color and both the children of w are also BLACK
			{
				w.color = 0;          // setting the x's sibling color to RED
				x = x.parent;          // setting new x to its parent
			}
			else                   // CASE 3: x's sibling color is BLACK and right child of sibling is BLACK
			{
				if(w.right.color == 1) 
				{
					w.left.color = 1;            // setting the sibling's left child color to BLACK
					w.color = 0;                // setting the sibling's color to RED
					rightRotate(t, w);           
					w = x.parent.right;            // setting the new sibling to the new sibling of x after right rotation
				}
				
			
				// CASE 4:  When sibling of x is BLACK and right child of sibling is RED
				w.color = x.parent.color;             // swapping the colors between x's sibling and x's parent
				w.right.color = 1;                 // setting the right child of x's sibling to BLACK
				x.parent.color = 1;            // setting the color of x's parent to BLACK
				leftRotate(t, w);
				x = t.root;                   // setting x to ROOT and terminating the while loop
			}
		}
		else       // SYMMETRIC CASE where x's sibling is the left child of its parent
		{
			w = x.parent.left;
			if(w.color == 0)             // CASE 1: sibling's color is RED
			{
				w.color = 1;
				x.parent.color = 0;
				rightRotate(t, x.parent);
				w = x.parent.left;   
			}
			if((w.right.color == 1) && (w.left.color == 1))    // CASE 2: x's xibling is BLACK and both children of x are BLACK
			{
				w.color = 0;
				x = x.parent;
			}
			else 
			{
				if(w.left.color == 1)         // CASE 3: x's sibling is BLACK and left child of x's sibling is BLACK
				{
					w.right.color = 1;
					w.color = 0;
					leftRotate(t, w);
					w = x.parent.left;
				}

				// CASE 4: x's sibling is black and left child of x's sibling is RED

				w.color = x.parent.color;
				w.left.color = 1;
				x.parent.color = 1;
				rightRotate(t, w);
				x = t.root;
			}

		
		}
	}

	x.color = 1;       // setting the root's color BLACK

}

// **********************************************************************************************************************************

//    DISPLAY THE TREE

void displayTree(Node root)
{
	//System.out.println("key= "+root.key+" color="+root.color);
	//System.out.println("key= "+root.left.key+" color="+root.left.color);
	if(root != null)
	{
		displayTree(root.left);
		System.out.println(root.key + " C: " + root.color + " ");
		displayTree(root.right);
	}
}

void displaypreOrder(Node root)
{
	if(root != null)
	{
		System.out.println(root.key);
		displaypreOrder(root.left);
		displaypreOrder(root.right);
	}
}


// ************************************************************** MAIN METHOD ********************************************************

	public static void main(String[] args)
	{

		redBlackTrees rb = new redBlackTrees();

		// Dataset for INSERTION

		Node x1 = new Node();
		x1.key  = 26;
		System.out.println("Inserting 26 ");
		rb.insert(rb, x1);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x2 = new Node();
		x2.key = 3;
		System.out.println("Inserting 3 ");
		rb.insert(rb, x2);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x3 = new Node();
		x3.key = 7;
		System.out.println("Inserting 7 ");
		rb.insert(rb, x3);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x4 = new Node();
		x4.key = 11;
		System.out.println("Inserting 11 ");
		rb.insert(rb, x4);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x5 = new Node();
		x5.key = 10;
		System.out.println("Inserting 10 ");
		rb.insert(rb, x5);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x6 = new Node();
		x6.key = 22;
		System.out.println("Inserting 22 ");
		rb.insert(rb, x6);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x7 = new Node();
		x7.key = 23;
		System.out.println("Inserting 23 ");
		rb.insert(rb, x7);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x8 = new Node();
		x8.key = 8;
		System.out.println("Inserting 8 ");
		rb.insert(rb, x8);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x9 = new Node();
		x9.key = 1;
		System.out.println("Inserting 1 ");
		rb.insert(rb, x9);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x10 = new Node();
		x10.key = 2;
		System.out.println("Inserting 2 ");
		rb.insert(rb, x10);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x11 = new Node();
		x11.key = 54;
		System.out.println("Inserting 54 ");
		rb.insert(rb, x11);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x12 = new Node();
		x12.key = 44;
		System.out.println("Inserting 44 ");
		rb.insert(rb, x12);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x13 = new Node();
		x13.key = 9;
		System.out.println("Inserting 9 ");
		rb.insert(rb, x13);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x14 = new Node();
		x14.key = 6;
		System.out.println("Inserting 6 ");
		rb.insert(rb, x14);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x15 = new Node();
		x15.key = 25;
		System.out.println("Inserting 25 ");
		rb.insert(rb, x15);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x16 = new Node();
		x16.key = 88;
		System.out.println("Inserting 88 ");
		rb.insert(rb, x16);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x17 = new Node();
		x17.key = 30;
		System.out.println("Inserting 30 ");
		rb.insert(rb, x17);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x18 = new Node();
		x18.key = 76;
		System.out.println("Inserting 76 ");
		rb.insert(rb, x18);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x19 = new Node();
		x19.key = 100;
		System.out.println("Inserting 100 ");
		rb.insert(rb, x19);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		Node x20 = new Node();
		x20.key = 0;
		System.out.println("Inserting 0 ");
		rb.insert(rb, x20);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		//System.out.println("key= "+rb.root.key+" color="+rb.root.color);

		/*System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);


		// DATASET for DELETION
	
		/*Node x1 = new Node();
		x1.key  = 50;
		System.out.println("Inserting 50 ");
		rb.insert(rb, x1);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x2 = new Node();
		x2.key = 25;
		System.out.println("Inserting 25 ");
		rb.insert(rb, x2);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x3 = new Node();
		x3.key = 75;
		System.out.println("Inserting 75 ");
		rb.insert(rb, x3);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x4 = new Node();
		x4.key = 48;
		System.out.println("Inserting 48 ");
		rb.insert(rb, x4);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x5 = new Node();
		x5.key = 80;
		System.out.println("Inserting 80 ");
		rb.insert(rb, x5);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x6 = new Node();
		x6.key = 27;
		System.out.println("Inserting 27 ");
		rb.insert(rb, x6);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		Node x7 = new Node();
		x7.key = 28;
		System.out.println("Inserting 28 ");
		rb.insert(rb, x7);
		System.out.println("Inorder: ");
		rb.displayTree(rb.root);
	System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);*/

		rb.delete(rb, 11);

		System.out.println("After deleting 11 ");

		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);

		rb.delete(rb, 44);

		System.out.println("After deleting 44 ");

		System.out.println("Inorder: ");
		rb.displayTree(rb.root);

		System.out.println("PreOrder: ");
		rb.displaypreOrder(rb.root);
		

	
	}



}

