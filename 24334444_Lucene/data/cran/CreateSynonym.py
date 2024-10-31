import os
import nltk
from nltk.corpus import stopwords, wordnet
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer

# Initialize necessary components
stop_words = set(stopwords.words('english'))
stemmer = PorterStemmer()

def preprocess_text(text):
    # Tokenize the text
    tokens = word_tokenize(text.lower())
    # Remove stop words and apply stemming
    filtered_tokens = [stemmer.stem(word) for word in tokens if word.isalpha() and word not in stop_words]
    return filtered_tokens

def get_synonyms(word):
    synonyms = set()
    # Get synonyms from WordNet
    for syn in wordnet.synsets(word):
        for lemma in syn.lemmas():
            synonyms.add(lemma.name())
    return synonyms

def read_cranfield_dataset(file_path):
    relevant_words = set()

    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
        documents = content.split('.I')

        for doc in documents:
            if '.W' in doc:  # We are only interested in the content after '.W'
                # Split and extract relevant part
                sections = doc.split('.W')
                if len(sections) > 1:
                    text = sections[1]
                    words = preprocess_text(text)
                    relevant_words.update(words)

    return relevant_words

# Path to the Cranfield 1400 dataset
cranfield_file_path = '/content/sample_data/cran.all.1400'

# Load and process the dataset
relevant_words = read_cranfield_dataset(cranfield_file_path)

# Create a dictionary to store synonyms
synonyms_dict = {}

for word in relevant_words:
    synonyms = get_synonyms(word)
    if synonyms:  # Only add if there are synonyms found
        synonyms_dict[word] = list(synonyms)

# File path to store synonyms for Lucene
synonym_file_path = 'synonyms.txt'

# Write synonyms to a file in the Lucene format
with open(synonym_file_path, 'w', encoding='utf-8') as f:
    for word, synonyms in synonyms_dict.items():
        line = ', '.join([word] + synonyms)  # Combine word and its synonyms
        f.write(line + '\n')  # Write each line to the file

print(f"Synonyms have been saved to: {synonym_file_path}")
