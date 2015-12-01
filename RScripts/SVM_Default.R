library(rpart)
library(e1071)

args<-commandArgs(TRUE)
fileContents <- read.csv(file = args[1],header = TRUE, sep = ",")
randomNumber <- runif(10,0.1,0.9)
totalAccuracy = 0
for(i in 1:10){

	val <- floor(randomNumber[i]*nrow(fileContents))
    index<-sample(nrow(fileContents),size=val)
    
    trainingData<-fileContents[index,]
    xTrainingData<-trainingData[,-9]
    yTrainingData<-as.factor(trainingData[,9])
    

    testData<-fileContents[-index,]
    xTestData<-testData[,-9]
    yTestData<-as.factor(testData[,9])

    model = svm(xTrainingData, yTrainingData, method = "C-classification", cost = 10, gamma = 0.1)
    result = table(predict(model,xTestData),yTestData)
    classZero <- result[1,1]+result[2,1]
    classOne <- result[1,2]+result[2,2]
    correctPredictions <- result[1,1]+result[2,2]
    total <- classZero+classOne
    accuracy <- correctPredictions/total
    accuracy <- accuracy*100.0
    cat(paste(accuracy,"\n"))
    totalAccuracy = totalAccuracy+accuracy
}
averageAccuracy = totalAccuracy/10
cat("Average accuracy=",averageAccuracy)
sum(testData$default10yr==p)/length(p)

