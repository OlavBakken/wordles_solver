#include <iostream>
#include <Windows.h>
#include <fstream>
#include <time.h>
#include <stdio.h>
using namespace std;

#define RED    "\033[1;31;40m# "
#define YELLOW "\033[1;33;40m# "
#define GREEN  "\033[1;32;40m# "
#define RESET  "\033[1;37;40m"

int * compare(string guess, string answer);
void print_result(int* result);
bool verify(int* result);
bool EnableVTMode();
string words[20000];

int main() {
	EnableVTMode();
	srand(time(NULL));
	fstream wordlist("words.txt");
	int n = 0;
	while (!wordlist.eof()) {
		wordlist >> words[n++];
	}
	string answer = words[rand() % n];
	//cout << answer << endl;

	while (true) {
		cout << "gjett ett ord: ";
		string guess;
		cin >> guess;
		while (guess.size() != 5) {
			cout << "fem bokstaver: ";
			cin >> guess;
		}
		int* result = compare(guess, answer);
		print_result(result);
		if (verify(result)) break;
		free(result);
	}
	cout << "bra gjort!" << endl;
	cout << "trykk enter for aa avslutte: ";
	char finish;
	scanf_s("%c", &finish);
	scanf_s("%c", &finish);
}

int* compare(string guess, string answer) {
	int * res = (int *) malloc(5*sizeof(int));
	for (int i = 0; i < 5; i++) {
		res[i] = 0;
		if (guess[i] == answer[i]) {
			res[i] = 2;
			guess[i] = '#';
			answer[i] = '.';
		}
	}

	for (int i = 0; i < 5; i++) {
		for (int j = 0; j < 5; j++) {
			if (guess[i] == answer[j]) {
				res[i] = 1;
				guess[i] = '#';
				answer[j] = '.';
			}
		}
	}

	return res;
}

void print_result(int* result) {
	cout << "result: ";
	for (int i = 0; i < 5; i++) {
		switch (result[i]) {
		case 0:
			cout << RED;
			break;
		case 1:
			cout << YELLOW;
			break;
		case 2:
			cout << GREEN;
			break;
		}
	}
	cout << RESET << endl;
}

bool verify(int* result) {
	for (int i = 0; i < 5; i++) {
		if (result[i] != 2) return false;
	}

	return true;
}

bool EnableVTMode()
{
	// Set output mode to handle virtual terminal sequences
	HANDLE hOut = GetStdHandle(STD_OUTPUT_HANDLE);
	if (hOut == INVALID_HANDLE_VALUE)
	{
		return false;
	}

	DWORD dwMode = 0;
	if (!GetConsoleMode(hOut, &dwMode))
	{
		return false;
	}

	dwMode |= ENABLE_VIRTUAL_TERMINAL_PROCESSING;
	if (!SetConsoleMode(hOut, dwMode))
	{
		return false;
	}
	return true;
}