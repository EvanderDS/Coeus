import pandas as pd
# Need to change manually few filters
# Create a dictionary with the data
data = {
    "Analyzer/Filter": [
        "StandardTokenizer",
        "ClassicFilter",
        "EnglishPossessiveFilter",
        "ASCIIFoldingFilter",
        "LowerCaseFilter",
        "TrimFilter",
        "StopFilter",
        "LengthFilter",
        "PorterStemFilter",
        "SynonymGraphFilter",
        "SynonymMap.Builder"
    ],
    "Iteration Second": ["✔", "✔", "✔", "✔", "✔", "✔", "✔", "✔", "✔", "✖", "✖"],
    "Iteration Third": ["✔", "✔", "✖", "✖", "✔", "✔", "✔", "✔", "✖", "✖", "✖"],
    "Iteration Fourth": ["✔", "✔", "✖", "✖", "✔", "✔", "✔", "✔", "✖", "✖", "✖"],
    "Iteration Fifth": ["✔", "✔", "✔", "✔", "✔", "✔", "✔", "✔", "✔", "✔", "✔"]
}

# Create the DataFrame
df = pd.DataFrame(data)

# Display the DataFrame
print(df)
