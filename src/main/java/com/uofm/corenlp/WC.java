package com.uofm.corenlp;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.PolarBlendMode;
import com.kennycason.kumo.PolarWordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;

public class WC {

	public static void main(String[] args) throws IOException {
		/*final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		List<WordFrequency> wordFrequencies = null;
		try {
			wordFrequencies = frequencyAnalyzer.load(new FileInputStream("C:\\Users\\Maxim\\eclipse-workspace\\SentimentAnalysis\\MSCI.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Dimension dimension = new Dimension(600, 600);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(2);
		wordCloud.setBackground(new CircleBackground(300));
		wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
		wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
		wordCloud.build(wordFrequencies);
		wordCloud.writeToFile("MSCI.png");
*/
		
		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		frequencyAnalyzer.setWordFrequenciesToReturn(750);
		frequencyAnalyzer.setMinWordLength(4);
		frequencyAnalyzer.setStopWords(loadStopWords());

		final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new FileInputStream("C:\\Users\\Maxim\\eclipse-workspace\\SentimentAnalysis\\PositiveNDAQ.txt"));
		final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(new FileInputStream("C:\\Users\\Maxim\\eclipse-workspace\\SentimentAnalysis\\NegativeNDAQ.txt"));
		final Dimension dimension = new Dimension(600, 600);
		final PolarWordCloud wordCloud = new PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
		wordCloud.setPadding(2);
		wordCloud.setBackground(new CircleBackground(300));
		wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
		wordCloud.build(wordFrequencies, wordFrequencies2);
		wordCloud.writeToFile("PolarNDAQ.png");
	}

	 private static Set<String> loadStopWords() throws IOException {
	        return new HashSet<String>(IOUtils.readLines(new FileInputStream("C:\\Users\\Maxim\\eclipse-workspace\\SentimentAnalysis\\stop_words.txt")));
	    }
	 /*private static InputStream getInputStream(final String path) {
	        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}*/
}
