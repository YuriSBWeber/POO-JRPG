package rpg;

import java.util.ArrayList;
import java.util.List;

import rpg.world.Area;
import rpg.world.Cidade;
import rpg.world.Grupo;

public class Jogo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Grupo grupo;
    private Cidade cidade;
    private List<Area> areas;
    private int moedas;

    private Area areaAtual;
    private int batalhasVencidasNaArea;
    private int recrutamentosPermitidos;
    private int recrutamentosRealizados;
    
    public Jogo(Grupo grupo) {
        this.grupo = grupo;
        this.cidade = new Cidade("Cidade Inicial");
        this.areas = new ArrayList<>();
        this.moedas = 0;

        this.areaAtual = null;
        this.batalhasVencidasNaArea = 0;
        this.recrutamentosPermitidos = 0;
        this.recrutamentosRealizados = 0;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public int getMoedas() {
        return moedas;
    }

    public void adicionarMoedas(int quantidade) {
        moedas += quantidade;
    }

    public boolean gastarMoedas(int quantidade) {
        if (moedas < quantidade) {
            return false;
        }

        moedas -= quantidade;
        return true;
    }

    public void adicionarArea(Area area) {
        areas.add(area);
    }

    public Area getAreaAtual() {
        return areaAtual;
    }

    public void entrarNaArea(Area area) {
        areaAtual = area;
        batalhasVencidasNaArea = 0;
    }

    public int getBatalhasVencidasNaArea() {
        return batalhasVencidasNaArea;
    }

    public void registrarVitoriaComum() {
        batalhasVencidasNaArea++;
    }

    public boolean deveEnfrentarChefe() {
        return batalhasVencidasNaArea >= 5;
    }

    public void concluirAreaAtual() {
        int indice = areas.indexOf(areaAtual);

        if (indice < 0) throw new IllegalStateException("Nenhuma área válida está ativa.");
        if (!areaAtual.isConcluida() && (indice == 0 || indice == 1)
                && !areas.get(indice + 1).isDesbloqueada()) {
            recrutamentosPermitidos++;
        }
        areaAtual.concluir();

        if (indice < areas.size() - 1) {
            areas.get(indice + 1).desbloquear();
        }

        areaAtual = null;
        batalhasVencidasNaArea = 0;
    }
    public boolean chefeDerrotado(int indice) {
        if (indice < 0 || indice >= areas.size()) return false;
        return areas.get(indice).isConcluida()
                || (indice + 1 < areas.size() && areas.get(indice + 1).isDesbloqueada());
    }
    public boolean podeRecrutarAliado() {
        return recrutamentosRealizados < recrutamentosPermitidos
                && grupo.getMembros().size() < 3;
    }

    public void registrarRecrutamento() {
        recrutamentosRealizados++;
    }
}
