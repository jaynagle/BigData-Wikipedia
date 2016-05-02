import os
import nltk
from nltk.corpus import wordnet as wn
from practnlptools.tools import Annotator
import csv
import traceback
from nltk import stem
import re 

path = "C:/Users/hp/Desktop/bigdata/POS Tagging/input/"
data = []

f = open('tagged_output.csv', 'wt')
csv.register_dialect('lineterminator',lineterminator='\n')
writer = csv.writer(f, dialect = csv.get_dialect('lineterminator'))

writer.writerow(('Cause', 'Cause_Nouns', 'Action', 'Effect', 'Effect_Nouns', 'FileName'))

for file in os.listdir(path):
    with open(path + file) as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            cause = ''
            cause_nouns = []
            action = ''
            effect = ''
            effect_nouns = []
            fileName = ''
            
            for cell in row:
                text = row[cell].strip()
                try:
                    tokens = text.split()
                    tags = nltk.pos_tag(tokens)
            
                    if(cell == 'Cause'):
                        for tag in tags:
                             if tag[1].startswith("NN"):
                                cause_nouns.append(tag[0])
                    elif(cell == 'Action'):
                        action = text
                    elif(cell == 'Effect'):
                        for tag in tags:
                            if tag[1].startswith("NN"):
                                effect_nouns.append(tag[0])    
                    elif(cell == 'fileName'):
                        fileName = text
                except Exception as e:
                    traceback.print_exc()
            
            cause = row['Cause']
            effect = row['Effect']

            writer.writerow((cause, ','.join(cause_nouns), action, effect, ','.join(effect_nouns), fileName))
            f.flush()