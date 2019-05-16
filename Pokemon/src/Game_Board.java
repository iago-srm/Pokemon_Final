import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

class Mapa{

	Random rand = new Random();
	int mapa_rows = 16;
	int mapa_cols = 16;
	Player p;
	int xPos, yPos;
	char [][] mapa = new char [mapa_rows][mapa_cols];
	
	public void printMapa() {
		char antes = mapa[xPos][yPos];
		mapa[xPos][yPos] = 'X';
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
	
	public boolean getWild() {
		boolean appears = false;
		if(p.grass == true) {
			int r = rand.nextInt(10);
			if(r > 7) {
				System.out.println("A wild Pokémon appears!");
				
				appears = true;
			}
		}
		return appears;
	}
	public int makeMove(getInput inp) {
		
		String input = "a";
		String[] possibles = {"a","w","s","d","q"};
		
		input = inp.result_String(possibles);
		//System.out.println("Escolhido: "+input);
		//System.out.println("m("+xPos+","+yPos+")");
		int ret = 1;
		
		char charInput;
		charInput = input.charAt(0);
		
		switch(charInput) {
		
		case 'q':
			ret = 0;
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
		//System.out.println("m("+xPos+","+yPos+")");
		
		return ret;
	}
	
	public Mapa() {

		Scanner scan = new Scanner(System.in);
		System.out.println("Insira caminho (absoluto) do mapa");
		String path = scan.nextLine();
		System.out.println(path);
		File file = new File(path); 
		//BufferedReader br = new BufferedReader(new FileReader(file));
		
		try {	
		
			BufferedReader br = new BufferedReader(new FileReader(file));
			
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

public class Game_Board extends Controller{

	static getInput inp = new getInput();
	
	Poke_Factory fct;
	PlayerMaker pm;
	Wild w;
	Battle_Controller bc;
	
	Player p;
	int gameMode;
	static int holdsPokemon = 2;
	int rounds = 0;
	
	
	/*
	class Wild extends Player{

		int specMoves;
		Random rand = new Random();
		
		
		public Wild(int specMoves, int player, int holdsPokemon) {
			
			super(specMoves, 2, 1);
			
			this.specMoves = specMoves;
			heals = 0;
			
			//pm.getPokemonNames()
			/*
			int pokemonNum = rand.nextInt(fct.pokemonNum);
			
			pokemon_names[0] = fct.pokemon_names[pokemonNum];
			
			pokemon_moves[0] = fct.pokemon_moves[pokemonNum];
			pokemon_moves[1] = fct.pokemon_moves[pokemonNum+1];
			
			pokemon_moves_descr[0] = fct.pokemon_moves_descr[pokemonNum];
			pokemon_moves_descr[1] = fct.pokemon_moves_descr[pokemonNum+1];
			
			makePokemon();
			pokemon_ativo = 0;
			
		}
		
		@Override
		public int getJogada(String...opcoes) {
			return 3;
		}
		
		@Override
		public Attack getAttack() {
			Pokemon ativo = pokemons[pokemon_ativo];
			int selected = rand.nextInt(2+specMoves);
			return ativo.getAttack(selected);
		}
	}

	*/

	public void playGameMapa() {
		
		Mapa mapa = new Mapa();
		
		System.out.println("Bem-vindo ao modo de mapa\n");
		
		p = pm.make_player(0);
		p.setInitialPosition(mapa);
		mapa.insertPlayer(p);
		
		System.out.println("\n***RODADA "+rounds+"***");
		
		if(rounds == 0) 
			System.out.println("Que comece a aventura!");

		mapa.printMapa();
		System.out.println("Selecione uma direção: 'w','a','s','d'\n ou 'q' para sair do jogo");

		while(mapa.makeMove(inp) != 0) {
			mapa.printMapa();
			if(mapa.getWild()) {
				w = (Wild)pm.make_player(2);
				bc = new Battle_Controller(p,w,fct, holdsPokemon);
				bc.run();
				mapa.printMapa();
			}
			System.out.println("Selecione uma direção: 'w','a','s','d'\n ou 'q' para sair do jogo");
			
			
		}
	}
	
	class Set_Game_PvP extends Event{

		public Set_Game_PvP(long eventTime) {
			super(eventTime);
		}

		@Override
		public void action() {
			Battle_Controller bc = new Battle_Controller(fct, holdsPokemon);
			bc.run();
		}

		@Override
		public String description() {
			return "Fim do jogo PvP";
		}
	}
	
	public Game_Board() {
		fct = new Poke_Factory();
		pm = new PlayerMaker(fct, holdsPokemon);
		System.out.println("Selecione o modo de jogo:\n0) Mapa \n1) PvP");
		gameMode = inp.result_int(0,1);
		
		switch(gameMode) {
		case 0:
			playGameMapa();
			break;
		case 1: 
			addEvent(new Set_Game_PvP(System.currentTimeMillis()));
			break;
		}
	}
	
	public static void main(String[] args) {
		Game_Board game = new Game_Board();
		game.run();
		/*
		Scanner scan = new Scanner(System.in);
		String path = scan.nextLine();
		System.out.println("Path is"+path);
		scan.close();
		*/
	}

}
