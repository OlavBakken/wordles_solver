from library import *
import sys
if len(sys.argv) < 2:
    print "abort"

POSSIBLE_ANSWERS = 2315
INITIAL_GUESS = "raise"
WORDLIST = "wordlist.txt"
SEARCH_TREE_TARGET = open(sys.argv[1], 'w')

wordsfull = [word[:-1] for word in open(WORDLIST).readlines()]
wordsans = wordsfull[:POSSIBLE_ANSWERS]

tree = GuessTree(wordsans, wordsfull, INITIAL_GUESS) #raise, adieu, watch
depth = tree.depth()

queue = [tree]
while queue:
    node = queue.pop()
    for key in node.children:
        SEARCH_TREE_TARGET.write("{} {} {}\n".format(node.name, key, node.children[key].name))
        queue.append(node.children[key])

