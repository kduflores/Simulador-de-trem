import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Simulador {
    private Trilho trilho;
    private final List<Trem> trens = new ArrayList<>();
    private final int inicio = 8 * 60;           // 08:00 em minutos
    private final int fim = 17 * 60 + 30;        // 17:30 em minutos
    private Random rand = new Random();

    public Simulador(int N) {
        trilho = new Trilho(N);
    }

    public Simulador(Trilho trilho) {
        this.trilho = trilho;
    }

    public void scheduleTrains() {
        int time = inicio;
        int id = 1;
        while (time <= fim) {
            trens.add(new Trem("T" + id++, Trem.Direcao.A_PARA_B, trilho.getInicioA(), rand.nextInt(41) + 10));
            trens.add(new Trem("T" + id++, Trem.Direcao.B_PARA_A, trilho.getInicioB(), rand.nextInt(41) + 10));
            time += 30;
        }
    }

    public void run() throws IOException {
        scheduleTrains();

        int tempo = inicio;
        int maxTime = trilho.getEstacoes().size() * 50 + 200;

        while (!trens.isEmpty() && tempo - inicio < maxTime) {
            System.out.printf("%02d:%02d\n", tempo / 60, tempo % 60);

            for (Trem t : new ArrayList<>(trens)) {
                t.step();

                boolean chegouDestino;
                if (t.direcao == Trem.Direcao.A_PARA_B) {
                    chegouDestino = (t.posicao == trilho.getInicioB());
                } else {
                    chegouDestino = (t.posicao == trilho.getInicioA());
                }

                if (chegouDestino) {
                    trens.remove(t);
                }
            }
            tempo++;
        }

        if (!trens.isEmpty()) {
            System.out.println("Aviso: tempo máximo da simulação atingido antes que todos os trens chegassem.");
        }

        saveReport();
        System.out.println("Simulação concluída. Relatório salvo em relatorio.txt");
    }

    private void saveReport() throws IOException {
        try (FileWriter fw = new FileWriter("relatorio.txt")) {
            for (Trilho.StationNode sn : trilho.getEstacoes()) {
                Estacao e = sn.getEstacao();
                fw.write(e.getNome() + ": embarcados=" + e.getTotalEmbarcados()
                        + ", desembarcados=" + e.getTotalDesembarcados() + "\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int N = new Random().nextInt(21) + 10;
        Simulador simulador = new Simulador(N);

        int i = 1;
        for (Trilho.StationNode sn : simulador.trilho.getEstacoes()) {
            System.out.println("Estacao" + i++ + ": " + sn.getEstacao().getNome());
        }

        simulador.run();
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public Trilho getTrilho() {
        return trilho;
    }

    public void setTrilho(Trilho trilho) {
        this.trilho = trilho;
    }
}
