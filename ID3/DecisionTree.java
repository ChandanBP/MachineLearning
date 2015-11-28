import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DecisionTree implements Cloneable {

	DecisionTree dT;
	boolean visitedAttr[];
	int bestAttr,num_Attr;
	float accuracy;
	double entropyOfS;
	Node rootNode;
	Node[] attrList;
	Node leafNode;
	ArrayList<Records>leftRecordList;
    ArrayList<Records>rightRecordList;
    ArrayList<Records>recordList;
	static String[] nameIndexAttributeMap = {"XB","XC","XD","XE","XF","XG","XH","XI","XJ","XK","XL","XM","XN","XO","XP","XQ","XR","XS","XT","XU"};
    
	public DecisionTree(){
		
	}
	
	public DecisionTree(DecisionTree d){
		this.dT = d;
	}
	
	public DecisionTree(ArrayList<Records>rList,Node[] nList,double infGain,int numAttr){
		
		recordList = leftRecordList = rightRecordList = rList;
		attrList = nList;
		entropyOfS = infGain;
		num_Attr = numAttr;
		
		//visitedAttr = new boolean[numAttr];
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public double entropyOfChildren(Node node,int val){
		
		double entropy;
		double t1,r1,t2,r2;
		
		t1 = t2 = r1 = r2 = 0;
		if(val == 0){
			
			// ZeroZero/zero
			t1 = (double)node.zeroPInstances/node.attrZero;
			
			// ZeroOne/zero
			t2 = (double)node.zeroNInstances/node.attrZero;
			
		}
		if(val ==1){
			
			// ZeroZero/zero
			t1 = (double)node.onePInstances/node.attrOne;
		    
			// ZeroOne/zero
			t2 = (double)node.oneNInstances/node.attrOne;
		}
		
		
		if(t1==0 || t2==0){
			entropy = 0;
		}
		else{
			r1 = (double)(Math.log(t1)/Math.log(2));
			r2 = (double)(Math.log(t2)/Math.log(2));
			entropy = (-1*t1*r1)+(-1*t2*r2);
		}
		return entropy;
	}
	
	public double getEntropyOFS(Node node){
		
		
		int pClassCount = node.zeroPInstances+node.onePInstances;
		int nClassCount = node.zeroNInstances+node.oneNInstances;
		int totalCount = pClassCount+nClassCount;
		
		double t1 = (double)pClassCount/totalCount;
		double t2 = (double)nClassCount/totalCount;
		
		double r1 = (Math.log(t1)/Math.log(2));
		double r2 = (Math.log(t2)/Math.log(2));
		
		double entropy = (-1 * t1 * r1)+(-1 * t2 * r2);
		return entropy;
	}
	
	
	public double calculateInformation(Node node){
		
		int totalCount;
		double infoGain,pveEntropy,nveEntropy,temp1,temp2,entropyOFS;
		
		totalCount = node.attrZero+node.attrOne;
		entropyOFS = getEntropyOFS(node);
		pveEntropy = entropyOfChildren(node,0);
		pveEntropy = roundOff(pveEntropy);
		nveEntropy = entropyOfChildren(node,1);
		nveEntropy = roundOff(nveEntropy);
		
		temp1 = (double)node.attrZero/totalCount;
		temp1 = roundOff(temp1);
		temp2 = (double)node.attrOne/totalCount;
		temp2 = roundOff(temp2);
		
		infoGain = entropyOFS - (temp1*pveEntropy)-(temp2*nveEntropy);
		infoGain = roundOff(infoGain);
		return infoGain;
	}
	
	public double roundOff(double value){
		return Math.floor(value*1000)/1000;
	}
	
	public Node setRootNode(){
		
	  double max = Double.MIN_VALUE,infoGain;	
		
	  for (int i = 0; i < attrList.length; i++) {
		    
		  infoGain = calculateInformation(attrList[i]);
		  if(infoGain>max){
			  max = infoGain;
			  attrList[i].informationGain = infoGain;
			  rootNode = attrList[i];
			  bestAttr = i;
		  }
		  
	  }
	  return rootNode;
	}
	
	public void construct(){
		
    	buildTree(rootNode, recordList);
		System.out.println();
	}
	
	public ArrayList<Records> getSubRecords(ArrayList<Records>list,int attrPos,int attrVal){
		
		ArrayList<Records>subList = new ArrayList<Records>();
		Records record;
		int val;
		
		for (int i = 0; i < list.size(); i++) {
			
			record = list.get(i);
			val = Integer.parseInt(record.getRecordAttrs()[attrPos]);
			if(val == attrVal){
				subList.add(record);
			}
		}
		
		return subList;
	}
    
	@SuppressWarnings("unused")
	public Node findBestAttribute(ArrayList<Records>list){
		
		Records record;
		String input[];
		int count = list.size();
		int attrVal,classVal,pClass,nClass;
		Node[] attrNodes = new Node[num_Attr];
		Node bestNode = null;
		
		pClass = nClass = 0;
		
		// For number of records
		for (int i = 0; i < list.size(); i++) {
			
			record = list.get(i);
			input = record.getRecordAttrs();
			classVal = Integer.parseInt(input[input.length-1]);
			
			if(classVal == 0){
				++pClass;
			}
			else{
				++nClass;
			}
			
			// For each attribute
			for (int j = 0; j < input.length-1; j++) {
				
				if(!visitedAttr[j]){
					
					if(attrNodes[j]==null){
						attrNodes[j] = new Node();
					}
					attrNodes[j].attr = j;
					if(Integer.parseInt(input[j]) == 0){
						attrNodes[j].attrZero+=1;
						if(classVal == 0){
							attrNodes[j].zeroPInstances+=1;
						}
						else{
							attrNodes[j].zeroNInstances+=1;
						}
					}
					else{
						attrNodes[j].attrOne+=1;
                        if(classVal == 0){
							attrNodes[j].onePInstances+=1;
						}
						else{
							attrNodes[j].oneNInstances+=1;
						}
						
					}
				}
			}
		}
		
		boolean flag = false;
		for (int i = 0; i < visitedAttr.length; i++) {
			if(visitedAttr[i] == false){
				flag = true;
				break;
			}
		}
		if(flag == false){
			leafNode = new Node();
			if(pClass>nClass||pClass==list.size()){
				leafNode.classLabel = 0;
			}
			if(nClass>pClass||nClass==list.size()){
				leafNode.classLabel = 1;
			}
			return bestNode;
		}
		
		double max = Double.MIN_VALUE;
		double infoGain;
		int bestAtt = -1;
		for (int i = 0; i < attrNodes.length; i++) {
			
			if(!visitedAttr[i]){
				infoGain = calculateInformation(attrNodes[i]);
				if(infoGain>max){
					max = infoGain;
					bestAtt = attrNodes[i].attr;
					bestNode = attrNodes[i];
				}
			}
		}
		if(bestAtt==-1){
			
			leafNode = new Node();
			if(pClass == list.size()||pClass>nClass){
				leafNode.classLabel = 0;
			}
			if(nClass == list.size()||nClass>pClass){
				leafNode.classLabel = 1;
			}
		}
		return bestNode;
	}
	
	
	public void buildTree(Node node,ArrayList<Records>list){
		
		ArrayList<Records>lSubList,rSubList;
		visitedAttr = new boolean[num_Attr];
		Node leaf;
		if(node == null){
			node = new Node();
			node = setRootNode();
			visitedAttr[node.attr] = true;
			visitedAttr[node.attr] = true;
		}
		
		lSubList = getSubRecords(list, node.attr, 0);
		rSubList = getSubRecords(list, node.attr, 1);
		
		node.left = findBestAttribute(lSubList);
		if(node.left==null){
			node.pClassLabel = leafNode.classLabel;
			leaf = new Node();
			leaf.isLeaf = true;
			leaf.classLabel = leafNode.classLabel;
			node.left = leaf;
		}
		node.right = findBestAttribute(rSubList);
		if(node.right == null){
			node.nClassLabel = leafNode.classLabel;
			leaf = new Node();
			leaf.isLeaf = true;
			leaf.classLabel = leafNode.classLabel;
			node.right = leaf;
		}
		if(node.left.isLeaf!=true){
			visitedAttr[node.left.attr] = true;
			node.left.val = 0;
			buildTree(node.left,lSubList);
		}
		
		if(node.right.isLeaf!=true){
			visitedAttr[node.right.attr] = true;
			if(node.left!=null){
				visitedAttr[node.left.attr] = false;
			}
			node.right.val = 1;
			buildTree(node.right,rSubList);
		}
	}
	
	
	
	public float evaluateAccuracy(DecisionTree dTree,File testFile){
		
		float accr = 0;
		try {

			BufferedReader br = new BufferedReader(new FileReader(testFile));
			int attrVal,nodeAttr,classLabel,correct,count;
			String line;
			String input[];
			Node node = dTree.rootNode;
			
			
			correct = count = 0;
			line = br.readLine();
			line = br.readLine();
			while(line!=null){
				
				++count;
				input = line.split(",");
				node = dTree.rootNode;
				while(!node.isLeaf){
					nodeAttr = node.attr;
					attrVal = Integer.parseInt(input[nodeAttr]);
					if(attrVal == 0){
						node = node.left;
					}
					if(attrVal == 1){
						node = node.right;
					}
				}
				
				classLabel = Integer.parseInt(input[input.length-1]);
				if(node.classLabel == classLabel){
					++correct;
				}
				line = br.readLine();
			}
			br.close();
			accr = (float)correct/count;
			accr = accr*100f;
			
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accr;
	}
	
    public void printSpaces(int tabCount){
		
		for (int i = 0; i <= tabCount; i++) {
			System.out.print(" |");
		}
	}
	
public void printTree(Node node,int tabCount){
		
		Integer left = new Integer(tabCount);
		Integer right = new Integer(tabCount);
		printSpaces(left);
		if(node.left.isLeaf == true){
			System.out.println(DecisionTree.nameIndexAttributeMap[node.attr]+"= 0:"+node.pClassLabel);
		}
		else{
			System.out.println(DecisionTree.nameIndexAttributeMap[node.attr]+"= 0:");
			printTree(node.left,left+1);
		}
		if(node.right.isLeaf == true){
			printSpaces(right);
			System.out.println(DecisionTree.nameIndexAttributeMap[node.attr]+"= 1:"+node.nClassLabel);
		}
		else{
			printSpaces(right);
			System.out.println(DecisionTree.nameIndexAttributeMap[node.attr]+"= 1:");
			printTree(node.right,right+1);
		}
}

}
