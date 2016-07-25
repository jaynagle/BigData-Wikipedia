# -*- coding: utf-8 -*-
"""

@author: Vinay Vernekar
"""
from __future__ import division
import nltk
from nltk.corpus import wordnet
from nltk.corpus import wordnet as wn
from os import listdir
import os, sys
path = sys.argv[1]
dirs = os.listdir( path )
reload(sys)  
sys.setdefaultencoding('utf8')
holder=[]
for file in dirs:
    files= open(path + "\\" + file,"r")
    text= files.read()
	text= text.lower() # Converting the text in lower text for case sensitivity
    files.close()
    
    
	length= len(text)
	unique= sorted(set(text)) # The vocabulary of a text is just the set of tokens that it uses, since in a set, all duplicates are collapsed together
	unique_words= len(unique)
	lexical_richness= (unique_words/length)* 100
 
    details=[file,unique_words,lexical_richness]
	holder.append(details)

f = open("C:\\Second Sem\\Big Data and Analytics\\Project\\\\Output_lexical_richness.txt",'w')
for i in holder:
    f.write(i+'\n'),
f.close()

       

