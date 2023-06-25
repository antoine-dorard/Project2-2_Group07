
import torch
from transformers import TapasTokenizer
import pandas as pd
import collections
import numpy as np

# class to predict the answer for a given question and table

class TapasPredictor:
    def __init__(self):
        self.model= torch.load('schedule-tapas-small-finetuned-sqa.pth')
        self.tokenizer = TapasTokenizer.from_pretrained("google/tapas-small-finetuned-sqa")
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model.to(self.device)

    


def compute_prediction_sequence(model, data, device):
  """Computes predictions using model's answers to the previous questions."""
  
  # prepare data
  input_ids = data["input_ids"].to(device)
  attention_mask = data["attention_mask"].to(device)
  token_type_ids = data["token_type_ids"].to(device)

  all_logits = []
  prev_answers = None

  num_batch = data["input_ids"].shape[0]
  
  for idx in range(num_batch):
    
    if prev_answers is not None:
        coords_to_answer = prev_answers[idx]
        # Next, set the label ids predicted by the model
        prev_label_ids_example = token_type_ids_example[:,3] # shape (seq_len,)
        model_label_ids = np.zeros_like(prev_label_ids_example.cpu().numpy()) # shape (seq_len,)

        # for each token in the sequence:
        token_type_ids_example = token_type_ids[idx] # shape (seq_len, 7)
        for i in range(model_label_ids.shape[0]):
          segment_id = token_type_ids_example[:,0].tolist()[i]
          col_id = token_type_ids_example[:,1].tolist()[i] - 1
          row_id = token_type_ids_example[:,2].tolist()[i] - 1
          if row_id >= 0 and col_id >= 0 and segment_id == 1:
            model_label_ids[i] = int(coords_to_answer[(col_id, row_id)])

        # set the prev label ids of the example (shape (1, seq_len) )
        token_type_ids_example[:,3] = torch.from_numpy(model_label_ids).type(torch.long).to(device)   

    prev_answers = {}
    # get the example
    input_ids_example = input_ids[idx] # shape (seq_len,)
    attention_mask_example = attention_mask[idx] # shape (seq_len,)
    token_type_ids_example = token_type_ids[idx] # shape (seq_len, 7)
    # forward pass to obtain the logits
    outputs = model(input_ids=input_ids_example.unsqueeze(0), 
                    attention_mask=attention_mask_example.unsqueeze(0), 
                    token_type_ids=token_type_ids_example.unsqueeze(0))
    logits = outputs.logits
    all_logits.append(logits)

    # convert logits to probabilities (which are of shape (1, seq_len))
    dist_per_token = torch.distributions.Bernoulli(logits=logits)
    probabilities = dist_per_token.probs * attention_mask_example.type(torch.float32).to(dist_per_token.probs.device) 

    # Compute average probability per cell, aggregating over tokens.
    # Dictionary maps coordinates to a list of one or more probabilities
    coords_to_probs = collections.defaultdict(list)
    prev_answers = {}
    for i, p in enumerate(probabilities.squeeze().tolist()):
      segment_id = token_type_ids_example[:,0].tolist()[i]
      col = token_type_ids_example[:,1].tolist()[i] - 1
      row = token_type_ids_example[:,2].tolist()[i] - 1
      if col >= 0 and row >= 0 and segment_id == 1:
        coords_to_probs[(col, row)].append(p)

    # Next, map cell coordinates to 1 or 0 (depending on whether the mean prob of all cell tokens is > 0.5)
    coords_to_answer = {}
    for key in coords_to_probs:
      coords_to_answer[key] = np.array(coords_to_probs[key]).mean() > 0.5
    prev_answers[idx+1] = coords_to_answer
    
  logits_batch = torch.cat(tuple(all_logits), 0)
  
  return logits_batch

# generate answers for given questions and tables

def generate_answers(queries, table):
    " Given table and queries, generate answers, answers is a list of strings"
    inputs = self.tokenizer(table=table, queries=queries, padding='max_length', return_tensors="pt")
    logits = compute_prediction_sequence(self.model, inputs, self.device)
    predicted_answer_coordinates, = self.tokenizer.convert_logits_to_predictions(inputs, logits.cpu().detach())
    answers = []
    for coordinates in predicted_answer_coordinates:
        if len(coordinates) == 1:
        # only a single cell:
            answers.append(table.iat[coordinates[0]])
        else:
        # multiple cells
            cell_values = []
            for coordinate in coordinates:
                cell_values.append(table.iat[coordinate])
            answers.append(", ".join(cell_values))
    return answers        

queries = ["What do we have at 11 on Tuesday?","What is the next lecture?"]
table =  pd.read_csv("student_schedule_week.csv").astype(str)

answers = generate_answers(queries, table)