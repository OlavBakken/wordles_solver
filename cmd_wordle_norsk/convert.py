import re, codecs
word_file = codecs.open("ordliste.txt", 'r', encoding='utf-8')
file_out = open("words.txt", 'w')
pattern = re.compile("[A-Z]{5}$")

words = set()
for line in word_file.readlines():
    word, description = line.split()
    if pattern.match(word):
        words.add(word)
print len(words)
for word in words:
    file_out.write("{}\n".format(word))
file_out.close()
