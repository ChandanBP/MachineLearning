library(class)

args <- commandArgs(TRUE)
fileContents <- read.csv(file = args[1],header = TRUE, sep = ",")
randomNumber <- runif(10,0.1,0.9)
totalAccuracy = 0

for(i in 1:10){

	val <- floor(randomNumber[i]*nrow(fileContents))
	index<-sample(nrow(fileContents),size=val)
	trainingData<-fileContents[index,]
	testData<-fileContents[-index,]

	targetTrainingData<-fileContents[index,9]
	testTrainingData<-fileContents[-index,9]

    knnModel <- knn(trainingData, testData, as.factor(targetTrainingData), k = 11,prob=TRUE)
    summary(knnModel)
    tab<-table(testTrainingData,knnModel)
    accuracy<-sum(diag(tab))/sum(tab)
    accuracy<-accuracy*100
    totalAccuracy = totalAccuracy+accuracy
    
}
averageAccuracy = totalAccuracy/10
paste("Average accuracy =",averageAccuracy,"%",sep="")