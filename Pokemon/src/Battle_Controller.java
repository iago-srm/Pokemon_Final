
//Representa uma batalha individual
public class Battle_Controller extends Controller{
	
	static Poke_Factory fct;
	static getInput inp = new getInput();
	static int holdsPokemon;

	Player p0;
	Player p1;
	Player[] p = {p0,p1};

	int rounds = 0;
	int battle_on = 1;
	int vencedor = 0;
	
	//Jogadores s�o criados aqui. Modo PvP
	public Battle_Controller(Poke_Factory fct, int hold) {
	
		Battle_Controller.fct = fct;
		PlayerMaker pm = new PlayerMaker(fct,hold);
		holdsPokemon = hold;
		this.p0 = pm.make_player(0);
		this.p1 = pm.make_player(1);
		
		addEvent(new Rodada(System.currentTimeMillis()));
		
		
		
	}
	
	//Jogadores s�o dados prontos para o construtor
	public Battle_Controller(Player p0, Player p1, Poke_Factory fct,int hold){
		
		Battle_Controller.fct = fct;
		holdsPokemon = hold;
		this.p0 = p0;
		this.p1 = p1;
		
		addEvent(new Rodada(System.currentTimeMillis()));

	}
	
	class Atacar extends Event{

		//Jogador ataca e "outro" se defende.
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

			//Obtém um objeto de ataque do jogador atacante
			Attack attck = attacker.getAttack();

			defender.getAttacked(attck);
			
			/*System.out.println("O "+attacker.getNomeAtivo()+" do Jogador "+attacker.num+" desferiu um "+
					attck.nome+" no "+defender.getNomeAtivo()+" do Jogador "+outro.num+"\n("+attck.descricao+")");
			*/

			//Jogador atacado não tem mais Pokémon vivos.
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
				
				descr = "[Jogador "+jogador.num+" trocou seu Pok�mon, de "+antes+" para "+
						jogador.getNomeAtivo()+"]";
			//Jogador tem apenas um Pokémon vivo, então não pode trocar.
			}else {
				descr = "[Jogador "+jogador.num+" n�o p�de trocar de P�kemon.]";
				
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

			String pokemon = jogador.usarItem();
			
			descr = "[Jogador "+jogador.num+" utilizou heal em "+pokemon+"]";
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
				//Vencedor é o jogador que não fugiu.
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
		String[] opcoes = {"Fugir","Trocar de Pok�mon","Usar item", "Atacar"};
		
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
		
			//Flag foi settada em algum evento da rodada. Aqui, a rodada descobre essa info e termina o jogo.
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
			
			//Obt�m jogadas
			jogadas[0] = p0.getJogada(opcoes);
			jogadas[1] = p1.getJogada(opcoes);
			
			//As jogadas têm prioridades diferentes. Os jogadores selecionam suas jogadas e, então, a prioritária é executada
			//primeiro. A ordem de prioridade é a da disposição dos itens na array "opcoes" desta classe.
			//Se ambas as jogadas tiverem a mesma prioridade, o jogador 0 joga primeiro.
			int jogada_prioritaria = 0;
			int outra_jogada = 1;
			Player primeiro = p0;
			Player outro = p1;
			
			//Jogador 1 tem jogada prioritária
			if(jogadas[0] < jogadas[1]) {
				jogada_prioritaria = jogadas[0];
				outra_jogada = jogadas[1];
				primeiro = p0;
				outro = p1;
			//Jogador 1 não tem jogada prioritária
			}else if(jogadas[1] <= jogadas[0]){
				jogada_prioritaria = jogadas[0];
				outra_jogada = jogadas[1];
				primeiro = p0;
				outro = p1;
			}
			
			//Adicionar os eventos das jogadas
			
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
	
	

}
