### Mid term presentation regression ####


setwd("C:\\Second Sem\\Big Data and Analytics")

D=read.csv("Frequency.csv")
head(D)


Model <- lm(V ~ V1 +V2+ V3 + V4 + V5 + V6 + V7 + V8 + V9 + V10 +V11 , data = D )

summary(Model)

Model$coefficients

residualtest=resid(Model) # check to see the residulas of the model