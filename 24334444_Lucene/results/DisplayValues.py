import pandas as pd
import os

# Define the base path for the TREC folders
base_path = "D:\\24334444_Lucene\\results\\TREC"

# Define the iteration folders
iteration_folders = ["SecondItr", "ThridItr", "FourItr", "FifthItr"]

# Define similarity names
similarities = ["BM25.txt", "Classic.txt", "LMDirichlet.txt", "TFIDF.txt"]

# Initialize a dictionary to store scores for each similarity across iterations
scores = {similarity: {"P_5 Score": [], "MAP Score": [], "Recall Score": []} for similarity in similarities}

# Read the CSV files and extract the scores
for folder in iteration_folders:
    folder_path = os.path.join(base_path, folder)
    csv_file_path = os.path.join(folder_path, "scores_summary.csv")

    # Read the CSV file
    df = pd.read_csv(csv_file_path)

    # Extract the scores for each similarity
    for index, row in df.iterrows():
        scores[row['File Name']]['P_5 Score'].append(row['P_5 Score'])
        scores[row['File Name']]['MAP Score'].append(row['MAP Score'])
        scores[row['File Name']]['Recall Score'].append(row['Recall Score'])

# Create a DataFrame to display the results in a table format
results = []

# Prepare table rows
for similarity in similarities:
    # Start with similarity name
    row = [similarity]
    for folder in iteration_folders:
        # Check if the similarity exists in the current folder's scores
        if similarity in scores:
            index = len(results)  # Get the current row index for this similarity
            map_score = scores[similarity]['MAP Score'][index] if index < len(scores[similarity]['MAP Score']) else None
            p_5_score = scores[similarity]['P_5 Score'][index] if index < len(scores[similarity]['P_5 Score']) else None
            recall_score = scores[similarity]['Recall Score'][index] if index < len(scores[similarity]['Recall Score']) else None
            row.extend([map_score, p_5_score, recall_score])  # Use extend to add multiple elements
        else:
            row.extend([None, None, None])  # Handle missing data

    results.append(row)

# Create a multi-index DataFrame to organize mega columns and mini columns
column_tuples = []
for folder in iteration_folders:
    column_tuples.append((folder, 'MAP'))
    column_tuples.append((folder, 'P_5'))
    column_tuples.append((folder, 'Recall'))

# Create the multi-index columns
multi_index_columns = pd.MultiIndex.from_tuples(column_tuples)

# Create the DataFrame
results_df = pd.DataFrame(results, columns=['Similarity'] + multi_index_columns.tolist())

# Set the 'Similarity' column as the index for better readability
results_df.set_index('Similarity', inplace=True)

# Display the results table
print(results_df.to_string(line_width=None))

# If needed, save the DataFrame to a CSV file
results_df.to_csv("results_summary.csv")
