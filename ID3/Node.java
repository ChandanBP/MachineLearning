public class Node implements Cloneable{
	
	boolean isLeaf;
	float accuracy;
	int N;
	int attr;
    int attrZero;
    int attrOne;
    int val;
    int zeroPInstances;
    int zeroNInstances;
    int onePInstances;
    int oneNInstances;
    int classLabel;
    int pClassLabel;
    int nClassLabel;
    double informationGain;
    Node left;
    Node right;
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
    
    	Node node = new Node();
    	
    	node.accuracy = this.accuracy;
    	node.isLeaf = this.isLeaf;
    	node.N = this.N;
    	node.attr = this.attr;
    	node.attrZero = this.attrZero;
    	node.attrOne = this.attrOne;
    	node.val = this.val;
    	node.zeroPInstances = this.zeroPInstances;
    	node.zeroNInstances = this.zeroNInstances;
    	node.onePInstances = this.onePInstances;
    	node.oneNInstances = this.oneNInstances;
    	node.classLabel = this.classLabel;
    	node.pClassLabel = this.classLabel;
    	node.nClassLabel = this.nClassLabel;
    	node.informationGain = this.informationGain;
    	node.left = this.left;
    	node.right = this.right;
    	
    	return super.clone();
    }
    
}
