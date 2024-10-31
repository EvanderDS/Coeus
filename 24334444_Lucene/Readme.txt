This Lucene Project is divided into the following folders, as given below.
The limitations of the code can be seen in the Analysers. Different analysers could have been used.
Another limitation is the synonym graph as the model which is usibng that is given very poor performance. Generating the Synonym graph another way would be beneficial for future research.

Data :
- Contains Cran dataset qry and document files
- Qrels for Cran Dataset
- CreateSynonym.py : Creates a Synonym Map using NLTK in python and stores it in Synonyms.txt
- Synonyms.txt : Contains Map of the Synonyms

Index:
- Indexable format of documents

Results:
- FifthItr/FourthItr/Thriditr/SecondItr : Results of four similarities:
    - Each has BM25, Classic, LMDirichlet, TF-IDF txt results which contain scores in TREC format

- FirstItr: Results of test Lucene example
    - has BM25, Classic, LMDirichlet, TF-IDF txt results which contaon scores in TREC format

- TREC
    - Has Iterations folders 1 to 5th
    - Contains all trec scores for BM25, Classic, LMDirichlet, TF-IDF

- compare-trec-results.py : Python script to compare the trec results for all iterations
- DisplayValues.py : Python Script to display all scores in readable format
- FlitersUsed.py : Python Script to display the filters used in each iteration
- Plotresults.py : Python Script to display the Plots of MAP, P_5 and Recall for all iterations
- QRelsCorrectedforTRECeval.txt : Corected Qrels and ranking changed such that -1 becomes 5 and the other values are shifted for rank
- results_summary.csv : Summary of results for Latex report

-src
  -Java
     -org.example
       -CustomAnalyser: Self-Defined Custom Analyser with various different filters
       -CustomTFIDFSimilarity: Self-Defined Custom TFIDF for Lucene
       -Indexer: Indexes the dataset
       -IndexerViewer: Views the indexable format of document for the user
       -Main: Indexes, Analysing, querying the dataset
       -Parser: Parser for the Indexer to read Cran based on .T, .A, .W and .B
       -QueryFiles: Queries the files in cran dataset

