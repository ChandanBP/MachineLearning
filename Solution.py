import json
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression


n = int(input())

questions = []
for i in range(n):
    s = input()
    questions.append(json.loads(s))

rawDocs = [q.get("question_text") for q in questions]
topics = set()
counts = set()
for q in questions:
    counts.add(q.get("view_count"))
    counts.add(q.get("follow_count"))
    counts.add(q.get("age"))
    for t in q.get("topics"):
        topics.add(t.get("name"))
rawDocs.extend(list(topics))
rawDocs.extend(map(str, counts))

vectorizer = TfidfVectorizer(ngram_range=(1,3), analyzer='char', max_df=0.5,sublinear_tf=True)
vectorizer.fit(rawDocs)


question = dict()
for q in questions:
    topics = sorted(q.get("topics"), key = lambda t: -t.get("followers"))
    topics = ' '.join([t.get("name") for t in topics])
    topics += ' ' + q.get("question_text")
    topics += ' ' + str(q.get("view_count")) + ' ' + str(q.get("follow_count")) + ' ' +\
            str(q.get("age"))

    question[q.get("question_key")] = topics


d = int(input())
train = []
target = []
for i in range(d):
    x, y, z = input().split()
    train.append(question[x] + ' ' + question[y])
    target.append(int(z))

train = vectorizer.transform(train)

model = LogisticRegression(C=2.0)
model.fit(train, target)

N = int(input())
pred = []
answer = []
for i in range(N):
    x, y = input().split()
    answer.append((x, y))
    pred.append(question[x] + ' ' + question[y])

pred = vectorizer.transform(pred)

pred = model.predict(pred)

for i in range(N):
    print(answer[i][0], answer[i][1], pred[i])