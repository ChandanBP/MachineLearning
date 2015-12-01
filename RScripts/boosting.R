library(rpart)
library(adabag)

baggingData <- read.csv('http://www.utdallas.edu/~axn112530/cs6375/creditset.csv',TRUE)
randomNumber <- runif(10,0.1,0.9)

for(i in 1:10){

	
    index<-sample(1:nrow(baggingData),size=0.9*nrow(baggingData))
    
    dataset$default10yr = as.factor(dataset$default10yr)
    BC.rpart <- rpart(default10yr~.,data=dataset[index,-1], maxdepth=3)
    pred <- predict(BC.rpart,newdata=dataset[-index,-1],type="class")
    tb <-table(pred,dataset$default10yr[-index])

    BC.adaboost <- boosting(default10yr~.,data=dataset[index,],iter=20, nu=1, type="discrete")
    BC.adaboost.pred <- predict.boosting(BC.adaboost,newdata = dataset[-index,])
    BC.adaboost.pred$error
}

