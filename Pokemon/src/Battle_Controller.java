import java.util.Random;
import java.util.Scanner;

/*
class SetupGame extends Event{

	long evt;
	Battle_Controller bc;
	int gameMode;
	
	public SetupGame(long eventTime, Battle_Controller bc, int gameMode) {
		
		super(eventTime);
		evt = eventTime;
		this.bc = bc;
		this.gameMode = gameMode;
	}

	@Override
	public void action() {
		if(gameMode == 0) {
			bc.addEvent(bc.new Make_Player(evt,0,bc,bc.holdsPokemon));
			bc.addEvent(bc.new Make_Player(System.currentTimeMillis(),1,bc,bc.holdsPokemon));
			bc.gameOn = 1;
			bc.addEvent(bc.new Rodada(System.currentTimeMillis(),bc.players[0],bc.players[1]));
		}else {
			
		}
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Produzir jogadores";
	}


}

*/

public class Battle_Controller extends Controller{
	
	static Poke_Factory fct;
	Player p0;
	Player p1;
	Player[] p = {p0,p1};
	int rounds = 0;
	int battle_on = 1;
	int vencedor = 0;
	static getInput inp = new getInput();
	static int heal = 50; //Quantos hp cada item de cura fornece
	static int holdsPokemon;
	
	class Atacar extends Event{

		Player jogador;
		Player outro;
		
		public Atacar(long eventTime, Player attacker, Player defender) {
			super(eventTime);
			this.jogador = attacker;
			this.outro = defender;
		}

		@Override
		public void action() {

			if(battle_on == 0)
				return;
			
			Player attacker = jogador;
			Player defender = outro;
			Attack attck = attacker.getAttack();
			defender.getAttacked(attck);
			
			System.out.println("O "+attacker.getNomeAtivo()+" do Jogador "+attacker.num+" desferiu um "+
					attck.nome+" no "+defender.getNomeAtivo()+" do Jogador "+outro.num+"\n("+attck.descricao+")");

			if(defender.checkPokemon() == 0) {
				vencedor = jogador.num;
				battle_on = 0;
			}
			
		}

		@Override
		public String description() {
			String descr = "[]";
			if(battle_on == 1) 
				descr = "[O Jogador "+jogador.num+" atacou o Jogador "+outro.num+"]";
			
			return descr;
		}
		
	}
	class Trocar_Pokemon extends Event{

		Player jogador;
		String descr = "";
		
		public Trocar_Pokemon(long eventTime,Player jogador) {
			super(eventTime);
			this.jogador = jogador;
		}

		@Override
		public void action() {
	
			if(battle_on == 0)
				return;
			
			String antes = jogador.getNomeAtivo();

			jogador.trocarPokemon();
			if(jogador.living_pokemon > 1) {
				
				descr = "[Jogador "+jogador.num+" trocou seu Pokémon, de "+antes+" para "+
						jogador.getNomeAtivo()+"]";
			}else {
				descr = "[Jogador "+jogador.num+" não pôde trocar de Pókemon.]";
				
			}			
		}

		@Override
		public String description() {
			return descr;
		}
	}
	class Usar_Item extends Event{

		Player jogador;
		String descr = "";

		public Usar_Item(long eventTime, Player jogador) {
			super(eventTime);
			this.jogador = jogador;
		}

		@Override
		public void action() {
			if(battle_on == 0)
				return;
			
			descr = "[Jogador "+jogador.num+" utilizou heal em "+jogador.usarItem(heal)+"]";
						
		}

		@Override
		public String description() {
			return descr;
		}
	}
	class Fugir extends Event{

		int num;
		public Fugir(long eventTime, int num) {
			super(eventTime);
			this.num = num;
		}

		@Override
		public void action() {
			if(battle_on == 1) {
				vencedor = (num+1)%2;
				battle_on = 0;
			}
		
		}

		@Override
		public String description() {
			return "[Jogador "+num+" fugiu da batalha]";
		}
	}


	class Rodada extends Event{

