import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class Mapa{

	static char playerSymbol = 'X';
	static char gramaSymbol = '/';
	
	static int wildPokemonOdds = 5;
    Random rand = new Random();
	getInput inp = new getInput();

	int mapa_rows = 16; //Dimensão y do mapa
    int mapa_cols = 16; //Dimensão x do mapa
    
	Player p; //Jogador (único) que está jogando este mapa
    int xPos, yPos;
    
	char [][] mapa = new char [mapa_rows][mapa_cols]; //Matriz que é atualizada a cada movimento e printada na tela
	
	//Printa o estado atual do mapa
	public void printMapa() {

		char antes = mapa[xPos][yPos];
		
		mapa[xPos][yPos] = playerSymbol;
		
		System.out.println("MAPA DO JOGO");
		
		for(int i = 0; i< mapa_rows; i++) {
			for(int j = 0; j< mapa_cols; j++) {
				System.out.print(mapa[i][j]);
			}
			System.out.println();
		}
		
		mapa[xPos][yPos] = antes;
	}
	
	public void insertPlayer(Player p) {
		xPos = p.x;
		yPos = p.y;
		this.p = p;
	}
	
    //Se o jogador estiver sobre a grama, pode aparecer um Pokemon Selvagem.
    //Retorna um boolean se apareceu um ou não
	public boolean getWild() {
		boolean appears = false;
		if(p.grass == true) {
			int r = rand.nextInt(100);
			if(r > 100-wildPokemonOdds) {
				System.out.println("A wild Pok�mon appears!");
				
				appears = true;
			}
		}
		return appears;
	}

	//Obtém input do próximo movimento do usuário.
	private char getDirection(){
		
		String input = "a";
		String[] possibles = {"a","w","s","d","q"};
		
		input = inp.result_String(possibles);
		
		
		return input.charAt(0);
		
	}

	//Obtém input do próximo movimento do usuário e atualiza mapa.
	public int makeMove(getInput inp) {
		
		//Flag que indica se o jogador deu quit ou não.
		int continua = 1;
		
		char charInput = getDirection();
		
		switch(charInput) {
		
			case 'q':
			continua = 0;
				break;
			case 'a':
				if(yPos > 0) {
					yPos--;
					p.updatePosition(charInput);
				}
				break;
			case 'w':
				if(xPos > 0) {
					xPos--;
					p.updatePosition(charInput);
				}
				break;
			case 's':
				if(xPos < mapa_rows-1) {
					xPos++;
					p.updatePosition(charInput);
				}
				break;
			case 'd':
				if(yPos < mapa_cols-1) {
					yPos++;
					p.updatePosition(charInput);
				}
				break;
		}
		
		return continua;
	}
	
	//Obtém arquivo de texto com o mapa a partir do caminho absoluto informado pelo usuário.
	private File promptArquivo(){
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Insira caminho (absoluto) do mapa");
		String path = scan.nextLine();
		scan.close();
		
		return new File(path); 
		
	}

    //Construtor cria a matrix mapa a partir de um arquivo de texto
	public Mapa() {

		File file = promptArquivo();

		try {	
		
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//Lê cada linha do arquivo de texto, a coloca em uma array de chars, e insere os chars no atributo mapa.
			String st = br.readLine();
			int i=0;
			char[] t;
			while(st != null) {
				
				t = st.toCharArray();
				
				for(int j=0; j<mapa_cols;j++) {
					mapa[i][j] = t[j];
				}
				i++;
				st = br.readLine();
			}
		
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		


	}
}
