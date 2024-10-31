package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // Tokenizer
        StandardTokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = tokenizer;

        // Apply filters
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new ASCIIFoldingFilter(tokenStream);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new TrimFilter(tokenStream);
        tokenStream = new LengthFilter(tokenStream, 2, 20);
        tokenStream = new PorterStemFilter(tokenStream);
        CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
        tokenStream = new StopFilter(tokenStream, stopWords);

        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    public List<String> analyze(String text) throws IOException {
        List<String> result = new ArrayList<>();


        try (TokenStream tokenStream = tokenStream("content", text)) {
            CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                result.add(attr.toString());
            }
            tokenStream.end();
        }
        return result;
    }
}
