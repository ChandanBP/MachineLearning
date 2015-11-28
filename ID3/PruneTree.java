import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class PruneTree {
    
	Node dPrimeNode;
	Node dBest;
	File validationFile;
	
	public PruneTree(File file) {
		dBest = new Node();
		this.validationFile = file;
	}
	
	@SuppressWarnings("unused")
	public Node prune(int L,int K,Node root) throws CloneNotSupportedException{
		
		int N;
		int M;
		int P;
		int nodes;
		//Node dPrimeNode,dBest;
		
		for (int i = 1; i <=L; i++) {
			
			// Copy D into Dprime
			dPrimeNode = new Node();
			dPrimeNode = (Node)root.clone();
			
			M = getRandomNumber(1, K);
			
			for (int j = 1; j <=M; j++) {
				
				N = getNumNonLeafNodes(dPrimeNode);
				P = getRandomNumber(1, N);
				
				// Replace subtree
				replaceTreeAtP(P);
			}
			
			// Evaluate accuracy
			evaluateAccuracy();
			
			if(dPrimeNode.accuracy>dBest.accuracy){
				dBest = dPrimeNode;
			}
		}
		return dBest;
	}
	
public void replaceTreeAtP(int P){
		
		Queue<Node>queue = new LinkedList<Node>();
	    Node node = dPrimeNode;
	    queue.add(node);
	    
	    while(!queue.isEmpty()){
	    	 
	    	   // Dequeue
	    	   node = queue.poll();
	    	   if(node.N == P){
	    		   break;
	    	   }
	    	   if(node.left!=null){
	    		   queue.add(node.left);
	    	   }
	    	   if(node.right!=null){
	    		   queue.add(node.right);
	    	   }
	       }
	    
	    int classZero,classOne; 
	    
	    if(node!=null){
	    	
	    	classZero = node.zeroPInstances+node.onePInstances;
	    	classOne = node.zeroNInstances+node.oneNInstances;
	    	if(classZero>classOne){
	    		node.classLabel = 0;
	    	}
	    	if(classOne>classZero){
	    		node.classLabel = 1;
	    	}
	    	if(node.left!=null){
	    		node.left = null;
	    	}
	    	if(node.right!=null){
	    		node.right=null;
	    	}
	    }
	}
	
	public int getNumNonLeafNodes(Node dt){
		
	       Queue<Node>queue = new LinkedList<Node>();
	       Node node;
	       int n=1;
	       
	       //Initially add root node to the queue
	       dt.N = n;
	       queue.add(dt);
	       
	       while(!queue.isEmpty()){
	    	 
	    	   // Dequeue
	    	   node = queue.poll();
	    	   if(node.left!=null){
	    		   node.left.N = ++n;
	    		   queue.add(node.left);
	    	   }
	    	   if(node.right!=null){
	    		   node.right.N = ++n;
	    		   queue.add(node.right);
	    	   }
	       }
	       
			return n;
		}
	
	@SuppressWarnings({ "resource", "unused" })
	public void evaluateAccuracy(){
		
		try {

			BufferedReader br = new BufferedReader(new FileReader(validationFile));
			int attrVal,nodeAttr,classLabel,correct,count;
			String line;
			String input[];
			Node node = dPrimeNode;
			Node par = node;
			correct = count = 0;
			line = br.readLine();
			line = br.readLine();
			while(line!=null){
				
				++count;
				input = line.split(",");
				node = dPrimeNode;
				while(node!=null){
					nodeAttr = node.attr;
					attrVal = Integer.parseInt(input[nodeAttr]);
					if(attrVal == 0){
						par = node;
						node = node.left;
					}
					if(attrVal == 1){
						par = node;
						node = node.right;
					}
				}
				
				classLabel = Integer.parseInt(input[input.length-1]);
				if(par.classLabel == classLabel){
					++correct;
				}
				line = br.readLine();
			}
			
			float accuracy = (float)correct/count;
			accuracy = accuracy*100f;
			dPrimeNode.accuracy = accuracy;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
public int getRandomNumber(int n1,int n2){
		
		int r = (int) (Math.random() * (n2 - n1)) + n1;
		return r;
	}
}
