fun main() {
    exibirMenssagemBoasVidas()
    exibirMenuPrincipal()
}

fun exibirMenssagemBoasVidas() {
    println("========================================")
    println("Bem-vindo ao Manga Rosa Memory Game!")
    println("========================================")
}

fun exibirMenuPrincipal() {
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
            4 -> {
                println("Saindo do jogo... até mais :(")
                break
            }
            else -> println("Opção inválida!! Tente novamente digitando uma opção válida")
        }
    }
}

// código samsam

fun iniciaJogo() {
    val tamanhoTabuleiro = selecionarTamanhoTabuleiro()
    val (jogador1, jogador2) = registrarJogadores()
    val tabuleiro = criarTabuleiro(tamanhoTabuleiro)

    println("\nTabuleiro configurado com sucesso! Pronto para iniciar...")

    jogar(tabuleiro, jogador1, jogador2) // código de arthur
}

// código de arthur: Função principal do jogo que controla os turnos
private fun jogar(tabuleiro: Array<Array<Carta>>, jogador1: Jogador, jogador2: Jogador) {
    var jogadorAtual = jogador1
    while (!jogoTerminado(tabuleiro)) {
        println("\nVez de ${jogadorAtual.nome} (${jogadorAtual.cor})")
        exibirTabuleiro(tabuleiro)
        jogarTurno(tabuleiro, jogadorAtual)
        jogadorAtual = if (jogadorAtual == jogador1) jogador2 else jogador1
    }

}

// código de arthur: Função que executa o turno de um jogador
private fun jogarTurno(tabuleiro: Array<Array<Carta>>, jogador: Jogador) {
    val primeiraCarta = escolherCarta(tabuleiro, jogador)
    primeiraCarta.virada = true
    exibirTabuleiro(tabuleiro)

    val segundaCarta = escolherCarta(tabuleiro, jogador)
    segundaCarta.virada = true
    exibirTabuleiro(tabuleiro)

    // Verifica se as cartas formam um par
    if (primeiraCarta.figura == segundaCarta.figura) {
        println("Par encontrado! 🎉")
        jogador.pontos += 1 // Se desejar, pode adicionar pontuação aqui
    } else {
        println("Não foi um par! As cartas serão ocultadas novamente.")
        Thread.sleep(2000) // Pequeno delay para o jogador ver as cartas antes de escondê-las
        primeiraCarta.virada = false
        segundaCarta.virada = false
    }

    exibirTabuleiro(tabuleiro) // Atualiza o tabuleiro após ocultar cartas erradas
}

// código de arthur: Função para escolher uma carta
private fun escolherCarta(tabuleiro: Array<Array<Carta>>, jogador: Jogador): Carta {
    var tentativas = 0
    while (true) {
        println("Digite a linha e a coluna da carta (ex: 1 2):")
        val (linha, coluna) = readlnOrNull()?.split(" ")?.mapNotNull { it.toIntOrNull() } ?: continue

        val linhaIndex = linha - 1
        val colunaIndex = coluna - 1

        if (linhaIndex in tabuleiro.indices && colunaIndex in tabuleiro[linhaIndex].indices) {
            val carta = tabuleiro[linhaIndex][colunaIndex]
            if (!carta.virada) {
                return carta
            } else {
                println("A carta da posição informada já está virada, por favor, escolha outra posição.")
            }
        } else {
            println("Posição da carta inválida, por favor, insira uma posição válida.")
        }

        tentativas++
        if (tentativas == 3) {
            println("Você errou 3 vezes! Passando a vez para o próximo jogador.")
            return tabuleiro[0][0]  // Retorna qualquer valor, pois a vez do jogador será passada
        }
    }
}


// código de arthur: Verifica se o jogo terminou
private fun jogoTerminado(tabuleiro: Array<Array<Carta>>): Boolean {
    return tabuleiro.all { linha -> linha.all { it.virada } }
}

// código de Arthur: Exibir tabuleiro atualizado com numeração corrigida e alinhamento melhorado
private fun exibirTabuleiro(tabuleiro: Array<Array<Carta>>) {
    println("\n     " + (1..tabuleiro.size).joinToString("   ") { it.toString() }) // Números das colunas
    println("   " + "-".repeat(tabuleiro.size * 4)) // Linha separadora

    for ((i, linha) in tabuleiro.withIndex()) {
        print("${i + 1} | ") // Número da linha, começando do 1
        for (carta in linha) {
            print(if (carta.virada) "[${carta.figura}] " else "[??]  ")
        }
        println()
    }
}


// código samsam: Funções auxiliares (já definidas anteriormente)
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
            else -> println("\nPor favor, escolha uma das opções de tamanho de tabuleiro disponíveis")
        }
    }
}

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

private fun criarTabuleiro(tamanho: Int): Array<Array<Carta>> {
    val totalPares = (tamanho * tamanho) / 2

    val paresPretos = 1
    val paresVermelhoAzul = totalPares / 2
    val paresAmarelos = totalPares - paresPretos - paresVermelhoAzul

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

// código samsam: Funções de exibição de regras e pontuação
fun exibirRegras() {
    println("========================================")
    println("            REGRAS DE PONTUAÇÃO             ")
    println("========================================")
    println(
        "\n Se encontrar um par de cartas..."
    )
    println("Aperte ENTER para voltar para o menu principal: ")
    readln()
}

fun exibirPontuacao(){
    println("É contigo meu mano tony...")
}
