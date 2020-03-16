import java.util.Scanner;

//Classe que ajuda a obter input do usuário pelo teclado. Cada construtor cria um objeto que vai obter um tipo de input diferente.
public class getInput{
	
	int min, max;
	String[] possible;
	Scanner scan = new Scanner(System.in);
	
	public getInput() {}

	//Input numérico (inteiro) no intervalo [min,max]
	public getInput(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	//Input de String, dentre as possibilidades
	public getInput(String...possible){
		this.possible = possible;
	}
	
	public String result_String() {
		String inp;
		int ok=0;
		
		inp = scan.nextLine();
		while(ok!=1) {
			for(int i=0; i<possible.length; i++) {
				if(inp.equals(possible[i])) {
					ok=1;
					break;
				}
			}
			if(ok!=1) {
				System.out.println("Favor inserir uma op��o v�lida");
				inp = scan.nextLine();
			}
		}
		
		return inp;
	}
	
	public String result_String(String...possible) {
		String inp;
		int ok=0;
		
		inp = scan.nextLine();
		while(ok!=1) {
			for(int i=0; i<possible.length; i++) {
				if(inp.equals(possible[i])) {
					ok=1;
					break;
				}
			}
			if(ok!=1) {
				System.out.println("Favor inserir uma op��o v�lida");
				inp = scan.nextLine();
			}
		}
		
		return inp;
	}
 
	
	public int result_int() {
		String inp;
		int input = min;
		int ok = 0;
		
		while(ok!=1) {
			inp = scan.nextLine();
			
			//Pega n�o n�meros
			try {
				input = Integer.parseInt(inp);
			}
			catch (Exception e) {
				System.out.println("Favor inserir um n�mero.");
				continue;
			}
			
			//Pega n�meros inv�lidos
			if(input < min || input > max) {
				System.out.println("Favor inserir um n�mero v�lido.");
			}else
				ok=1;
		}
		return input;
		
	}

	
	public int result_int(int min, int max) {
			
			String inp;
			int input = min;
			int ok = 0;
			
			while(ok!=1) {
				inp = scan.nextLine();
				
				//Pega n�o n�meros
				try {
					input = Integer.parseInt(inp);
				}
				catch (Exception e) {
					System.out.println("Favor inserir um n�mero.");
					continue;
				}
				
				//Pega n�meros inv�lidos
				if(input < min || input > max) {
					System.out.println("Favor inserir um n�mero v�lido.");
				}else
					ok=1;
			}
			return input;
			
		}
	
}