package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import is2.data.SentenceData09;
import is2.parserR2.Reranker;
import se.lth.cs.srl.SemanticRoleLabeler;
import se.lth.cs.srl.corpus.Sentence;
import se.lth.cs.srl.options.ParseOptions;
import se.lth.cs.srl.pipeline.Pipeline;

public class SemMarker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Create a data container for a sentence
		try
		{
				SentenceData09 i = new SentenceData09();

				if (true) { // input might be a sentence: "This is another test ." 
					StringTokenizer st = new StringTokenizer("the lower credit quality ultimately caused massive defaults");
					ArrayList<String> forms = new ArrayList<String>();
					
					forms.add("<root>");
					while(st.hasMoreTokens()) forms.add(st.nextToken());
					
					i.init(forms.toArray(new String[0]));
					
				} else {
					// provide a default sentence 
					i.init(new String[] {"<root>","This","is","a","test","."});
				}

				//print the forms
				for (String l : i.forms) System.out.println("form :  "+l);
		
		ParseOptions parse = new ParseOptions(new String[]{"eng", "/home/anwar/Downloads/corpus.txt" ,"/home/anwar/Downloads/srl-eng.model", "/home/anwar/Downloads/output-test.txt"});
		SemanticRoleLabeler ab = new se.lth.cs.srl.pipeline.Reranker(parse);
		Sentence s = new Sentence(i, true);
		
		ab.parseSentence(s);
		String st = ab.getStatus();
		System.out.println(st);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

	}

}
