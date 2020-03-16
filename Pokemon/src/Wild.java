import java.util.Random;

public class Wild extends Player {

	Random rand = new Random();
	
	public Wild(int specMoves, int player, int holdsPokemon) {
		super(specMoves, 2, 1);
		
		valorCura = 0;
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
