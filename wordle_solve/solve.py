#################################################
# For aa indikere resultatet av                 #
# en gjetning skriver du 0 for graa, 1 for gul, #
# og 2 for groenn, for eksempel:                #
# 0 2 1 0 0 -> graa groenn gul graa graa        #
#################################################

from library import *
import sys, re
if len(sys.argv) < 2:
    print "abort"
SEARCH_TREE = open(sys.argv[1])

initial_guess = SEARCH_TREE.readline()[:-1]

pattern = re.compile("([a-z]{5}) (\(\d, \d, \d, \d, \d\)) ([a-z]{5})\n")
pattern2 = re.compile("\((\d), (\d), (\d), (\d), (\d)\)")
search_tree = {}
for line in SEARCH_TREE.readlines():
    match = pattern.match(line)
    parent = match.group(1)
    edge = tuple([int(x) for x in pattern2.match(match.group(2)).groups()])
    child = match.group(3)
    if parent not in search_tree:
        search_tree[parent] = {}
    search_tree[parent][edge] = child

node = initial_guess
while True:
        print node
        result = tuple([int(x) for x in raw_input().split()])
        if result == (2, 2, 2, 2, 2):
                print "success!"
                break
        node = search_tree[node][result]

