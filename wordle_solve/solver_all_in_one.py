#################################################
# Programmet bygger s�ketre f�rst, s� kan ta en #
# stund � starte, for � indikere resultatet av  #
# en gjetning skriver du 0 for gr�, 1 for gul,  #
# og 2 for gr�nn, for eksempel:                 #
# 0 2 1 0 0 -> gr� gr�nn gul gr� gr�            #
#################################################

from library import *
POSSIBLE_ANSWERS = 2315
INITIAL_GUESS = "watch"

wordsfull = [word[:-1] for word in open("wordlist.txt").readlines()]
wordsans = wordsfull[:POSSIBLE_ANSWERS]
#guesswords = [word[:-1].lower() for word in sys.stdin.readlines()]

#guesswords = wordsans
#if len(sys.argv) > 1:
#	initial_guess = sys.argv[1]

tree = GuessTree(wordsans, wordsfull, INITIAL_GUESS) #raise, adieu, watch
depth = tree.depth()
total = 0

node = tree
while True:
        print node.name
        result = tuple([int(x) for x in raw_input().split()])
        if result == (2, 2, 2, 2, 2):
                print "success!"
                break
        node = node.children[result]

