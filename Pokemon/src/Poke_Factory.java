//Le um arquivo de texto e cria pokemons
import java.io.*;
import java.util.Scanner; 

//Coleta nomes de Pokemons e seus ataques especiais de um arquivo de texto
public class Poke_Factory {

	int pokemonNum;
	int specMoves;
	String pokemon_names[];
	String pokemon_moves[];
	String pokemon_moves_descr[];
	int index;
	
	public Poke_Factory() {
		
		//Leitor do arquivo onde se encontram infos dos Pokemons
		
		try {
			
			Scanner scan = new Scanner(System.in);
			System.out.println("Insira caminho da pokemon list");
			String path = scan.nextLine();
			System.out.println(path);
			File file = new File(path); 
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//scan.close();
			
			//Leem numero de pokemons e numero de ataques especiais
			String st = br.readLine();
			pokemonNum = Integer.parseInt(st);
			st = br.readLine();
			specMoves = Integer.parseInt(st);
				
			pokemon_moves = new String[specMoves*pokemonNum];
			pokemon_moves_descr = new String[specMoves*pokemonNum];
			pokemon_names = new String[pokemonNum];
			
			System.out.println("Pokemons: "+pokemonNum+" Moves:"+specMoves);
			
			//Coleta os nomes dos pokemons, seus ataques e as descricoes dos ataques
			for(int i=0; i<pokemonNum;i++) {
				st = br.readLine();
				pokemon_names[i] = st;
				//System.out.println((i+1)+"º Pokemon é "+pokemon_names[i]);
			}
			for(int i=0; i<specMoves*pokemonNum;i++) {
				st = br.readLine();
				pokemon_moves[i] = st;
				//System.out.println("Especial:"+pokemon_moves[i]);
			}
			for(int i=0; i<specMoves*pokemonNum;i++) {
				st = br.readLine();
				pokemon_moves_descr[i] = st;
				//System.out.println("Especial descr:"+pokemon_moves_descr[i]);
			}
			
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
