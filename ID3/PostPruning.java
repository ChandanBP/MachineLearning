import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class PostPruning {

	int L;
	int K;
	DecisionTree dPrime;
	DecisionTree D;
	DecisionTree dBest;
	File validationFile;
	
	public PostPruning(DecisionTree dTree,int L,int K,File validation) throws CloneNotSupportedException{
		
		this.D = new DecisionTree();
		this.D = (DecisionTree)dTree.clone();
		this.D.accuracy = 0;
		
		this.dBest = new DecisionTree();
		this.dBest = (DecisionTree)D.clone();
		this.dBest.accuracy = 0;
		this.L = L;
		this.K = K;
		validationFile = validation;
	}
	
	@SuppressWarnings("unused")
	public DecisionTree prune() throws CloneNotSupportedException{
		
		int N;
		int M;
		int P;
		int nodes;
		for (int i = 1; i <=L; i++) {
			
			// Copy D into Dprime
			dPrime = new DecisionTree();
			dPrime = (DecisionTree)D.clone();
			
			M = getRandomNumber(1, K);
			
			for (int j = 1; j <=M; j++) {
				
				N = getNumNonLeafNodes(dPrime);
				P = getRandomNumber(1, N);
				
				// Replace subtree
				replaceTreeAtP(P);
				nodes = getNumNonLeafNodes(D);
			}
			
			// Evaluate accuracy
			evaluateAccuracy();
			
			if(dPrime.accuracy>dBest.accuracy){
				dBest = dPrime;
			}
		}
		return dBest;
	}
	
	@SuppressWarnings({ "resource", "unused" })
	public void evaluateAccuracy(){
		
		try {

			BufferedReader br = new BufferedReader(new FileReader(validationFile));
			int attrVal,nodeAttr,classLabel,correct,count;
			String line;
			String input[];
			Node node = dPrime.rootNode;
			Node par = node;
			correct = count = 0;
			line = br.readLine();
			line = br.readLine();
			while(line!=null){
				
				++count;
				input = line.split(",");
				node = dPrime.rootNode;
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
			dPrime.accuracy = accuracy;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void replaceTreeAtP(int P){
		
		Queue<Node>queue = new LinkedList<Node>();
	    Node node = dPrime.rootNode;
	    Node leaf;
	    int n;
	    
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
	    	//leaf = new Node();
	    	//leaf.isLeaf = true;
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
	    	//node = leaf;
	    }
	}
	
	public int getNumNonLeafNodes(DecisionTree dt){
		
       Queue<Node>queue = new LinkedList<Node>();
       Node node;
       int n=1;
       
       //Initially add root node to the queue
       dt.rootNode.N = n;
       queue.add(dt.rootNode);
       
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
	
	public int getRandomNumber(int n1,int n2){
		
		int r = (int) (Math.random() * (n2 - n1)) + n1;
		return r;
	}
	
}
