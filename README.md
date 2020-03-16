Pokémon - Lab de OOP - Poli/IME

Esse é um jogo inspirado no Pokémon red/yellow que eu fiz para a matéria de laboratório de Programação Orientada a Objetos, no 3º semestre, dada pelo IME-USP para a turma da engenharia de computação.

Todo o output do jogo é texto. Os controles são também em texto, e são do tipo de números inteiros ou letras. Todos os inputs são explicados durante jogo.

Há dois modos de jogo: PvP (Player vs Player) e Mapa. 

No modo mapa, o jogador anda por um mapa em que há dois tipos de terreno: grama ou não-grama(asfalto?). Enquanto o jogador estiver na grama, há uma chance de aparece um Pokémon selvagem, com quem o jogador tem que lutar para prosseguir. A batalha contra o Pokémon selvagem usa a mesma estrutura da PvP.

O jogo precisa de dois arquivos de texto, cujo caminho absoluto deve ser fornecido pelo usuário quando pedido: 
1) pokemon_list.txt, que contém informações nessa ordem: a quantidade de Pokémons, quantidade de ataques especiais que eles tem, nomes dos Pokémon, nomes e descrição dos seus ataques especiais.
2) mapa.txt, que contém o mapa do jogo

TODO
- método para separar conteúdo de comentário a partir de um separador como ";", para poder adicionar comentários nos arquivos txt.
- Ataques especiais e descrições que façam sentido
- Mais Pokémons
- Obter tamanho do mapa do arquivo txt
- Pedir para o usuário quais os caracteres que ele quer que represente o jogador, a grama e o asfalto e alterar o output visual do mapa a partir disso.
- Opção de salvar o jogo, e criar um arquivo com as condições atuais do jogo
- Menu com "Load Game" e "New Game"
- Pokémon selvagem deve ser capturado pelo jogador depois de ser derrotado
- Unificar classes Player e PlayerMaker (não há razão para serem diferentes)
- Refatorar funções e classes, para garantir SRP (single responsability principle)