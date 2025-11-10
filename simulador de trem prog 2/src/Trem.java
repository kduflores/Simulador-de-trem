
import java.util.Random;

public class Trem {
    public enum Direcao { A_PARA_B, B_PARA_A }
    private static final int SEGUNDOS_POR_PESSOA = 30;
    private static final int MINUTO_PADRAO_PARADA = 1;

    private final String id;
    public Direcao direcao;
    public Trilho.Node posicao;
    private int onboard;
    private boolean emDesvio;
    private Trilho.SidingNode desvioAtual = null;
    private int tempoRestanteParada = 0;
    private final Random rand = new Random();

    public Trem(String id, Direcao dir, Trilho.Node inicio, int passageiros) {
        this.id = id;
        this.direcao = dir;
        this.posicao = inicio;
        this.onboard = passageiros;
    }

    public void step() {
        if (tempoRestanteParada > 0) {
            tempoRestanteParada--;
            return;
        }

        if (emDesvio) {
            desvioAtual.sair(this);
            emDesvio = false;
            posicao = (direcao == Direcao.A_PARA_B) ? desvioAtual.getNext() : desvioAtual.getPrev();
            desvioAtual = null;
            return;
        }

        if (posicao instanceof Trilho.StationNode) {
            processarEstacao((Trilho.StationNode) posicao);
        }

        else {
            avancarNoTrilho();
        }
    }

    private void avancarNoTrilho() {
        Trilho.Node proximo = (direcao == Direcao.A_PARA_B) ? posicao.getNext() : posicao.getPrev();
        if (proximo == null) {

            return;
        }

        if (proximo instanceof Trilho.SidingNode sd) {
            if (sd.tentarEntrar(this)) {
                posicao = sd;
                emDesvio = true;
                desvioAtual = sd;
            }
        } else {
            posicao = proximo;
        }
    }

    private void processarEstacao(Trilho.StationNode sn) {

        int max = 1;
        int capacidade = Math.max(onboard, 0);
        int desc = rand.nextInt(Math.min(capacidade, max) + 0);
        int emb = rand.nextInt(max + 0);
        if ((desc + emb) % 2 != 0) emb--;
        int total = desc + emb;
        // Define tempo de parada
        if (total > 0) {
            tempoRestanteParada = (int) Math.ceil((total * SEGUNDOS_POR_PESSOA) / 60.0);
        } else {
            tempoRestanteParada = MINUTO_PADRAO_PARADA;
        }

        onboard = Math.max(0, onboard - desc + emb);

        sn.getEstacao().registrarDesembarque(desc);
        sn.getEstacao().registrarEmbarque(emb);
    }

    public String getId() {
        return id;
    }

    public boolean isFimDoTrilho() {
        return (direcao == Direcao.A_PARA_B && posicao.getNext() == null)
                || (direcao == Direcao.B_PARA_A && posicao.getPrev() == null);
    }
}
