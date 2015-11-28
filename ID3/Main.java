import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	
	// No of attributes
	int num_attr;
	int L;
	int K;
	int pClass,nClass;
	float sInfo;
	
	// To print yes or no
	String print;
	
	// Three Files
	File trainingFile;
	File validationFile;
	File testSetFile;
	
	Node[] attrList;
	ArrayList<Records>recordList;
	
	public Main(int l,int k,String tFile,String vFile,String testFile,String p){
		
		// Assign L and K
		L = l;
		K = k;
		
		// Allocate memory for file objects
		trainingFile = new File(tFile);
		validationFile= new File(vFile);
		testSetFile = new File(testFile);
		print = p;
		
		pClass = nClass = 0;
		
		recordList = new ArrayList<Records>();
	}
	
	public void updateInformationGain(String input[]){
	
		int val,classVal;
		
		classVal = Integer.parseInt(input[input.length-1]);
		if(classVal == 0){
			++pClass;
		}
		else{
			++nClass;
		}
		for (int i = 0; i < input.length-1; i++) {
			
			val = Integer.parseInt(input[i]);
			if(attrList[i]==null){
        		attrList[i] = new Node();
        		attrList[i].attr = i;
        	}
			
			if(val ==0 ){
				attrList[i].attrZero+=1;
				if(classVal == 0){
					attrList[i].zeroPInstances+=1;
				}
				if(classVal ==1){
					attrList[i].zeroNInstances+=1;
				}
			}
			else{
				attrList[i].attrOne+=1;
				if(classVal==0){
					attrList[i].onePInstances+=1;
				}
				if(classVal == 1){
					attrList[i].oneNInstances+=1;
				}
			}
			
		}
		
	}
	
	// Read training file
	@SuppressWarnings({ "resource" })
	public void readTrainingFile(){
		
		String line,input[];
		int count = 0;
		
		try{

			BufferedReader br = new BufferedReader(new FileReader(trainingFile));
			Records r;
            line = br.readLine();
            line = br.readLine();
            boolean first = true;
            
            while(line!=null){
            	
            	input = line.split(",");
            	
            	if(first){
            		this.num_attr = input.length-1;
            		attrList = new Node[this.num_attr];
            		first = false;
            	}
            	
            	updateInformationGain(input);
            	
            	r = new Records(input.length-1);
            	r.setRecordAttrs(input);
            	
            	recordList.add(r);
            	
            	++count;
            	line = br.readLine();
            }
			
            float t1 = (float)pClass/count;
			float t2 = (float)nClass/count;
			float r1 = (float)(Math.log(t1)/Math.log(2));
			float r2 = (float)(Math.log(t2)/Math.log(2));
			
			sInfo = (float)((-1*t1*r1)+(-1*t2*r2));
            
		}
		catch(FileNotFoundException fileNotFoundException){
			fileNotFoundException.printStackTrace();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws CloneNotSupportedException {
		
		if(args.length!=6){
			System.out.println("Invalid Arguments:Usage: <L> <K> <training-set> <validation-set> <test-set> <to-print>");
			return;
		}
	
		Main obj = new Main(Integer.parseInt(args[0]), Integer.parseInt(args[1]),args[2],
				             args[3], args[4], args[5]);
		
		obj.readTrainingFile();
		
		// Build Decision Tree using information gain as heuristics
		DecisionTree dTree = new DecisionTree(obj.recordList,obj.attrList,
				                              obj.sInfo,obj.num_attr); 
		dTree.construct();
		if(obj.print.equalsIgnoreCase("yes")){
			dTree.printTree(dTree.rootNode,0);
		}
		
		// Evaluate accuracy with the test set.
		dTree.accuracy = dTree.evaluateAccuracy(dTree,obj.testSetFile);
		System.out.println("*******Accuracy without pruning*******");
		System.out.println(dTree.accuracy);
		System.out.println();
		
		// Do the post pruning
		System.out.println("*******Accuracy after pruning*******");
		
		Node copy = (Node)dTree.rootNode.clone();
		PruneTree pObj = new PruneTree(obj.validationFile);
		Node best;
			best = pObj.prune(5, 5, copy);
			System.out.println(best.accuracy);
			copy = (Node)dTree.rootNode.clone();
	}
}
