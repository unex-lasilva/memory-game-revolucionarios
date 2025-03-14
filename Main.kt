
fun main() {
    exibirMenssagemBoasVidas()
    exibirMenuPrincipal()

}

fun exibirMenssagemBoasVidas(){
    println("========================================")
    println("Bem-vindo ao Manga Rosa Memory Game!")
    println("========================================")
}
fun exibirMenuPrincipal(){
    while (true) {
        println("/Menu principal")
        println("1 -> Começar")
        println("2 -> Regras")
        println("3 -> Pontuação")
        println("4 -> Sair ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> iniciaJogo()
            2 -> exibirRegras()
            3 -> exibirPontuacao()
            4 ->{
                println("saindo do jogo... ate mais :(")
                break

            }

            else -> println("opção invalida!! tente novamente digitando uma opção valida")
        }
    }
}
// codigo samsam

fun iniciaJogo() {
    val tamanhoTabuleiro = selecionarTamanhoTabuleiro()
    val (jogador1, jogador2) = registrarJogadores()
    val tabuleiro = criarTabuleiro(tamanhoTabuleiro)

    println("\nTabuleiro configurado com sucesso! Pronto para iniciar...")
}

// selecionar tamanho
private fun selecionarTamanhoTabuleiro(): Int {
    while (true) {
        println("\nQUAL O TAMANHO DE TABULEIRO DESEJA JOGAR?")
        println("a. 4x4")
        println("b. 6x6")
        println("c. 8x8")
        println("d. 10x10")
        print("DIGITE A OPÇÃO: ")

        when (readlnOrNull()?.lowercase()) {
            "a" -> return 4
            "b" -> return 6
            "c" -> return 8
            "d" -> return 10
            else -> println("\nPor favor, escolha uma das opções de tamanho de tabuleiro disponíveis (a-d)")
        }
    }
}

// registrar jogadores
private fun registrarJogadores(): Pair<Jogador, Jogador> {
    val jogador1 = Jogador(
        nome = solicitarNome(1),
        cor = "Vermelho"
    )

    val jogador2 = Jogador(
        nome = solicitarNome(2),
        cor = "Azul"
    )

    return Pair(jogador1, jogador2)
}

private fun solicitarNome(numeroJogador: Int): String {
    print("\nQUAL O APELIDO DA(O) PARTICIPANTE $numeroJogador? ")
    val nome = readlnOrNull()?.trim()
    return if (nome.isNullOrEmpty()) {
        "PARTICIPANTE0$numeroJogador"
    } else {
        nome.uppercase()
    }
}

private data class Jogador(
    val nome: String,
    val cor: String,
    var pontos: Int = 0
)

// criaçao do tabuleiro
private fun criarTabuleiro(tamanho: Int): Array<Array<Carta>> {
    val totalPares = (tamanho * tamanho) / 2

    // define a distribuição de cores
    val paresPretos = 1
    val paresVermelhoAzul = totalPares / 2
    val paresAmarelos = totalPares - paresPretos - paresVermelhoAzul

    // cria a lista de carta
    val cartas = mutableListOf<Carta>()


    repeat(paresPretos) {
        cartas.add(Carta(cor = "Preto", figura = "XX"))
        cartas.add(Carta(cor = "Preto", figura = "XX"))
    }

    repeat(paresVermelhoAzul) { index ->
        val cor = if (index % 2 == 0) "Vermelho" else "Azul"
        val figura = "F${index + 1}"
        cartas.add(Carta(cor = cor, figura = figura))
        cartas.add(Carta(cor = cor, figura = figura))
    }

    repeat(paresAmarelos) { index ->
        val figura = "A${index + 1}"
        cartas.add(Carta(cor = "Amarelo", figura = figura))
        cartas.add(Carta(cor = "Amarelo", figura = figura))
    }

    // embaralhaa
    cartas.shuffle()

    return Array(tamanho) { row ->
        Array(tamanho) { col ->
            cartas[row * tamanho + col]
        }
    }
}

private data class Carta(
    val cor: String,
    val figura: String,
    var virada: Boolean = false
)
// fim codigo sam
fun exibirRegras() {
    println("========================================")
    println("            REGRAS DE PONTUAÇÃO             ")
    println("========================================")
    println(
        "\n Se  encontrar um par de cartas com fundo amarelo, fatura 1 ponto \n" +
                " Se  encontrar um par de cartas com o fundo da sua cor, fatura 5 pontos \n" +
                " Se você encontrar um par de cartas com o fundo da cor de seu adversário e errar, perde 2 pontos. Porém, se acertar, ganha apenas 1 ponto.  \n" +
                " Se você ficar sem pontos não se preocupe não ficara negativo seus pontos continuará nulo ate conserguir marcar algum \n" +
                "Se você encontrar uma carta com o fundo preto e errar o seu par, sinto muito perde o jogo, mesmo que tenha a pontuação superior à da(o) outra(o) participante. Mas se acertar, ganha o jogo. :) "
    )
    println("Aperte ENTER para voltar para o menu principal: ")
    readln()

}

fun exibirPontuacao(){
    println("É contigo meu mano tony...")
}