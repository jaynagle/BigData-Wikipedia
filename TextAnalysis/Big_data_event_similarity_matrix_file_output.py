
# coding: utf-8
# Author Vinay Vernekar

# In[1]:

import nltk
from nltk.corpus import wordnet
from nltk.corpus import wordnet as wn
from os import listdir


# In[2]:

# Open a file
import os, sys
path = sys.argv[1]
dirs = os.listdir( path )
# This would print all the files and directories


# In[11]:

fileToNoun={}
for file in dirs:
    files= open(path + "\\" + file,"r")
    reading= files.readlines()
    files.close()
    ignoreCount = 0
    #print (reading)
    noun=[]
    verb=[]
    #print(reading)
    for i in reading:
        try:
            i.strip()
            n=str(i)
            text=nltk.word_tokenize(n)
            k=nltk.pos_tag(text)
            for j in k:
                if (len(j[0])<=2) or j[0]=="edit":
                    continue
                elif j[1]=="NN":
                    noun.append(j[0])
        except Exception,e:

            ignoreCount = ignoreCount + 1
    fileToNoun[file] = noun
    #print "Words Ignored: ", ignoreCount # To get how many words were ignored
print "done"




# In[4]:

# Calculating the WUP Similarity matrix
fileCount = len(fileToNoun)
Matrix = [[0 for x in range(fileCount)] for x in range(fileCount)] # length of pages in range
print "done"


# In[7]:

for i in range(fileCount):
    for j in range(fileCount):
        nouni = fileToNoun.values()[i]

        nounj = fileToNoun.values()[j]
        nfreqi =nltk.FreqDist(nouni)
        nfreqj =nltk.FreqDist(nounj)
        topNouni = nfreqi.most_common(30)# taking top 30 nouns based on the frequency of occurrance for event "A"
        topNounj = nfreqj.most_common(30)# taking top 30 nouns based on the frequency of occurrance for event "B"
        valueholder = []
        ignoreCount = 0
        for word in topNouni:
            try:
                word= word[0]+".n.01"
                word = wn.synset(word)
                for word2 in topNounj:
                    word2= word2[0]+".n.01"
                    try:
                        word2 = wn.synset(word2)
                        value=word.wup_similarity(word2)
                        valueholder.append(value)
             
                    except Exception,e:
                        ignoreCount = ignoreCount + 1;
            except Exception,e:
                ignoreCount = ignoreCount + 1;
        z=sum(valueholder)   
        Matrix[i][j] = z
print "Done"    


# In[8]:

# normalizing the Similarity matrix

for i in range(fileCount):
    MaxValue = Matrix[i][i]
    for j in range(fileCount):
        Matrix[i][j] = Matrix[i][j]/MaxValue
print "done"


# In[9]:

# Writing the file as an output to get the CSV file to be used for Neo4j graph

import csv
with open('C:\Users\Vinay\Desktop/bigdata\BigData_final_event_similarity.csv', 'wb') as csvfile:
    spamwriter = csv.writer(csvfile)
    for i in range(fileCount):
        list1 = [1,2,3]
        for j in range(fileCount):
            list1[0] = dirs[i]
            list1[1] = dirs[j]
            list1[2] = Matrix[i][j]
            spamwriter.writerow(list1)
# This file is further edited using excel functionality and fed into Neo4j

