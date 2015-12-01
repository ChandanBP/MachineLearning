library(rpart)
library(caret)
library(klaR)

args<-commandArgs(TRUE)
fileContents <- read.csv(file = args[1],header = TRUE, sep = ",")
randomNumber <- runif(10,0.1,0.9)
output = matrix(,nrow = 10,ncol = 1)
totalAccuracy =0
cat("Experiment    Accuracy\n")
for(i in 1:10){

	val <- floor(randomNumber[i]*nrow(fileContents))
    index<-sample(nrow(fileContents),size=val)
    
    trainingData<-fileContents[index,]
    xTrainingData<-trainingData[,-9]
    yTrainingData<-as.factor(trainingData[,9])
    

    testData<-fileContents[-index,]
    xTestData<-testData[,-9]
    yTestData<-as.factor(testData[,9])

    model = train(xTrainingData,yTrainingData,'nb',trControl=trainControl(method='cv',number=10))
    result = table(predict(model$finalModel,xTestData)$class,yTestData)
    classZero <- result[1,1]+result[2,1]
    classOne <- result[1,2]+result[2,2]
    correctPredictions <- result[1,1]+result[2,2]
    total <- classZero+classOne
    accuracy <- correctPredictions/total
    accuracy <- accuracy*100.0
    totalAccuracy = totalAccuracy+accuracy
    output[i,1]<-accuracy
    cat(paste(i,output[i,1],"\n"))
}

averageAccuracy = totalAccuracy/10
cat("Average accuracy =",averageAccuracy)
cat("\n")
