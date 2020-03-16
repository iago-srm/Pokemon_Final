import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;


public class Game_Board extends Controller{

	static getInput inp = new getInput();
	
	Poke_Factory fct;
	PlayerMaker pm;
	Wild w;
	Battle_Controller bc;
	
	Player p;
	int gameMode;
	static int holdsPokemon = 2; //Com quantos Pokémon o jogador inicia a partida
	int rounds = 0; //Quantos rounds já foram
	
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

	//Executa um jogo em modo mapa: jogador vaga pelo mapa à procura de Pokémon selvagens.
	public void playGameMapa() {
		
		//Construtor pede arquivo de texto para o usuário e produz matriz com as posições.
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

		//Loop principal do jogo: jogador joga enquanto está vivo ou não desiste
		while(mapa.makeMove(inp) != 0) {
			mapa.printMapa();
			
			if(mapa.getWild()) {
				w = (Wild)pm.make_player(2);
				bc = new Battle_Controller(p,w,fct, holdsPokemon);
				bc.run();
				mapa.printMapa();
			}
			System.out.println("Selecione uma dire��o: 'w','a','s','d'\n ou 'q' para sair do jogo");
			
			
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
	
	
	

}
