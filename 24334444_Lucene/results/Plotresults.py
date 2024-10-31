import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import os

# Define the base path for the TREC folders
base_path = "D:\\24334444_Lucene\\results\\TREC"

# Define the iteration folders
iteration_folders = ["SecondItr", "ThridItr", "FourthItr", "FifthItr"]

# Define similarity names
similarities = ["BM25.txt", "Classic.txt", "LMDirichlet.txt", "TFIDF.txt"]

# Define colors for each similarity using specified shades
colors = {
    "BM25.txt": ['#B0C4DE', '#4682B4', '#1E90FF'],  # LightSlateBlue, SteelBlue, DodgerBlue
    "Classic.txt": ['#FF7F7F', '#FF4D4D', '#FF0000'],  # LightCoral, Red, DarkRed
    "LMDirichlet.txt": ['#DDA0DD', '#9370DB', '#663399'],  # Plum, MediumPurple, RebeccaPurple
    "TFIDF.txt": ['#D3D3D3', '#A9A9A9', '#808080']  # LightGray, DarkGray, Gray
}

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

# Prepare data for plotting
num_iterations = len(iteration_folders)
bar_width = 0.2  # Width of each bar
x = np.arange(num_iterations)  # The label locations for iterations

# Function to create a bar plot for a specific score type
def plot_score(score_type):
    plt.figure(figsize=(14, 8))

    # Iterate over each similarity and plot the scores
    for i, similarity in enumerate(similarities):
        bars = plt.bar(x + i * bar_width, scores[similarity][score_type], width=bar_width,
                       label=similarity, color=colors[similarity][0])  # Use the first shade for the bar color

        # Add value annotations above each bar
        for bar in bars:
            yval = bar.get_height()
            plt.text(bar.get_x() + bar.get_width()/2, yval, f'{yval:.4f}',
                     ha='center', va='bottom', fontsize=10)

    # Set the x-axis labels and title
    plt.xlabel('Iterations')
    plt.ylabel(score_type)
    plt.title(f'{score_type} Scores Summary for Different Iterations and Similarities')
    plt.xticks(x + bar_width / 2, iteration_folders)  # Set x-axis to iteration names

    # Create a legend
    plt.legend(title='Similarity', bbox_to_anchor=(1.05, 1), loc='upper left')

    # Show grid lines for better readability
    plt.grid(axis='y')

    # Adjust layout
    plt.tight_layout()

    # Show the plot
    plt.show()

# Create separate plots for MAP, P_5, and Recall
plot_score("P_5 Score")
plot_score("MAP Score")
plot_score("Recall Score")
