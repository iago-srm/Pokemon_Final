import java.util.Random;

public class Player {

	int num;
	getInput inp = new getInput();
	Mapa mapa;
	
	int heals = 5; //itens de cura disponíveis para este jogador
	int holdsPokemon;
	
	String pokemon_names[] = new String[6];
	Pokemon pokemons[] = new Pokemon[6];
	String pokemon_moves[];
	String pokemon_moves_descr[];
	
	int specMoves;
	int pokemon_ativo;
	int living_pokemon;
	int x,y;
	boolean grass;
	
	Battle_Controller bc;
	
	public void setInitialPosition(Mapa mapa) {
		Random rand = new Random();
		this.mapa = mapa;
		this.x = rand.nextInt(mapa.mapa_cols);
		this.y = rand.nextInt(mapa.mapa_rows);
		if(mapa.mapa[x][y] == '/') {
			this.grass = true;
		}else {
			this.grass = false;
		}
		
		System.out.println("Jogador "+num+" @ ("+x+","+y+")");
	}
	
	public void updatePosition(char choice) {
		switch(choice) {
		
		case 'a':
			y--;
			break;
		case 'w':
			x--;
			break;
		case 's':
			x++;
			break;
		case 'd':
			y++;
			break;
		}
		
		if(mapa.mapa[x][y] == '/') {
			grass = true;
		} else {
			grass = false;
		}
		
		//System.out.println("p("+x+","+y+")");
		//System.out.println("Grass: "+grass);
	}
	
	public void trocarPokemon() {
		int ok = 0;
		int choice = 0;
		
		if(living_pokemon == 1) {
			System.out.println("Você não tem Pokémon para trocar.");
			return;
		}
		
		System.out.println("Jogador "+num+", selecione seu novo Pokémon:");
		Pokemon p;
		
		for(int i=0; i<living_pokemon; i++) {
			p = pokemons[i];
			System.out.println(i+") "+p.nome+"; hp = "+p.hp);
		}
		while(ok != 1) {
			choice = inp.result_int(0,living_pokemon-1);
			if(choice == pokemon_ativo) {
				System.out.println("Este já é seu Pokemón ativo");
			} else
				ok=1;
		}
		pokemon_ativo = choice;
		System.out.println("Seu Pokémon ativo agora é "+pokemon_names[pokemon_ativo]);
		
	}
	
	public String usarItem(int heal) {
		
		System.out.println("Jogador "+num+", deseja utilizar cura em qual Pokémon?");
		Pokemon p;
		int choice;
		for(int i=0; i<living_pokemon; i++) {
			p = pokemons[i];
			System.out.println(i+" "+p.nome+"; hp = "+p.hp);
		}
		choice = inp.result_int(0,living_pokemon-1);
		p = pokemons[choice];
		p.heal(heal);
		heals--;
		return p.nome;
	}
	
	//Verifica se todos os Pokémon estão vivos e, se tiver havido mortes, elicita
	// um novo pokemon ativo do usuario
	public int checkPokemon() {

		//int mortes = 0;
		for(int i=0; i<living_pokemon; i++) {
			
			if (pokemons[i].hp <= 0){
				if(num == 2) {
					System.out.println("O Pokémon selvagem morreu");
				}else {
					System.out.println("Jogador "+num+", seu "+pokemons[i].nome+
						" morreu.");
				}
				living_pokemon--;
				if(living_pokemon > 0) {
					System.out.println("Selecione um novo Pokémon ativo:");
					pokemons[i] = pokemons[living_pokemon];
					for(int j=0;j<living_pokemon;j++) {
						System.out.println(j+") "+pokemons[j].nome+"; hp = "+
								pokemons[j].hp);
	
					}
					pokemon_ativo = inp.result_int(0,living_pokemon-1);
				}else {
					if(num != 2) {
						System.out.println("Todos os seus Pokemon estão mortos");
					}
					return 0;

				}
				
			}
		}
		return 1;

		
	}
	
	//Verifica se não perdeu o jogo
	public int checkLost() {
		
		int lost = 0;
		
		for(int i=0; i<living_pokemon; i++) {
			if (pokemons[i].hp <= 0){
				if (living_pokemon-1 == 0)
					lost++;
				}
		}
		return lost;
		
	}
	
	//Seleciona o Pokémon ativo e devolve seu ataque selecionado
	public Attack getAttack() {
		Pokemon ativo = pokemons[pokemon_ativo];
		System.out.println("Jogador "+num+", escolha o ataque de seu "+ativo.nome+":");
		return ativo.getAttack();
	}
	
	//Seleciona o Pokémon ativo e o ataca
	public void getAttacked(Attack attck) {
		Pokemon ativo = pokemons[pokemon_ativo];
		ativo.getAttacked(attck);
		
	}
	
	public String getNomeAtivo() {
		return pokemon_names[pokemon_ativo];
	}
	
	public void printEstadoAtual() {
		System.out.println("Jogador "+num+" tem "+heals+" itens de cura.");
		Pokemon p;

		for(int i=0; i<living_pokemon; i++) {
			if(i == pokemon_ativo) {
				p = pokemons[i];
				System.out.println("(ativo) "+p.nome+"; hp = "+p.hp);
			}
			else {
				p = pokemons[i];
				System.out.println(i+" "+p.nome+"; hp = "+p.hp);
			}
		}
		
	}
	
	public void printJogadas(String... opcoes) {
		
		for(int i=0;i<opcoes.length;i++) {
			System.out.println(i+") "+opcoes[i]);
		}
		
	}
	
	public int getJogada(String... opcoes) {
		System.out.println("Jogador "+num+", selecione sua jogada");
		printJogadas(opcoes);
		
		return inp.result_int(0,opcoes.length-1);
	}
	
	public void makePokemon() {
		for(int i=0; i<holdsPokemon; i++) {
			pokemons[i] = new Pokemon(pokemon_names[i],pokemon_moves[2*i],pokemon_moves[2*i+1],
					pokemon_moves_descr[2*i],pokemon_moves_descr[2*i+1]);
		}
	}
	
	public Player(int specMoves,int player, int holdsPokemon) {
		pokemon_moves = new String[holdsPokemon*specMoves];
		pokemon_moves_descr = new String[holdsPokemon*specMoves];
		this.specMoves = specMoves;
		this.num = player;
		this.holdsPokemon = holdsPokemon;
		this.living_pokemon = holdsPokemon;
	}
	
}
