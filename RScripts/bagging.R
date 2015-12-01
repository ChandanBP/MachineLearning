library(rpart)
library(adabag)

dataset <- read.csv('http://www.utdallas.edu/~axn112530/cs6375/creditset.csv',TRUE)

for(i in 1:10){


    index<-sample(1:nrow(dataset),size=0.9*nrow(dataset))
    
    dataset$default10yr = as.factor(dataset$default10yr)

    BGC.bag <- bagging(default10yr~.,data=dataset[index,],iter=20, nu=1, type="discrete")
    BGC.bag.pred <- predict.bagging(BGC.bag,newdata = dataset[-index,])
    BGC.bag.pred$error
}

