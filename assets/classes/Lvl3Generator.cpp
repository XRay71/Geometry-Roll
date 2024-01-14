/*
Rain Yeyang
Date: June 21, 2022
Generates the wave terrain for level 3 of Geometry Roll
Do not run this file
*/
#include <bits/stdc++.h>
using namespace std;

const int MAX_H = 12;
const int MAX = 1000;

int main()
{

	// create file
	ofstream file;
	// file.open("lvl3.txt");

	char arr[100][MAX];

	// pad arr with spaces
	for (int i = 0; i < 15; ++i)
	{
		for (int j = 0; j < MAX; ++j)
		{
			arr[i][j] = ' ';
		}
	}

	// starting indices
	int i = 0;
	int j = 1;
	bool up = true; // direction (upside down)
	int cur;		// "steps" up or down, to be added

	// seed for rand()
	srand(time(NULL));
	while (true)
	{
		cur = rand() % 9 + 2;
		if (up)
		{
			for (int k = 0; k < cur; ++k)
			{
				// break if over limit
				if (i + k > MAX_H)
				{
					break;
				}
				// upper bound of "corridor"
				arr[i][j] = '0';
				arr[i + 1][j] = '6';
				// lower bound of "corridor"
				arr[i + 4][j] = '3';
				arr[i + 5][j] = '0';
				i++;
				j++;
			}
			arr[i][j] = '0';
			arr[i + 1][j] = '6';
			arr[i + 4][j] = '3';
			arr[i + 5][j] = '0';
			j++;
		}
		else
		{
			for (int k = 0; k < cur; ++k)
			{
				// break if over limit
				if (i - k < 0)
				{
					break;
				}
				// upper bound of "corridor"
				arr[i][j] = '0';
				arr[i + 1][j] = '4';
				// lower bound of "corridor"
				arr[i + 4][j] = '5';
				arr[i + 5][j] = '0';
				i--;
				j++;
			}
			arr[i][j] = '0';
			arr[i + 1][j] = '4';
			arr[i + 4][j] = '5';
			arr[i + 5][j] = '0';
			j++;
		}
		// flip direction
		up = !up;

		// break if over limit
		if (j >= MAX)
		{
			break;
		}
	}

	// write characters to text file
	for (int i = 0; i < 15; ++i)
	{
		for (int j = 0; j < MAX; ++j)
		{
			file << arr[i][j];
		}
		file << '\n';
	}
	file.close();

	return 0;
}