		int[] jogadas = new int[2];
		String[] opcoes = {"Fugir","Trocar de Pokémon","Usar item", "Atacar"};
		
		public Rodada(long eventTime) {
			super(eventTime);
		}

		public void printEstadoAtual(){
		
			System.out.println();
			
			//Printa estado atual do Jogador 0
			p0.printEstadoAtual();
			
			//Printa estado atual do Jogador 1
			p1.printEstadoAtual();
			
			System.out.println();
			
		}
		
		
		@Override
		public void action() {
		
			if(battle_on == 0) {
				System.out.println("Vencedor: Jogador "+vencedor);
				return;
			}
			
			System.out.println("\n***RODADA "+rounds+"***");
			
			if(rounds == 0) {
				System.out.println("Que comece a batalha!");
				
			}
			rounds++;
			
			printEstadoAtual();
			
			//Obtém jogadas
			jogadas[0] = p0.getJogada(opcoes);
			jogadas[1] = p1.getJogada(opcoes);
			
			int jogada_prioritaria = 0;
			int outra_jogada = 1;
			Player primeiro = p0;
			Player outro = p1;
			
			if(jogadas[0] < jogadas[1]) {
				jogada_prioritaria = jogadas[0];
				outra_jogada = jogadas[1];
				primeiro = p0;
				outro = p1;
			}else if(jogadas[1] <= jogadas[0]){
				jogada_prioritaria = jogadas[0];
				outra_jogada = jogadas[1];
				primeiro = p0;
				outro = p1;
			}
			
			
			
			switch(jogada_prioritaria) {
				case 0:
					addEvent(new Fugir(System.currentTimeMillis(),primeiro.num));
					break;
					
				case 1: 
					addEvent(new Trocar_Pokemon(System.currentTimeMillis(),primeiro));
					break;
				case 2:
					addEvent(new Usar_Item(System.currentTimeMillis(),primeiro));
					break;
				case 3:
					addEvent(new Atacar(System.currentTimeMillis(),primeiro,outro));
					break;
			}
			
				switch(outra_jogada) {
					case 0:
						addEvent(new Fugir(System.currentTimeMillis(),outro.num));
						break;
						
					case 1: 
						addEvent(new Trocar_Pokemon(System.currentTimeMillis(),outro));
						break;
					case 2:
						addEvent(new Usar_Item(System.currentTimeMillis(),outro));
						break;
					case 3:
						addEvent(new Atacar(System.currentTimeMillis(),outro,primeiro));
						break;
				}
				addEvent(new Rodada(System.currentTimeMillis()));
		
		}

