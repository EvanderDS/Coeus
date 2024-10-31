import os
import pandas as pd

# Directory containing the result files
directory = r'D:\24334444_Lucene\results\TREC\FifthItr'

# List to store the extracted scores
data = []

# Read the first 5 .txt files in the directory
for filename in os.listdir(directory):
    if filename.endswith('.txt'):
        file_path = os.path.join(directory, filename)

        with open(file_path, 'r') as file:
            lines = file.readlines()

        # Initialize variables for scores
        p_5 = None
        map_score = None
        recall_score = None

        # Extract relevant scores
        for line in lines:
            # Strip leading and trailing whitespace and split by whitespace
            parts = line.strip().split()
            if len(parts) >= 3:  # Ensure there are enough parts to avoid index errors
                if parts[0] == 'P_5':
                    p_5 = float(parts[-1])  # Get the last part
                elif parts[0] == 'map':
                    map_score = float(parts[-1])  # Get the last part
                elif parts[0] == 'recall_5':
                    recall_score = float(parts[-1])  # Get the last part

        # Append the results to the data list
        data.append({
            'File Name': filename,
            'P_5 Score': p_5,
            'MAP Score': map_score,
            'Recall Score': recall_score
        })

# Create a DataFrame from the data list
df = pd.DataFrame(data)

# Print the DataFrame
print(df)

# Optionally, save the results to a new CSV file
df.to_csv(os.path.join(directory, 'scores_summary.csv'), index=False)
