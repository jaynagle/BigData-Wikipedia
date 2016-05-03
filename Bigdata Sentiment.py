
# coding: utf-8
# Author Vinay Vernekar

# In[28]:

import graphlab as gl
import re
from os import listdir
import os, sys
path = sys.argv[1]
dirs = os.listdir( path )
print (dirs)
newlist=[]
for i in dirs:
    newlist.append(i)
print (newlist)


# # Sentiment analysis proof of concept

# In[2]:

# we passed the main or introductory paragraph to analyze the sentiment and based on the result we notic that the classifier(Bag of Word)
# BOW classifier does a poor job on this in terms of identifying the sentiment. This inbuilt classifier word better on consumer rating or review data

import graphlab as gl
data = gl.SFrame({'text': ["The United States subprime mortgage crisis was a nationwide banking emergency that coincided with the U.S. recession of December 2007 June 2009. It was triggered by a large decline in home prices after the collapse of a housing bubble, leading to mortgage delinquencies and foreclosures and the devaluation of housing related securities. Declines in residential investment preceded the recession and were followed by reductions in household spending and then business investment. Spending reductions were more significant in areas with a combination of high household debt and larger housing price declines"]}) # this reads in the title and judges the sentiment of the title
m = gl.sentiment_analysis.create(data, features=['text'])
Sentiment_value=m.predict(data)
print Sentiment_value


# In[ ]:

for i in range (0,len(newlist)-1):
    if Sentiment_value[i]<0.5:
        senti="Negative"
    else:
        senti="Positive"
    print newlist[i] + ","+ senti


# In[3]:

print Sentiment_value