		@Override
		public String description() {
			if(battle_on == 0)
				return "";
			
			return "[Jogador "+p0.num+" escolheu "+opcoes[jogadas[0]]+". Jogador "+p1.num+" escolheu "+opcoes[jogadas[1]]+"]";
		}
		
	}
	/*
	private class CheckPokemon extends Event{

		Player p;
		//String descr;
		
		public CheckPokemon(long eventTime, Player p) {
			super(eventTime);
			this.p = p;
		}

		@Override
		public void action() {
			p.checkPokemon();
			/*if(mortes == 1) {
				descr = "Houve mortes";
			}
			else if(mortes == 0){
				descr = "Não houve mortes";
			} else if (mortes == 2) {
				descr = "Houve mortes";
				battle_on = 0;
				addEvent(new Finalizar_Jogo(System.currentTimeMillis(),p.num%1));
			}
		}

		@Override
		public String description() {
			return "";
		}
		
	}

	/*private class Move extends Event{

		String descr;
		
		Player jogador;
		int jogada;
		
		Player outro;
		int outro_jogada;
		
		int order;
		
		public Move(long eventTime, int jogada, Player jogador, int outro_jogada,Player outro, int order) {
			
			super(eventTime);
			this.jogada = jogada;
			this.jogador = jogador;
			this.outro = outro;
			this.order = order;
			this.outro_jogada = outro_jogada;
			//System.out.println(jogador.num+" fazer "+jogada);
		}

		
		@Override
		public void action() {
			//System.out.println(jogador.num+" executar "+jogada);
			
			switch(jogada) {

				case 0:
				
					//addEvent(new Finalizar_Jogo(System.currentTimeMillis()+50,outro.num));
					descr = "Jogador "+jogador.num+" fugiu da batalha e "+outro.num+" venceu";
					return;
					
				case 1: 
					
					jogador.trocarPokemon();
					if(jogador.living_pokemon > 1) {
						String antes = jogador.getNomeAtivo();
						
						descr = "Jogador "+jogador.num+" trocou seu Pokémon, de "+antes+" para "+
								jogador.getNomeAtivo();
					}else {
						descr = "Jogador "+jogador.num+" não pôde trocar de Pókemon.";
						
					}
					break;
				
				case 2: 
					
					descr = "Jogador "+jogador.num+" utilizou heal em "+jogador.usarItem(heal);
					break;
			
				case 3:
				
					Player attacker = jogador;
					Player defender = outro;
					Attack attck = attacker.getAttack();
					defender.getAttacked(attck);
					descr = "O "+attacker.getNomeAtivo()+" do Jogador "+attacker.num+" desferiu um "+
							attck.nome+" no "+defender.getNomeAtivo()+" do Jogador "+outro.num+"\n"+attck.descricao;
					//addEvent(new CheckPokemon(System.currentTimeMillis(),defender));
					if(defender.checkLost() == 1) {
						battle_on = 0;
						//addEvent(new CheckPokemon(System.currentTimeMillis(),defender));
						//addEvent(new Finalizar_Jogo(System.currentTimeMillis(),attacker.num));
					}
					
					break;
			}
			
			//Se este for o primeiro movimento da rodada, criar um evento com a jogada do outro
			//jogador. Se for o segundo, chamar nova rodada.
			if(battle_on == 1)
				if(order == 0) {
					addEvent(new Move(System.currentTimeMillis(),outro_jogada,outro,-1,jogador,1));
				}
				else if(order == 1) {
					addEvent(new Rodada(System.currentTimeMillis()+55));
				}
			
		}

		@Override
		public String description() {
			return descr;
		}
		
	}
	
	class Finalizar_Jogo extends Event{

		int vencedor;
		
		public Finalizar_Jogo(long eventTime, int vencedor) {
			super(eventTime);
			this.vencedor = vencedor;
		}

		@Override
		public void action() {
			battle_on = 0;
		}

		@Override
		public String description() {
			return "Jogador "+vencedor+" venceu após "+(rounds)+" rodadas";
		}
		
	}

	class SetupGame extends Event{


		long evt;
		Battle_Controller bc;
		
		
		public SetupGame(long eventTime, Battle_Controller bc) {
			
			super(eventTime);
			evt = eventTime;
			this.bc = bc;
		}
	
		@Override
		public void action() {
			
			addEvent(new Make_Player(evt,0,bc,holdsPokemon));
			addEvent(new Make_Player(System.currentTimeMillis(),1,bc,holdsPokemon));
			gameOn = 1;
			addEvent(new Rodada(System.currentTimeMillis(),players[0],players[1]));

		}
	
		@Override
		public String description() {
			// TODO Auto-generated method stub
			return "Produzir jogadores";
		}
	
	
	}
*/
	
	//Jogadores são criados aqui. Modo PvP
	public Battle_Controller(Poke_Factory fct, int hold) {
	
		Battle_Controller.fct = fct;
		PlayerMaker pm = new PlayerMaker(fct,hold);
		holdsPokemon = hold;
		this.p0 = pm.make_player(0);
		this.p1 = pm.make_player(1);
		
		addEvent(new Rodada(System.currentTimeMillis()));
		
		
		
	}
	
	//Jogadores são dados prontos para o construtor
	public Battle_Controller(Player p0, Player p1, Poke_Factory fct,int hold){
		
		Battle_Controller.fct = fct;
		holdsPokemon = hold;
		this.p0 = p0;
		this.p1 = p1;
		
		addEvent(new Rodada(System.currentTimeMillis()));

	}

}
