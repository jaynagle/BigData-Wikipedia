import nltk
from nltk.corpus import wordnet
from nltk.corpus import wordnet as wn

files= open("C:\\Users\\Vinay\\Desktop\\bigdata\\subprime1.txt","r")
reading= files.readlines()
files.close()
noun=[]
verb=[]
print(reading)
for i in reading:
    i.strip()
    n=str(i)
    text = nltk.word_tokenize(n)
    k=nltk.pos_tag(text)
    for j in k:
        if len(j)<=2:
            continue
        elif j[1]=="NN":
            noun.append(j[0])
        elif j[1]=="VBD":
            verb.append(j[0])
        
nfreq=nltk.FreqDist(noun)
vfreq=nltk.FreqDist(verb)

nfreq.most_common(50)

print(nfreq.most_common(50))
print("nouns are ", noun)

eventvalue=[]
valueholder=[]
list1=["mortgage","debt"]
list2=["house","market"]
for word in list1:
    word= word+".n.01"
    word = wn.synset(word)
    for word2 in list2:
        word2= word2+".n.01"
        word2 = wn.synset(word2)
        value=word.wup_similarity(word2)
        valueholder.append(value)
        print(value,word,word2)
    z=sum(valueholder)
    eventvalue.append(z)
print(sum(eventvalue))
       

