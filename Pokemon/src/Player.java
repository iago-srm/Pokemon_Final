import java.util.*;
import java.util.Random;

public class Player {

	int num;
	getInput inp = new getInput();
	Mapa mapa;
	
	static int valorCura = 5;
	static int wildPokemon = 2;

	Map<String, Integer> itens = new HashMap<String, Integer>() {{
        put("heal", 5); //Jogador tem 5 itens de cura
    }};
	int holdsPokemon; //Com quantos Pokémon o jogador começa
	
	String pokemon_names[] = new String[6];
	Pokemon pokemons[] = new Pokemon[6];
	String pokemon_moves[];
	String pokemon_moves_descr[];
	
	int specMoves;
	int pokemon_ativo; //Índice da lista de Pokémon em que se encontra o ativo
	int living_pokemon; //Quantos Pokémon vivos ainda restam
	int x,y; //Coordenadas atuais
	boolean grass; //Diz se o jogador está sobre a grama
	
	Battle_Controller bc;
	
	//Insere o jogador numa posição inicial aleatória
	public void setInitialPosition(Mapa mapa) {
		Random rand = new Random();
		this.mapa = mapa;
		this.x = rand.nextInt(mapa.mapa_cols);
		this.y = rand.nextInt(mapa.mapa_rows);
		if(mapa.mapa[x][y] == Mapa.gramaSymbol) {
			this.grass = true;
		}else {
			this.grass = false;
		}
		
		System.out.println("Jogador "+num+" @ ("+x+","+y+")");
	}
	
	//Recebe a direção escolhida e atualiza o mapa e a posição do jogador
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
		
		if(mapa.mapa[x][y] == Mapa.gramaSymbol) {
			grass = true;
		} else {
			grass = false;
		}
		
		//System.out.println("p("+x+","+y+")");
		//System.out.println("Grass: "+grass);
	}
	
	//Checa se é possível trocar de Pokémon, prompta pela opção e realiza a troca
	public void trocarPokemon() {
		int ok = 0;
		int choice = 0;
		
		if(living_pokemon == 1) {
			System.out.println("Voc� n�o tem Pok�mon para trocar.");
			return;
		}
		
		System.out.println("Jogador "+num+", selecione seu novo Pok�mon:");
		Pokemon p;
		
		for(int i=0; i<living_pokemon; i++) {
			p = pokemons[i];
			System.out.println(i+") "+p.nome+"; hp = "+p.hp);
		}
		while(ok != 1) {
			choice = inp.result_int(0,living_pokemon-1);
			if(choice == pokemon_ativo) {
				System.out.println("Este j� � seu Pokem�n ativo");
			} else
				ok=1;
		}
		pokemon_ativo = choice;
		System.out.println("Seu Pok�mon ativo agora � "+pokemon_names[pokemon_ativo]);
		
	}
	
	private String usarCura(){
		
		System.out.println("Jogador "+num+", deseja utilizar cura em qual Pok�mon?");
		Pokemon p;
		
		for(int i=0; i<living_pokemon; i++) {
			p = pokemons[i];
			System.out.println(i+" "+p.nome+"; hp = "+p.hp);
		}

		int choice = inp.result_int(0,living_pokemon-1);

		p = pokemons[choice]; //pega o Pokémon escolhido
		p.heal(valorCura); //Cura o Pokémon
		itens.put("heal", itens.get("heal") - 1); //Decrementa itens disponíveis
		return p.nome; 
	}

	public String usarItem() {
		
		System.out.println("Jogador "+num+", deseja utilizar qual item?");
		int i = 0;
		for (Map.Entry<String,Integer> entry : itens.entrySet())  {
			
			System.out.println(i+ ") " +entry.getKey() + 
							 " : " + entry.getValue()); 
			i++;
		}

		int choice = inp.result_int(0,itens.size()-1);
		switch(choice){
			case 0:
				return usarCura();
			
		}

		return null;
	}
	
	//Verifica se todos os Pok�mon est�o vivos e, se tiver havido mortes, elicita
	// um novo pokemon ativo do usuario
	public int checkPokemon() {

		//int mortes = 0;
		for(int i=0; i<living_pokemon; i++) {
			
			if (pokemons[i].hp <= 0){
				if(num == wildPokemon) {
					System.out.println("O Pok�mon selvagem morreu");
				}else {
					System.out.println("Jogador "+num+", seu "+pokemons[i].nome+
						" morreu.");
				}
				living_pokemon--;
				if(living_pokemon > 0) {
					System.out.println("Selecione um novo Pok�mon ativo:");
					pokemons[i] = pokemons[living_pokemon];
					for(int j=0;j<living_pokemon;j++) {
						System.out.println(j+") "+pokemons[j].nome+"; hp = "+
								pokemons[j].hp);
	
					}
					pokemon_ativo = inp.result_int(0,living_pokemon-1);
				}else {
					if(num != wildPokemon) {
						System.out.println("Todos os seus Pokemon est�o mortos");
					}
					return 0;

				}
				
			}
		}
		return 1;

		
	}
	
	//Verifica se n�o perdeu o jogo
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
	
	//Seleciona o Pok�mon ativo e devolve seu ataque selecionado
	public Attack getAttack() {
		Pokemon ativo = pokemons[pokemon_ativo];
		System.out.println("Jogador "+num+", escolha o ataque de seu "+ativo.nome+":");
		return ativo.getAttack();
	}
	
	//Seleciona o Pok�mon ativo e o ataca
	public void getAttacked(Attack attck) {
		Pokemon ativo = pokemons[pokemon_ativo];
		ativo.getAttacked(attck);
		
	}
	
	public String getNomeAtivo() {
		return pokemon_names[pokemon_ativo];
	}
	
	public void printEstadoAtual() {
		System.out.println("Jogador "+num+" tem "+itens.get("heal")+" itens de cura.");
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
