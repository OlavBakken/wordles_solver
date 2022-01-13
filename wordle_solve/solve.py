#################################################
# Programmet bygger søketre først, så kan ta en #
# stund å starte, for å indikere resultatet av  #
# en gjetning skriver du 0 for grå, 1 for gul,  #
# og 2 for grønn, for eksempel:                 #
# 0 2 1 0 0 -> grå grønn gul grå grå            #
#################################################

INITIAL_GUESS = "watch"
POSSIBLE_ANSWERS = 2315
import sys, random
GREY = 0
YELLOW = 1
GREEN = 2

def compare(guess, correct):
	_guess = list(guess)
	_correct = list(correct)
	ans = [GREY] * 5
	for i in range(5):
		if _guess[i] == _correct[i]:
			ans[i] = GREEN
			_correct[i] = '#'
	
	for i in range(5):
		for j in range(5):
			if ans[i] == GREY and _guess[i] == _correct[j]:
				ans[i] = YELLOW
				_correct[j] = '#'
	
	return tuple(ans)
	
class GuessTree:
	def __init__(self, words, guessable, name=None):
		global wordsfull
		self.leaf = False
		self.sz = len(words)
		if len(words) == 1:
			self.leaf = True
			self.name = words[0]
			return
		if not name:
			groups = {}
			for word in guessable:
				groups[word] = {}
				
			for i in guessable:
				for j in words:
					ans = compare(i, j)
					if ans not in groups[i]:
						groups[i][ans] = 0
					groups[i][ans] += 1
			
			best = []
			for word in words:
				total = 0
				for key in groups[word]:
					total += pow(groups[word][key], 2)
				best.append( (total, word) )
			best.sort()
			
			name = best[0][1]
			#print name
		
		self.name = name
		group = {}
		groupguess = {}
		for word in words:
			ans = compare(name, word)
			if ans not in group:
				group[ans] = []
			group[ans].append(word)
			
		for word in guessable:
			ans = compare(name, word)
			if ans not in groupguess:
				groupguess[ans] = []
			groupguess[ans].append(word)
		
		self.children = {}
		for key in group:
			self.children[key] = GuessTree(group[key], groupguess[key])
		
	def depth(self):
		if self.leaf:
			return 0
		return 1 + max(map(lambda key: self.children[key].depth(), self.children))

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

