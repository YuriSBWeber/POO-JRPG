package rpg.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import rpg.model.Clerigo;
import rpg.model.Guerreiro;
import rpg.model.Combatente;
import rpg.model.Monstro;
import rpg.model.Personagem;
import rpg.model.Chefe;
import rpg.world.Grupo;
import rpg.item.Consumivel;

public class Batalha {

    private Grupo grupo;
    private List<Monstro> monstros;
    private Random random;
    private Combatente usuarioDoSantuario;
    private int pulsosDeSantuarioRestantes;
    private List<String> registro;
    private Map<Chefe, Integer> turnosDosChefes;

    public Batalha(Grupo grupo, List<Monstro> monstros) {
        this.grupo = grupo;
        this.monstros = new ArrayList<>(monstros);
        this.random = new Random();
        this.registro = new ArrayList<>();
        this.turnosDosChefes = new HashMap<>();
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public List<Monstro> getMonstros() {
        return monstros;
    }
    public List<String> getRegistro() {
        return registro;
    }

    public boolean todosMonstrosForamDerrotados() {
        for (Monstro monstro : monstros) {
            if (monstro.estaVivo()) {
                return false;
            }
        }

        return true;
    }

    public boolean grupoFoiDerrotado() {
        return grupo.getMembrosVivos().isEmpty();
    }

    public boolean batalhaTerminou() {
        return todosMonstrosForamDerrotados() || grupoFoiDerrotado();
    }

    public List<Combatente> criarOrdemDeTurnos() {
        List<Combatente> ordem = new ArrayList<>();

        ordem.addAll(grupo.getMembrosVivos());

        for (Monstro monstro : monstros) {
            if (monstro.estaVivo()) {
                ordem.add(monstro);
            }
        }

        ordem.sort(Comparator.comparingInt(Combatente::getVelocidade)
                .reversed());

        return ordem;
    }

    public void executarRodada(AcaoCombate acaoDoJogador,
            Combatente alvoEscolhido) {
        registro.clear();

        List<Combatente> ordem = criarOrdemDeTurnos();
        Personagem protagonista = grupo.getMembros().get(0);
        for (Combatente combatente : ordem) {
            combatente.iniciarNovaRodada();
        }
        aplicarSantuarioPendente();

        for (Combatente combatente : ordem) {
            if (batalhaTerminou() || !combatente.estaVivo()) {
                continue;
            }

            if (combatente == protagonista) {
                if (acaoDoJogador instanceof Habilidade
                        && ((Habilidade) acaoDoJogador).isAtingeTodos()) {

                    if (acaoDoJogador instanceof Santuario) {
                        executarAcaoEmArea(acaoDoJogador, protagonista,
                                grupo.getMembrosVivos());
                    } else {
                        executarAcaoEmArea(acaoDoJogador, protagonista,
                                monstros);
                    }

                } else {
                    Combatente alvo = alvoEscolhido;

                    if (alvo instanceof Monstro && !alvo.estaVivo()) {
                        alvo = escolherMonstroComMenorHp();
                    }

                    executarAcao(acaoDoJogador, protagonista, alvo);
                }


            } else if (combatente instanceof Personagem) {
                executarTurnoAliado((Personagem) combatente);
                
            } else if (combatente instanceof Chefe) {
                executarTurnoChefe((Chefe) combatente);
            } else if (combatente instanceof Monstro) {
                Personagem alvo = escolherAlvoDoMonstro();

                executarAcao(new AtaqueBasico(), combatente, alvo);
            }
        }
    }

    private void executarTurnoChefe(Chefe chefe) {
        int turno = turnosDosChefes.getOrDefault(chefe, 0) + 1;
        turnosDosChefes.put(chefe, turno);
        HabilidadeEspecialChefe especial = HabilidadeEspecialChefe.criar(chefe.getHabilidadeEspecial());
        if (turno % 3 == 0 && especial != null && chefe.getMpAtual() >= especial.getCustoMp()) {
            if (especial.isAtingeTodos()) {
                executarAcaoEmArea(especial, chefe, grupo.getMembrosVivos());
            } else {
                executarAcao(especial, chefe, escolherAlvoDoMonstro());
            }
        } else {
            executarAcao(new AtaqueBasico(), chefe, escolherAlvoDoMonstro());
        }
    }

    private Monstro escolherAlvoDoAliado(Personagem aliado) {
        for (Monstro monstro : monstros) {
            int danoEstimado = aliado.getAtaque() - monstro.getDefesa();

            if (monstro.estaVivo() && danoEstimado >= monstro.getHpAtual()) {
                return monstro;
            }
        }

        return escolherMonstroComMenorHp();
    }

    private Monstro escolherMonstroComMenorHp() {
        Monstro menor = null;

        for (Monstro monstro : monstros) {
            if (monstro.estaVivo()
                    && (menor == null
                    || monstro.getHpAtual() < menor.getHpAtual())) {

                menor = monstro;
            }
        }

        return menor;
    }

    private Personagem escolherAlvoDoMonstro() {
        List<Personagem> vivos = grupo.getMembrosVivos();

        return vivos.get(random.nextInt(vivos.size()));
    }
    public boolean executarAcao(AcaoCombate acao, Combatente usuario,
            Combatente alvo) {

        if (acao instanceof UsarConsumivel && !((UsarConsumivel) acao).podeExecutar(alvo)) {
            registro.add("Não foi possível usar o consumível: alvo inválido ou item indisponível.");
            return false;
        }

        if (!usuario.gastarMp(acao.getCustoMp())) {
            return false;
        }

        registro.add(usuario.getNome()
                + " usa " + acao.getNome() + ".");

        if (acao instanceof AtaqueFurtivo
                && !usuario.recebeuDanoNestaRodada()) {

            AtaqueFurtivo ataqueFurtivo =
                    (AtaqueFurtivo) acao;

            ataqueFurtivo.executarComCritico(usuario, alvo, true);

        } 
        
        else {
            acao.executar(usuario, alvo);
        }        
        usuario.reduzirEfeitosTemporarios();
        if (acao instanceof Santuario) {
            usuarioDoSantuario = usuario;
            pulsosDeSantuarioRestantes = 2;
        }

        return true;
    }

    public boolean executarAcaoEmArea(AcaoCombate acao,
            Combatente usuario, List<? extends Combatente> alvos) {

        if (!usuario.gastarMp(acao.getCustoMp())) {
            return false;
        }
        registro.add(usuario.getNome()
                + " usa " + acao.getNome() + ".");

        for (Combatente alvo : alvos) {
            if (alvo.estaVivo()) {
                acao.executar(usuario, alvo);
            }
        }

        if (acao instanceof Santuario) {
            usuarioDoSantuario = usuario;
            pulsosDeSantuarioRestantes = 2;
        }

        usuario.reduzirEfeitosTemporarios();

        return true;
    }
    private void aplicarSantuarioPendente() {
        if (pulsosDeSantuarioRestantes <= 0) {
            return;
        }

        Santuario santuario = new Santuario();

        for (Personagem personagem : grupo.getMembrosVivos()) {
            santuario.executar(usuarioDoSantuario, personagem);
        }

        registro.add("Santuário restaura HP do grupo.");

        pulsosDeSantuarioRestantes--;
    }
    private void executarTurnoAliado(Personagem aliado) {
        Personagem protagonista = grupo.getMembros().get(0);
        if (!protagonista.estaVivo()) {
            for (Consumivel item : grupo.getInventario().getConsumiveisEquipados()) {
                if (item.isRevive()) {
                    executarAcao(new UsarConsumivel(item, grupo.getInventario()), aliado, protagonista);
                    return;
                }
            }
        }
        AcaoCombate acao = escolherAcaoAliado(aliado);

        if (acao instanceof Cura) {
            executarAcao(acao, aliado, escolherAliadoMaisFerido());
            return;
        }

        if (acao instanceof Habilidade
                && ((Habilidade) acao).isAtingeTodos()) {

            if (acao instanceof Santuario) {
                executarAcaoEmArea(acao, aliado,
                        grupo.getMembrosVivos());
            } else {
                executarAcaoEmArea(acao, aliado, monstros);
            }

            return;
        }

        executarAcao(acao, aliado, escolherAlvoDoAliado(aliado));
    }

    private AcaoCombate escolherAcaoAliado(Personagem aliado) {
        GeradorAcoes geradorAcoes = new GeradorAcoes();

        if (aliado instanceof Clerigo) {
            Personagem ferido = escolherAliadoMaisFerido();

            if (ferido.getHpAtual() * 100 / ferido.getHpMaximo() < 50
                    && aliado.getMpAtual() >= 16) {

                return new Cura();
            }
        }

        if (aliado instanceof Guerreiro
                && !aliado.estaComFuria()
                && aliado.getNivel() >= 3
                && aliado.getMpAtual() >= 15) {

            return new Furia();
        }

        AcaoCombate melhorAcao = new AtaqueBasico();
        double maiorMultiplicador = 1.0;

        for (AcaoCombate acao : geradorAcoes.criarAcoes(aliado)) {
            if (acao instanceof Habilidade) {
                Habilidade habilidade = (Habilidade) acao;

                boolean habilidadeDeCura = acao instanceof Cura
                        || acao instanceof Santuario
                        || acao instanceof Furia;

                if (!habilidadeDeCura
                        && aliado.getMpAtual() >= acao.getCustoMp()
                        && habilidade.getMultiplicador()
                        > maiorMultiplicador) {

                    melhorAcao = acao;
                    maiorMultiplicador = habilidade.getMultiplicador();
                }
            }
        }

        return melhorAcao;
    }

    private Personagem escolherAliadoMaisFerido() {
        Personagem maisFerido = grupo.getMembrosVivos().get(0);

        for (Personagem personagem : grupo.getMembrosVivos()) {
            int porcentagemAtual = personagem.getHpAtual() * 100
                    / personagem.getHpMaximo();

            int porcentagemMenor = maisFerido.getHpAtual() * 100
                    / maisFerido.getHpMaximo();

            if (porcentagemAtual < porcentagemMenor) {
                maisFerido = personagem;
            }
        }

        return maisFerido;
    }
}
