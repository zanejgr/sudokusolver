
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuSolver {

	public static int [][] puzzle = new int [9][9];
	
	public static void main(String[] args) {
		//Display friendly greeting
		System.out.println("   _____ _    _ _____   ____  _  ___   "
				+ " _    _____ "+
					" ____  _ __      ________ _____\n"+  
					"  / ____| |  | |  __ \\ / __ \\| |/ / |  | "+
					"|  / ____|/ __ \\| |\\ \\    / /  ____|  __ \\\n" +
					" | (___ | |  | | |  | | |  | | ' /| |  | | | (___ | |  |"
					+ " | | \\ \\  / /| |__  | |__) |\n"+
					"  \\___ \\| |  | | |  | | |  | |  < | |  | |  \\___"
					+ " \\| |  | | |  \\ \\/ / |  __| |  _  / \n"+
					"  ____) | |__| | |__| | |__| | . \\| |__| |  ____)"
							+ " | |__|"+""
					+ " | |___\\  /  | |____| | \\ \\\n"+ 
					" |_____/ \\____/|_____/ \\____/|_|\\_\\\\____/ "
							+ " |_____/ \\____/|"+
					"______\\/   |______|_|  \\_\\\n");
		
		//declare and initialize scanners, plus a file to be read
		Scanner puzzleScanner = new Scanner("");
		File file;
		Scanner in = new Scanner(System.in);
		
		
		if(args.length == 1){
			file = new File(args[0]);
			try{
				puzzleScanner = new Scanner(file);
			}catch(FileNotFoundException e){
				System.out.println("Invalid File");
				System.exit(0);
			}
		}
		else if(args.length !=0){
			System.out.println("Usage: SudokuSolver file");
		}
		else{
			Boolean isValid;
		
			do{
				isValid = true;
				System.out.print("Enter filename containing a sudoku puzzle:");
				try{
					file = new File(in.next());
					puzzleScanner = new Scanner(file);
				}catch(FileNotFoundException e){
					System.out.println("Invalid file");
					isValid = false;
				}
			}while(!isValid);
		}
		
		//copy contents of file to the puzzle array
		System.out.println("Reading file...");
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				String temp = puzzleScanner.next();
				if(temp.equals("*")){
					puzzle[i][j] = 0;
				}
				else{
					puzzle[i][j] = Integer.parseInt(temp);
				}
			}
		}

		//output the unsolved puzzle
		printPuzzle();
		System.out.println("File Read!");
		
		System.out.println("Attempting to solve...");
		
		long t1 = System.currentTimeMillis();
		
		//Print solved puzzle. If no solution exists, indicate so to the user
		if(solvePuzzle()){
			System.out.println("Solution Found!");
			printPuzzle();
		}else{
			System.out.println("No solution exists.");
		}
		
		long t2 = System.currentTimeMillis();
		
		System.out.println("Time elapsed:"+(t2-t1)/1000.0+"seconds.");
	}
	private static void printRow(int rowIndex){
	/*Contract: print the given row of the array, formatted to match a 
	sudoku puzzle.
	Conditions: the row must be a valid index of puzzle*/
		System.out.print("\u2551");
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 2; i++){
				if(puzzle[rowIndex][j*3+i]==0){
					System.out.print(" ");
				}else{
					System.out.print(puzzle[rowIndex][j*3+i]);
				}
				System.out.print("\u2502");
			}
			if(puzzle[rowIndex][j*3+2]==0){
					System.out.print(" ");
				}else{
					System.out.print(puzzle[rowIndex][j*3+2]);
				}
			System.out.print("\u2551");
		}
		
		System.out.println();
	}
	private static void printPuzzle() {
	/*Contract: Print the array, formatted as a sudoku puzzle
	*/
		System.out.println("\u2554\u2550\u2564\u2550\u2564\u2550\u2566\u2550"+
			"\u2564\u2550\u2564\u2550\u2566\u2550\u2564\u2550\u2564\u2550"+
			"\u2557");
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				printRow(i*3+j);
				
				System.out.println("\u255f\u2500\u253c\u2500\u253c"+
					"\u2500\u256b\u2500\u253c"+
					"\u2500\u253c\u2500\u256b\u2500\u253c\u2500\u253c"+
					"\u2500\u2562");
			}
			printRow(i*3+2);
			System.out.println("\u2560\u2550\u256a\u2550\u256a\u2550"+
				"\u256c\u2550\u256a"+
				"\u2550\u256a\u2550\u256c\u2550\u256a\u2550\u256a\u2550"+
				"\u2563");
		}
		
		for(int j = 0; j < 2; j++){
			printRow(6+j);
			
			System.out.println("\u255f\u2500\u253c\u2500\u253c\u2500\u256b"+
				"\u2500"+
				"\u253c\u2500\u253c\u2500\u256b\u2500\u253c\u2500\u253c"+
				"\u2500\u2562");
		}
		printRow(8);
		System.out.println("\u255a\u2550\u2567\u2550\u2567\u2550\u2569"+
			"\u2550\u2567\u2550"+
			"\u2567\u2550\u2569\u2550\u2567\u2550\u2567\u2550\u255d");
	}

	public static boolean isCompletePuzzle(){
		/*Contract: Returns true if the puzzle has no more empty spaces*/
		boolean result = true;
		for(int [] i : puzzle)
			for(int j : i)
				if(j==0)
					result = false;
		return result;
	}
	
	public static boolean isValidPuzzle(){
		/*Contract: returns true if the puzzle follows the rules of sudoku
		Conditions: All elements of the array must be valid*/
		boolean result = true;	//may become false as it trickles thru checks
		//check rows for duplicates
		for(int row = 0; row < 9; row++){
			for(int i = 0; i < 9; i++){
				for(int j = i+1; j < 9; j++){
					if(puzzle[row][i] == puzzle[row][j] && puzzle[row][i]!=0){
						result = false;
					}
				}
			}
		}
		//check columns for duplicates
		for(int column = 0; column < 9; column++){
			for(int i = 0; i < 9; i++){
				for(int j = i+1; j < 9; j++){
					if(puzzle[i][column] == puzzle[j][column] 
							&& puzzle[i][column]!=0){
						result = false;
					}
				}
			}
		}
		
		//check for duplicates in a region
		for(int i = 0; i < 9; i++){	
			if(checkRegion(i)){	
				result = false;
			}
		}
		
		return result;
	}
	
	public static boolean checkRegion(int num){
		/*Contract: return true if the region specified contains nonzero
			duplicates
		Conditions: number less than or equal to the number of regions in the puzzle*/
		boolean flag = false;
		int [] region = new int [9];
		//fill the array for the region
		int k = 0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				region[k] = puzzle[num%3*3+i][num/3*3+j];
				k++;
			}
		}
		//iterate across and check for duplicates
		for(int i = 0; i < 9; i++){
			for(int j = i+1; j < 9; j++){
				if(region[i]==region[j]&&region[i]!=0){
					flag = true;
				}
			}
		}
		return flag;
	}
	
	public static boolean isSolvedPuzzle(){
		return (isValidPuzzle()&&isCompletePuzzle());		
	}
	
	public static boolean solvePuzzle(){
		/*Contract: fills blanks in an unsolved sudoku puzzle*/
		if(!isValidPuzzle()){
			return false;
		}
		if(isSolvedPuzzle()){
			return true;
		}
		
		int [] indices = findNextBlank();
		
		for(int i = 1; i <= 9; i++){
			puzzle[indices[0]][indices[1]] = i;
			if(solvePuzzle()){
				return true;
			}
		}
		
		puzzle[indices[0]][indices[1]] = 0;
		return false;
	}

	private static int[] findNextBlank() {
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(puzzle[i][j]==0){
					return new int [] {i,j};
				}
			}
		}
		return new int [0];
	}
}
