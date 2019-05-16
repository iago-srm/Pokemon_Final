import java.util.Random;

public class PlayerMaker {

		Poke_Factory fct;
		Random rand = new Random();
		int holdsPokemon;
		getInput inp = new getInput();
		
		public PlayerMaker(Poke_Factory fct, int holds) {
			this.fct = fct;
			this.holdsPokemon = holds;
		}
		
		
		public void printPokemonOptions() {
			for(int i=0; i<fct.pokemonNum;i++) {
				System.out.println(i+") "+fct.pokemon_names[i]);
			}
		}
		
		//Descreve pokemons que os jogadores possuem antes do início da batalha
		public void description(Player p) {
			int num = p.num;
			if(p.num != 2) {
				
				System.out.println("Jogador "+num+" tem:");
			}else {
				System.out.println("Pokémon selvagem:");
			}
			for(int i=0;i<p.holdsPokemon;i++) {
				System.out.println("\n");
				Pokemon po = p.pokemons[i];
				System.out.println(po.nome.toUpperCase());
				for(int j=0; j<p.specMoves+2;j++) {
					Attack t = po.attacks[j];
					System.out.println(t.nome+": "+t.descricao);
				}
				
			}
			System.out.println("Jogador "+num+", seu Pokémon ativo é "+
				p.pokemon_names[p.pokemon_ativo]);	
		}
		
		//Pega informações dos Pokémon contidas na Factory e as coloca no objeto player
		public void getPokemonNames(Player p) {
			if(p.num != 2) {
				System.out.println();
				System.out.println("Jogador "+p.num+", selecione "+holdsPokemon+" Pokemons");
			}	
				int choice;
			
				for(int i=0,j=0; i<p.holdsPokemon; i++,j+=2) {
					
					if(p.num != 2) {
						System.out.println("Jogador "+p.num+", selecione o "+(i+1)+"º Pokemon");
						printPokemonOptions();
						
						choice = inp.result_int(0,fct.pokemonNum-1);
					}else {
						choice = rand.nextInt(fct.pokemonNum);
					}
					p.pokemon_names[i] = fct.pokemon_names[choice];
					p.pokemon_moves[j] = fct.pokemon_moves[2*choice];
					p.pokemon_moves[j+1] = fct.pokemon_moves[2*choice+1];
					p.pokemon_moves_descr[j] = fct.pokemon_moves_descr[2*choice];
					p.pokemon_moves_descr[j+1] = fct.pokemon_moves_descr[2*choice+1];
					
				}
				p.makePokemon();
				
		}
		
		
		public Player make_player(int num) {
			
			Player p;
			
			if(num == 2)
				p = new Wild(fct.specMoves,num,holdsPokemon);
			else
				p = new Player(fct.specMoves,num,holdsPokemon);
			
			
			getPokemonNames(p);
			
			//Elicita o Pokémon ativo, se necessário
			if(p.holdsPokemon > 1) {
				int choice;
				System.out.println("Jogador "+num+", selecione seu Pokemon ativo:");
				
				for(int i=0; i<holdsPokemon; i++) {
					System.out.println(i+") "+p.pokemon_names[i]);
				}
				
				choice = inp.result_int(0,holdsPokemon-1);
				p.pokemon_ativo = choice;
			}else {
				p.pokemon_ativo = 0;
			}
			
			description(p);
			
			return p;
			
		}
}
	

