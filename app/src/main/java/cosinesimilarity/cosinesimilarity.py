import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer


class CosineSimilarity:
    def __init__(self, q, a, user_question):
        self.questions = q

        self.answers = a
        vectorizer= TfidfVectorizer()
        X = vectorizer.fit(self.questions)
        array = X.transform(self.questions).toarray()

        self.user_question = user_question
        user_vector = X.transform(self.user_question).toarray()

        response = " "
        most_sim = 0
        for i in range(len(self.questions)):
            if most_sim < self.cosine_similarity(array[i], user_vector[0]):
                most_sim = self.cosine_similarity(array[i], user_vector[0])
                answer_index = i                    #get the index of the current most similar question
                response = self.answers[answer_index] #get the answer of the most similar question.
        print(response)

    def cosine_similarity(self, a, b):
        #Takes 2 vectors a, b and returns the cosine similarity according
        #to the definition of the dot product
        dot_product = np.dot(a, b)
        norm_a = np.linalg.norm(a)
        norm_b = np.linalg.norm(b)
        print("result " , dot_product / (norm_a * norm_b))
        return dot_product / (norm_a * norm_b)


if __name__ == '__main__':
    questions = [
        "What do I like?",
        "Who is the teacher of NLP?",
        "What is the favorite food for people in the Ashanti region of Ghana?",
        "What is John job?",
        "Which lectures are there on Monday at 9"
    ]

    answers = [
        "I like Johns food",
        "John is the teacher of NLP",
        "Otumfuo Osei Tutu I",
        "The job of John is deliverer",
        "We start the day with Math"
    ]

    CS = CosineSimilarity(questions,answers, ["What lectures are there on Monday at 9"])
