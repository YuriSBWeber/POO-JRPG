import java.util.List;
import rpg.Jogo;
import rpg.combat.AcaoCombate;
import rpg.combat.AtaqueBasico;
import rpg.combat.Batalha;
import rpg.combat.HabilidadeEspecialChefe;
import rpg.combat.Santuario;
import rpg.model.Chefe;
import rpg.model.Clerigo;
import rpg.model.Combatente;
import rpg.model.Guerreiro;
import rpg.model.Monstro;
import rpg.model.Personagem;
import rpg.world.ConfiguradorMundo;
import rpg.world.Grupo;

public class TesteCombate {
    private static final AcaoCombate ESPERAR = new AcaoCombate() {
        public String getNome() { return "Esperar"; }
        public int getCustoMp() { return 0; }
        public void executar(Combatente usuario, Combatente alvo) { }
    };

    private static class PersonagemDeTeste extends Personagem {
        PersonagemDeTeste(String nome) { super(nome, 1, 5000, 100, 1, 0, 100); }
        public void subirNivel() { }
    }

    private static void conferir(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
    }

    private static int hpGrupo(Grupo grupo) {
        return grupo.getMembros().stream().mapToInt(Personagem::getHpAtual).sum();
    }

    private static boolean registrou(Batalha batalha, String texto) {
        return batalha.getRegistro().stream().anyMatch(linha -> linha.contains(texto));
    }

    private static void testarSantuario() {
        Clerigo clerigo = new Clerigo("Clérigo");
        for (int i = 0; i < 4; i++) clerigo.subirNivel();
        Guerreiro aliado = new Guerreiro("Aliado");
        clerigo.receberDano(65);
        aliado.receberDano(65);
        Grupo grupo = new Grupo(clerigo);
        grupo.adicionarAliado(aliado);
        Monstro monstro = new Monstro("Alvo", 1, 10000, 0, 1, 0, 1, 0, 0);
        Batalha batalha = new Batalha(grupo, List.of(monstro));
        int cura = (int) (clerigo.getAtaque() * 0.6);
        int anterior = hpGrupo(grupo);
        int mp = clerigo.getMpAtual();
        batalha.executarRodada(new Santuario(), clerigo);
        conferir(hpGrupo(grupo) == anterior + cura * 2 - 1, "cura inicial para cada aliado");
        conferir(clerigo.getMpAtual() == mp - 30, "Santuário cobra MP uma vez");
        for (int i = 0; i < 2; i++) {
            anterior = hpGrupo(grupo);
            batalha.executarRodada(ESPERAR, monstro);
            conferir(registrou(batalha, "Santuário restaura"), "pulso registrado na rodada " + (i + 1));
            conferir(hpGrupo(grupo) == anterior + cura * 2 - 1, "um pulso por rodada para cada aliado");
        }
        anterior = hpGrupo(grupo);
        batalha.executarRodada(ESPERAR, monstro);
        conferir(!registrou(batalha, "Santuário restaura") && hpGrupo(grupo) == anterior - 1,
                "Santuário termina após dois pulsos");

        Clerigo semMana = new Clerigo("Sem mana");
        semMana.gastarMp(semMana.getMpAtual());
        Grupo outroGrupo = new Grupo(semMana);
        Monstro outroMonstro = new Monstro("Alvo", 1, 10000, 0, 1, 0, 1, 0, 0);
        Batalha outra = new Batalha(outroGrupo, List.of(outroMonstro));
        conferir(!outra.executarAcaoEmArea(new Santuario(), semMana, outroGrupo.getMembrosVivos()), "sem MP");
        outra.executarRodada(ESPERAR, outroMonstro);
        conferir(!registrou(outra, "Santuário restaura"), "falha de MP não agenda pulsos");
    }

    private static void testarChefe(Chefe chefe, int custo, double multiplicador, boolean area) {
        Personagem heroi = new PersonagemDeTeste("Herói");
        Personagem aliado = new PersonagemDeTeste("Aliado");
        Grupo grupo = new Grupo(heroi);
        grupo.adicionarAliado(aliado);
        Batalha batalha = new Batalha(grupo, List.of(chefe));
        int mpInicial = chefe.getMpAtual();
        for (int turno = 1; turno <= 3; turno++) {
            int hpAnterior = hpGrupo(grupo);
            batalha.executarRodada(ESPERAR, chefe);
            if (turno < 3) {
                conferir(!registrou(batalha, chefe.getHabilidadeEspecial()), "ataque comum antes do terceiro turno");
                conferir(hpGrupo(grupo) == hpAnterior - chefe.getAtaque(), "dano básico do chefe");
            } else {
                int dano = (int) (chefe.getAtaque() * multiplicador);
                conferir(registrou(batalha, chefe.getHabilidadeEspecial()), "especial registrado");
                conferir(hpGrupo(grupo) == hpAnterior - dano * (area ? 2 : 1), "dano e alvos da habilidade");
                conferir(chefe.getMpAtual() == mpInicial - custo, "MP especial cobrado uma vez");
            }
        }
        batalha.executarRodada(ESPERAR, chefe);
        conferir(!registrou(batalha, chefe.getHabilidadeEspecial()), "ataque comum após especial");
    }

    private static void testarChefeSemMp(Chefe chefe) {
        chefe.gastarMp(chefe.getMpAtual());
        Personagem heroi = new PersonagemDeTeste("Herói");
        Batalha batalha = new Batalha(new Grupo(heroi), List.of(chefe));
        for (int i = 0; i < 3; i++) batalha.executarRodada(ESPERAR, chefe);
        conferir(!registrou(batalha, chefe.getHabilidadeEspecial()) && registrou(batalha, "Ataque básico"),
                "chefe sem MP usa ataque básico");
    }

    public static void main(String[] args) {
        testarSantuario();
        Jogo jogo = new Jogo(new Grupo(new Guerreiro("Jogador")));
        ConfiguradorMundo.configurarAreas(jogo);
        testarChefe(jogo.getAreas().get(0).getChefe().criarCopia(), 10, 1.6, false);
        testarChefe(jogo.getAreas().get(1).getChefe().criarCopia(), 12, 0.9, true);
        testarChefe(jogo.getAreas().get(2).getChefe().criarCopia(), 20, 1.15, true);
        testarChefeSemMp(jogo.getAreas().get(0).getChefe().criarCopia());
        conferir(HabilidadeEspecialChefe.criar("Desconhecida") == null, "habilidade desconhecida");
        System.out.println("Testes de combate passaram.");
    }
}
