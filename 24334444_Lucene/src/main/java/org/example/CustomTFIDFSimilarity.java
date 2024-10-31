package org.example;

import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

public class CustomTFIDFSimilarity extends TFIDFSimilarity {

    @Override
    public float tf(float freq) {
        return (float) (Math.log(1 + freq));
    }

    @Override
    public float idf(long docFreq, long docCount) {
        return (float) (Math.log((docCount + 1.0) / (docFreq + 1.0)));
    }

    @Override
    public float sloppyFreq(int distance) {
        return 1.0f;
    }

    @Override
    public float scorePayload(int doc, int start, int end, BytesRef payload) {
        return 1.0f;
    }

    @Override
    public float lengthNorm(int numTerms) {
        return 1.0f / (float) Math.sqrt(numTerms);
    }
}
