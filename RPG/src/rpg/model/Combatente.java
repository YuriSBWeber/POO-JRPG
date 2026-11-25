package rpg.model;

public abstract class Combatente implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private int nivel;

    private int hpMaximo;
    private int hpAtual;

    private int mpMaximo;
    private int mpAtual;

    private int ataque;
    private int defesa;
    private int velocidade;

    private int bonusAtaqueFuria;
    private int penalidadeDefesaFuria;
    private int penalidadeDefesaArmadura;
    private boolean recebeuDanoNestaRodada;
    private int turnosFuria;
    private int turnosDefesaReduzida;
    
    public Combatente(String nome, int nivel, int hpMaximo, int mpMaximo,
            int ataque, int defesa, int velocidade) {
    	
        this.nome = nome;
        this.nivel = nivel;

        this.hpMaximo = hpMaximo;
        this.hpAtual = hpMaximo;

        this.mpMaximo = mpMaximo;
        this.mpAtual = mpMaximo;

        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public int getHpMaximo() {
        return hpMaximo;
    }

    public int getHpAtual() {
        return hpAtual;
    }

    public int getMpMaximo() {
        return mpMaximo;
    }

    public int getMpAtual() {
        return mpAtual;
    }

    public int getAtaque() {
        return ataque + bonusAtaqueFuria;
    }

    public int getDefesa() {
        int defesaAtual = defesa - penalidadeDefesaFuria
                - penalidadeDefesaArmadura;

        return Math.max(0, defesaAtual);
    }

    public int getVelocidade() {
        return velocidade;
    }

    public boolean estaVivo() {
        return hpAtual > 0;
    }

    public void receberDano(int dano) {
        recebeuDanoNestaRodada = true;
        hpAtual -= dano;

        if (hpAtual < 0) {
            hpAtual = 0;
        }
    }

    public void curar(int valor) {
        hpAtual += valor;

        if (hpAtual > getHpMaximo()) {
            hpAtual = getHpMaximo();
        }
    }

    public void recuperarMp(int valor) {
        mpAtual = Math.min(mpMaximo, mpAtual + Math.max(0, valor));
    }

    public void ajustarHpAoMaximo() {
        hpAtual = Math.min(hpAtual, getHpMaximo());
    }

    public boolean gastarMp(int custo) {
        if (mpAtual < custo) {
            return false;
        }

        mpAtual -= custo;
        return true;
    }

    public void ativarFuria() {
        bonusAtaqueFuria = (int) (ataque * 0.40);
        penalidadeDefesaFuria = (int) (defesa * 0.20);
        turnosFuria = 3;
    }

    public void aplicarDestruirArmadura() {
        penalidadeDefesaArmadura = (int) (defesa * 0.30);
        turnosDefesaReduzida = 3;
    }

    public void reduzirEfeitosTemporarios() {
        if (turnosFuria > 0) {
            turnosFuria--;

            if (turnosFuria == 0) {
                bonusAtaqueFuria = 0;
                penalidadeDefesaFuria = 0;
            }
        }

        if (turnosDefesaReduzida > 0) {
            turnosDefesaReduzida--;

            if (turnosDefesaReduzida == 0) {
                penalidadeDefesaArmadura = 0;
            }
        }
    }

    public boolean estaComFuria() {
        return turnosFuria > 0;
    }
    protected void aumentarAtributos(int hp, int mp, int ataque,
            int defesa, int velocidade) {

        nivel++;

        hpMaximo += hp;
        hpAtual = getHpMaximo();

        mpMaximo += mp;
        mpAtual = mpMaximo;

        this.ataque += ataque;
        this.defesa += defesa;
        this.velocidade += velocidade;
    }
    public void restaurarAposBatalha() {
        hpAtual = getHpMaximo();
        mpAtual = mpMaximo;

        bonusAtaqueFuria = 0;
        penalidadeDefesaFuria = 0;
        penalidadeDefesaArmadura = 0;

        turnosFuria = 0;
        turnosDefesaReduzida = 0;
    }
    public void iniciarNovaRodada() {
        recebeuDanoNestaRodada = false;
    }

    public boolean recebeuDanoNestaRodada() {
        return recebeuDanoNestaRodada;
    }
}
