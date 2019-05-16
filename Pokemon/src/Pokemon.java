import java.util.Scanner;

class Attack{
		
		String nome;
		String descricao;
		int dano;
		double proporcao;
		
		public Attack (String nom, int dan, double prop, String descr) {
			nome = nom;
			descricao = descr;
			dano = dan;
			proporcao = prop;
		}
		

		
		public String descricao() {
			return descricao;
		}
}

public class Pokemon {

	getInput inp = new getInput();
	String nome;
	Attack attacks[] = new Attack[4];
	int hp = 100;

	public Attack getAttack(int random) {
		Attack choice = this.attacks[random]; 
		attack(choice);
		return choice; 
	}
	public Attack getAttack() {
		
		for(int i=0; i<4;i++) {
			System.out.println(i+") "+this.attacks[i].nome);
		}
		Attack choice = this.attacks[inp.result_int(0,4)]; 
		attack(choice);
		return choice; 
	}
	
	public void heal(int heal) {
		
		if(hp + heal >= 100) 
			hp = 100;		
		else
			hp += heal;
		
	}
	
	public void attack(Attack attck) {
		hp -= attck.proporcao*hp;
	}
	
	public void getAttacked(Attack attck) {
		hp -= attck.dano;
		
	}
	
	public Pokemon(String nom, String attck1, String attck2, String descr1, String descr2) {
		nome = nom;
		attacks[0] = new Attack("Investida",15,0,"Quase uma ombrada");
		attacks[1] = new Attack("Ataque Forte",30,0.1,"Uma cabeçada dolorida");
		attacks[2] = new Attack(attck1,40,0.2,descr1);
		attacks[3] = new Attack(attck2,40,0.2,descr2);
	}
}
