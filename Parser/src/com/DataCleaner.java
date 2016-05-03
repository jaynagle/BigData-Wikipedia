package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wiki XML page extractor to find frequency of years and dump it in a CSV file.
 * 
 * @author Jay Nagle
 *
 */
public class DataCleaner {
	private static Pattern pattern = Pattern.compile("(19|20)\\d{2}");
	private static Pattern pattern_title = Pattern.compile("<title>.*<\\/title>");
	private static String dir = "D:/Wikipedia DataSet/output/regression";
	private Set<Integer> yearSet = new HashSet<>();

	public static void main(String[] args) {
		DataCleaner dataCleaner = new DataCleaner();
		Map<String, Map<Integer, Integer>> titleDateMap = new HashMap<>();

		String fileName = null;
		File folder = new File(dir);
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				fileName = dir + "/" + fileEntry.getName();
				dataCleaner.readFile(fileName, titleDateMap);
			}
		}
		dataCleaner.writeToCsv(titleDateMap);
	}

	private void readFile(String fileName, Map<String, Map<Integer, Integer>> titleDateMap) {
		try {
			String text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			Matcher m1 = pattern_title.matcher(text);
			String title = null;
			if (m1.find()) {
				String tmpTitle = m1.group(0);
				title = m1.group(0).substring(7, tmpTitle.length() - 8);
			}
			Matcher m = pattern.matcher(text);

			while (m.find()) {
				Map<Integer, Integer> dateMap = titleDateMap.get(title);
				Integer freq = null;
				yearSet.add(Integer.parseInt(m.group(0)));
				if (dateMap != null) {
					freq = dateMap.get(Integer.parseInt(m.group(0)));
					if (freq != null) {
						freq++;
						dateMap.put(Integer.parseInt(m.group(0)), freq);
					} else {
						dateMap.put(Integer.parseInt(m.group(0)), m.groupCount());
					}
				} else {
					dateMap = new HashMap<>();
					dateMap.put(Integer.parseInt(m.group(0)), m.groupCount());
					titleDateMap.put(title, dateMap);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeToCsv(Map<String, Map<Integer, Integer>> titleDateMap) {
		System.out.println(titleDateMap);
		TreeSet myTreeSet = new TreeSet();
		myTreeSet.addAll(yearSet);
		System.out.println(myTreeSet);

		try {
			FileWriter writer = new FileWriter(dir + "/freq/freq.csv");

			Object[] yearArray = myTreeSet.toArray();
			StringBuffer bf = new StringBuffer();
			bf.append("title");
			bf.append(",");
			for (Object i : myTreeSet) {
				bf.append(i);
				bf.append(",");
			}
			bf.deleteCharAt(bf.length() - 1);
			writer.append(bf);
			writer.append("\n");

			for (Entry<String, Map<Integer, Integer>> entry : titleDateMap.entrySet()) {
				String title = entry.getKey();
				Map<Integer, Integer> dateMap = entry.getValue();

				TreeMap<Integer, Integer> dateTreeMap = new TreeMap<>(dateMap);
				bf = new StringBuffer();
				int k = 0;

				bf.append(title);
				bf.append(",");

				for (Entry<Integer, Integer> entry2 : dateTreeMap.entrySet()) {
					Integer year = entry2.getKey();
					Integer freq = entry2.getValue();

					while (!yearArray[k].equals(year)) {
						bf.append(0);
						bf.append(",");
						k++;
					}

					bf.append(freq);
					bf.append(",");
					k++;
				}
				while (yearArray.length > k) {
					bf.append(0);
					bf.append(",");
					k++;
				}
				bf.deleteCharAt(bf.length() - 1);
				writer.append(bf);
				writer.append("\n");
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}