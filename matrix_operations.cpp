//Write a Program to implement multithreading for Matrix Operations using Pthreads.

#include <iostream>
#include <thread>
#include <vector>
using namespace std;

#define MAX 3 // Define the size of the matrix

// Declare matrices
int A[MAX][MAX];
int B[MAX][MAX];
int C[MAX][MAX]; // Result matrix

// Function for matrix addition
void add_matrix(int row, int col) {
    C[row][col] = A[row][col] + B[row][col];
}

// Function for matrix subtraction
void subtract_matrix(int row, int col) {
    C[row][col] = A[row][col] - B[row][col];
}

// Function for matrix multiplication
void multiply_matrix(int row, int col) {
    C[row][col] = 0;
    for (int k = 0; k < MAX; ++k) {
        C[row][col] += A[row][k] * B[k][col];
    }
}

// Function to display a matrix
void display_matrix(int mat[MAX][MAX]) {
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            cout << mat[i][j] << " ";
        }
        cout <<  endl;
    }
}

int main() {
    // Initialize matrices A and B with sample values
    cout << "Matrix A:\n";
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            A[i][j] = i + j + (rand() % 11);
            B[i][j] = i * j + (rand() % 11);
             cout << A[i][j] << " ";
        }
        cout <<  endl;
    }

     cout << "\nMatrix B:\n";
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
             cout << B[i][j] << " ";
        }
         cout <<  endl;
    }

    // Perform Matrix Addition using multithreading
     cout << "\nMatrix Addition (A + B):\n";
     vector< thread> threads;
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            threads.emplace_back(add_matrix, i, j);
        }
    }

    // Join threads for addition
    for (auto& th : threads) {
        if (th.joinable()) {
            th.join();
        }
    }
    display_matrix(C);

    // Perform Matrix Subtraction using multithreading
     cout << "\nMatrix Subtraction (A - B):\n";
    threads.clear();
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            threads.emplace_back(subtract_matrix, i, j);
        }
    }

    // Join threads for subtraction
    for (auto& th : threads) {
        if (th.joinable()) {
            th.join();
        }
    }
    display_matrix(C);

    // Perform Matrix Multiplication using multithreading
     cout << "\nMatrix Multiplication (A * B):\n";
    threads.clear();
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            threads.emplace_back(multiply_matrix, i, j);
        }
    }

    // Join threads for multiplication
    for (auto& th : threads) {
        if (th.joinable()) {
            th.join();
        }
    }
    display_matrix(C);

    return 0;
}
