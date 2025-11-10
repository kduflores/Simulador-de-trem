import java.util.concurrent.ThreadLocalRandom;

public class Estacao {
    private final String nome;
    private int totalEmbarcados;;
    private int totalDesembarcados = ThreadLocalRandom.current().nextInt(0, totalEmbarcados + 1000);

    public Estacao(String nome) {
        this.nome = nome;
        totalEmbarcados = ThreadLocalRandom.current().nextInt(0, totalDesembarcados + 1000);
    }

    public String getNome() {
        return nome;
    }

    public synchronized void registrarEmbarque(int qtd) {
        totalEmbarcados += qtd;
    }

    public synchronized void registrarDesembarque(int qtd) {
        totalDesembarcados += qtd;
    }

    public int getTotalEmbarcados() {
        return totalEmbarcados;
    }

    public int getTotalDesembarcados() {
        return totalDesembarcados;
    }
}