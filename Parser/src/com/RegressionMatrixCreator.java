package com;

import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class RegressionMatrixCreator {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		FSDirectory directory = FSDirectory.open(Paths.get("/media/anwar/825ED72B5ED716AF1/Wikipedia/index_SS"));

		IndexReader indexReader = DirectoryReader.open(directory);

		for(int documentIndex = 0; documentIndex<indexReader.maxDoc(); documentIndex++)
		{
			try
			{
				Document document = indexReader.document(documentIndex);
				Terms termVector = indexReader.getTermVector(documentIndex, "Page_Text");
				TermsEnum te = termVector.iterator();
				BytesRef term = null;
				PostingsEnum postings = null;
				while((term = te.next()) != null)
				{
					try
					{
						String termText = term.utf8ToString();
						//System.out.println(termText);
						postings = te.postings(postings, PostingsEnum.FREQS);
						postings.nextDoc();
						int freq = postings.freq();
						//Docs
						System.out.println(termText + "   freq: " + freq);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

}
