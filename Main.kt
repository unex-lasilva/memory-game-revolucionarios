fun main() {
    exibirMenssagemBoasVindas()
    exibirMenuPrincipal()
}

fun exibirMenssagemBoasVindas() {
    println("========================================")
    println("Bem-vindo ao Manga Rosa Memory Game!")
    println("========================================")
}

data class ResultadoJogo(
    val nomeJogador1: String,
    val pontosJogador1: Int,
    val nomeJogador2: String,
    val pontosJogador2: Int,
)

val historicoJogos = mutableListOf<ResultadoJogo>()

fun exibirMenuPrincipal() {
    while (true) {
        println("\nMenu principal")
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
    var jogadorAguardando = jogador2
    while (!jogoTerminado(tabuleiro)) {
        println("\nVez de ${jogadorAtual.nome} (${jogadorAtual.cor})")
        exibirTabuleiro(tabuleiro)
        jogarTurno(tabuleiro, jogadorAtual, jogadorAguardando)
        jogadorAtual = jogadorAguardando.also { jogadorAguardando = jogadorAtual }
    }
    anunciarVencedor(jogador1, jogador2)
}

// código de arthur: Função que executa o turno de um jogador
private fun jogarTurno(tabuleiro: Array<Array<Carta>>, jogadorAtual: Jogador, jogadorAguardando: Jogador) {
    val codigoResetarCor = "\u001B[0m"
    val codigoCor = when (jogadorAtual.cor) {
        "Vermelho" -> "\u001B[31m"
        "Azul" -> "\u001B[34m"
        else -> ""
    }
    println("\nTurno de ${jogadorAtual.nome} ($codigoCor${jogadorAtual.cor}$codigoResetarCor):")
    val primeiraCarta = escolherCarta(tabuleiro, jogadorAtual, "primeira")
    if (primeiraCarta == null) {
        println("${jogadorAtual.nome} perdeu a vez.")
        return
    }
    val segundaCarta = escolherCarta(tabuleiro, jogadorAtual, "segunda")
    if (segundaCarta == null) {
        println("${jogadorAtual.nome} perdeu a vez.")
        primeiraCarta.virada = false
        return
    }

    // Vira as cartas
    segundaCarta.virada = true
    exibirTabuleiro(tabuleiro)

    // Verifica se as cartas formam um par
    if (primeiraCarta.figura == segundaCarta.figura) {
        tratarAcerto(jogadorAtual, jogadorAguardando, primeiraCarta.cor)
    } else {
        if (primeiraCarta.figura == "XXX" || jogadorAguardando.cor == primeiraCarta.cor
        ) {
            tratarErro(jogadorAtual, jogadorAguardando, primeiraCarta.cor)
        } else {
            if(segundaCarta.figura == "XXX") {
                tratarErro(jogadorAtual, jogadorAguardando, primeiraCarta.cor)
            } else {
                tratarErro(jogadorAtual, jogadorAguardando, segundaCarta.cor)
            }
        }
        // Vira as cartas de volta, pois houve erro
        primeiraCarta.virada = false
        segundaCarta.virada = false
    }

    println("A pontuação de ${jogadorAtual.nome}: ${jogadorAtual.pontos}")
    println("A pontuação de ${jogadorAguardando.nome}: ${jogadorAguardando.pontos}")

}

private fun tratarAcerto(jogadorAtual: Jogador, jogadorAguardando: Jogador, cor: String) {
    when (cor) {
        "Amarelo" -> {
            jogadorAtual.pontos += 1
            println("\nVocê acertou! Ganhou 1 ponto, pois a carta era amarela.")
        }
        jogadorAtual.cor -> {
            jogadorAtual.pontos += 5
            println("\nExcelente! Ganhou 5 pontos por acertar a sua cor.")
        }
        jogadorAguardando.cor -> {
            jogadorAtual.pontos += 1
            println("\nBoa! Ganhou 1 ponto, pois a carta era da cor do adversário.")
        }
        "Preto" -> {
            jogadorAtual.pontos += 50
            println("\nParabéns! Uma carta preta foi aberta e o par foi encontrado.")
        }
    }
}

private fun tratarErro(jogadorAtual: Jogador, jogadorAguardando: Jogador, cor: String) {
    when (cor) {
        jogadorAguardando.cor -> {
            jogadorAtual.pontos = maxOf(0, jogadorAtual.pontos - 2)
            println("\nErrou! Perdeu 2 pontos, pois uma das cartas era do adversário.")
        }
        "Preto" -> {
            jogadorAtual.pontos = maxOf(0, jogadorAtual.pontos - 50)
            println("\nQue pena! Uma carta preta foi aberta e o par não foi encontrado.")
        }
    }
}

private fun mensagemFimDeJogo() {
    println("\n╔═══════════════════════════════════════════╗")
    println("║              Fim de Jogo!                 ║")
    println("╚═══════════════════════════════════════════╝\n")
}

private fun anunciarVencedor(jogador1: Jogador, jogador2: Jogador) {
    val vencedor = if (jogador1.pontos > jogador2.pontos) jogador1
    else if (jogador2.pontos > jogador1.pontos) jogador2
    else null

    mensagemFimDeJogo()

    if (vencedor != null) {
        println("Vencedor: ${vencedor.nome} com ${vencedor.pontos} pontos!")
    } else {
        println("Empate! Ambos os jogadores terminaram com ${jogador1.pontos} pontos.")
    }

    println("\nPontuações Finais:")
    println("${jogador1.nome}: ${jogador1.pontos}")
    println("${jogador2.nome}: ${jogador2.pontos}")

    // Salvar o resultado do jogo no histórico
    historicoJogos.add(ResultadoJogo(jogador1.nome, jogador1.pontos, jogador2.nome, jogador2.pontos))

    println("\nPressione ENTER para voltar ao menu principal...")
    readlnOrNull()
}

// código de arthur: Função para escolher uma carta
private fun escolherCarta(tabuleiro: Array<Array<Carta>>, jogador: Jogador,ordinal: String): Carta? {
    var tentativas = 0
    val tamanhoTabuleiro = tabuleiro.size
    while (tentativas < 3) {
        print("Digite a linha (1-$tamanhoTabuleiro) da $ordinal carta:")
        val linhaInput = readlnOrNull()
        print("Digite a coluna (1-$tamanhoTabuleiro) da $ordinal carta:")
        val colunaInput = readlnOrNull()

        val numeroLinha = linhaInput?.toIntOrNull()
        val numeroColuna = colunaInput?.toIntOrNull()

        if (numeroLinha == null || numeroColuna == null || numeroLinha !in 1..tamanhoTabuleiro || numeroColuna !in 1..tamanhoTabuleiro) {
            println("Posição inválida. Insira números de 1 a $tamanhoTabuleiro.")
            tentativas++
            continue
        }

        val linha = numeroLinha - 1
        val coluna = numeroColuna - 1

        if (linha in tabuleiro.indices && coluna in tabuleiro[linha].indices) {
            val carta = tabuleiro[linha][coluna]
            if (!carta.virada) {
                if (ordinal == "primeira") {
                    carta.virada = true
                    exibirTabuleiro(tabuleiro)
                }
                return carta
            } else {
                println("A carta da posição informada já está virada, por favor, escolha outra posição.")
            }
        } else {
            println("Posição da carta inválida, por favor, insira uma posição válida.")
        }

        tentativas++
    }
    println("Você errou 3 vezes. Perdeu a vez.")
    return null
}


// código de arthur: Verifica se o jogo terminou
private fun jogoTerminado(tabuleiro: Array<Array<Carta>>): Boolean {
    return tabuleiro.all { linha -> linha.all { it.virada } }
}

// código de Arthur: Exibir tabuleiro atualizado com numeração corrigida e alinhamento melhorado
private fun exibirTabuleiro(tabuleiro: Array<Array<Carta>>) {
    println("\n═══════ Tabuleiro Atual ═══════")
    print("      ")
    for (j in tabuleiro.indices) {
        print("${j + 1}".padEnd(6))
    }
    println()
    for (i in tabuleiro.indices) {
        print("${i + 1}".padEnd(4))
        for (j in tabuleiro[i].indices) {
            val carta = tabuleiro[i][j]
            val simbolo = if (carta.virada) {
                val codigoCor = when (carta.cor) {
                    "Vermelho" -> "\u001B[31m" // Vermelho
                    "Azul" -> "\u001B[34m" // Azul
                    "Amarelo" -> "\u001B[33m" // Amarelo
                    "Preto" -> "\u001B[30m" // Preto
                    else -> ""
                }
                val codigoReset = "\u001B[0m"
                "[$codigoCor${carta.figura}$codigoReset]"
            } else "▓▒▒▒▓"
            print("$simbolo ")
        }
        println()
    }
    println("═══════════════════════════════")
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
        cartas.add(Carta(cor = "Preto", figura = "XXX"))
        cartas.add(Carta(cor = "Preto", figura = "XXX"))
    }
    fun formatarNumero(i: Int) = String.format("%02d", i)
    repeat(paresVermelhoAzul) { index ->
        val cor = if (index % 2 == 0) "Vermelho" else "Azul"
        val figura = "F${formatarNumero(index + 1)}"
        cartas.add(Carta(cor = cor, figura = figura))
        cartas.add(Carta(cor = cor, figura = figura))
    }

    repeat(paresAmarelos) { index ->
        val figura = "A${formatarNumero(index + 1)}"
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
    println("\nSe encontrar um par de cartas da sua cor: +5 pontos")
    println("Se encontrar um par de cartas da cor do adversário: +1 ponto")
    println("Se encontrar um par de cartas amarelas: +1 ponto")
    println("Se encontrar um par de cartas pretas: +50 pontos")
    println("\nSe errar e virar uma carta da cor do adversário: -2 pontos")
    println("Se errar e virar uma carta preta: -50 pontos")
    println("\nO jogo termina quando todas as cartas forem viradas.")
    println("O jogador com mais pontos vence!")
    println("Aperte ENTER para voltar para o menu principal: ")
    readln()
}

fun exibirPontuacao() {
    println("========================================")
    println("            HISTÓRICO DE JOGOS          ")
    println("========================================")

    if (historicoJogos.isEmpty()) {
        println("\nAinda não há jogos registrados.")
    } else {
        println("\nÚltimos resultados (mais recentes primeiro):")
        historicoJogos.reversed().forEachIndexed { index, resultado ->
            println("\nJogo #${historicoJogos.size - index}")
            println("${resultado.nomeJogador1}: ${resultado.pontosJogador1} pontos")
            println("${resultado.nomeJogador2}: ${resultado.pontosJogador2} pontos")

            val vencedor = if (resultado.pontosJogador1 > resultado.pontosJogador2)
                resultado.nomeJogador1
            else if (resultado.pontosJogador2 > resultado.pontosJogador1)
                resultado.nomeJogador2
            else
                "Empate"

            println("Resultado: ${if (vencedor != "Empate") "Vencedor - $vencedor" else vencedor}")
            println("----------------------------------------")
        }
    }

    println("\nAperte ENTER para voltar para o menu principal: ")
    readln()
}
