from tapas_predictor import TapasPredictor
import pandas as pd
import os


current_dir   = os.path.dirname(os.path.realpath(__file__))

table =  pd.read_csv(current_dir + "\\student_schedule_week.csv").astype(str)


days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]

time_table = table["Time"]
times = []

for time in time_table:
    times.append(time)



dictionary = {}

for day in days:

    indexed_table = table[day]

    stop = False
    answers = {}
    i = 0

    while not stop:
        try:
            item = {time_table[i]: indexed_table[i]}
            answers.update(item)
        except:
            stop = True
        i = i + 1 

    new_item = {day: answers}
    dictionary.update(new_item)




################################################################################

filelist= []

experiments_file = open(current_dir + "\\tapas_predictor_experiment_set.txt",'r')
for line in experiments_file:
    line = line.strip()
    filelist.append(line)

experiments_file.close()


qas = {}

for question in filelist:

    for day in days:
        day_question = question.replace("<DAY>", day)

        for time in times:
            time_question = day_question.replace("<TIME>", time)

            qa = {time_question: dictionary[day][time]}
            qas.update(qa)



qas_multi = {}

for question in filelist:

    for i in range(len(days)):
        day_question = question.replace("<DAY>", days[i])

        for j in range(len(times)-1):
            time_question = day_question.replace("<TIME>", times[j])

            try:
                qa = {time_question: dictionary[days[i]][times[j+1]]}
                qas_multi.update(qa)
            except:
                # skip the out-of-bounds indexes
                pass

#################################################################################

predictor = TapasPredictor(model_path = current_dir + "\\schedule-tapas-small-finetuned-sqa.pth")


i = 0
answers = []
trues = 0
falses = 0
false_values = []

trues_multi = 0
falses_multi = 0
false_values_multi = []

for question in list(qas.keys()):

    progress = "Busy with processing... (" + str(i) + " of " + str(len(qas)) + ")"
    print(progress)

    prediction = predictor.generate_answers([question, "What is the next lecture?"], table)
    answer = prediction[0]
    answer_multi = prediction[1]

    answers.append(answer)
    print("Answer : " + answer)

    expected = qas[question]
    print("Expected : " + expected)

    print("T : " + str(trues) + " | F : " + str(falses) + " | Acc : " + str(trues/(trues+falses)))

    if(answer == expected):
        trues = trues + 1
    else:
        falses = falses + 1
        false_val = {"question": question, "expected": expected, "answer": answer}
        false_values.append(false_val)

    try:
        expected_multi = qas_multi[question]
        print("Next lecture 'Answer' : " + answer_multi)
        print("Next lecture 'Expected' : " + expected_multi)

        if(answer_multi == expected_multi):
            trues_multi = trues_multi + 1
        else:
            falses_multi = falses_multi + 1
            false_val = {"question": question, "expected": expected_multi, "answer": answer_multi}
            false_values_multi.append(false_val)
    except:
        pass

    i = i + 1


print("Processing done...\n")
print(answers)
print("True : " + str(trues))
print("False : " + str(falses))
print("Accuracy : " + str(trues/(trues+falses)))

print("False values : ")
print(false_values)



##############################
# FILE WRITE

items = []

items.append("Experiments Results.")
items.append(" ")
items.append("True : " + str(trues))
items.append("False : " + str(falses))
items.append("Accuracy : " + str(trues/(trues+falses)))
items.append(" ")
items.append("False Values")
items.append("------------")

for value in false_values:
    items.append("question : " + value["question"])
    items.append("expected : " + value["expected"])
    items.append("answer : " + value["answer"])
    items.append(" ")


write_file = open(current_dir + "\\experiments_result.txt",'w')

for item in items:
	write_file.write(item+"\n")

write_file.close()


##############################
# FILE WRITE MULTI

items = []

items.append("Experiments Results of Multi-turn Dialog.")
items.append(" ")
items.append("True : " + str(trues_multi))
items.append("False : " + str(falses_multi))
items.append("Accuracy : " + str(trues_multi/(trues_multi+falses_multi)))
items.append(" ")
items.append("False Values")
items.append("------------")

for value in false_values_multi:
    items.append("question : " + value["question"])
    items.append("expected : " + value["expected"])
    items.append("answer : " + value["answer"])
    items.append(" ")


write_file_multi = open(current_dir + "\\experiments_result_multi.txt",'w')

for item in items:
	write_file_multi.write(item+"\n")

write_file_multi.close()

