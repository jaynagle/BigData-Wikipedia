# This program is designed to see the WUP Similarity matrix as a heatmap
# Author Vinay Vernekar


install.packages("corrplot")
library(corrplot)

setwd("C:\\Second Sem\\Big Data and Analytics")

D=read.csv("datamatrix.csv")
head(D)
heatmap(as.matrix(D[-1,-1]))

