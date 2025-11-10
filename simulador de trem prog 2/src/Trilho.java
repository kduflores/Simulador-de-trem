import java.util.*;

public class Trilho {
    public abstract static class Node {
        private Node next;
        private Node prev;
        public Node getNext() { return next; }
        public Node getPrev() { return prev; }
        void setNext(Node n) { next = n; }
        void setPrev(Node p) { prev = p; }
    }

    public static class TrackNode extends Node {}

    public static class SidingNode extends Node {
        private Trem ocupadoPor;
        private final Queue<Trem> fila = new LinkedList<>();

        public synchronized boolean tentarEntrar(Trem t) {
            if (ocupadoPor == null) { ocupadoPor = t; return true; }
            if (!fila.contains(t)) fila.add(t);
            return false;
        }

        public synchronized void sair(Trem t) {
            if (ocupadoPor == t) {
                ocupadoPor = fila.poll();
            }
        }
    }

    public static class StationNode extends Node {
        private final Estacao estacao;
        public StationNode(Estacao e) { this.estacao = e; }
        public Estacao getEstacao() { return estacao; }
    }

    private final Node inicioA;
    private final Node inicioB;
    private final List<StationNode> estacoes;

    public Trilho(int N) {
        estacoes = new ArrayList<>();
        Node prev = new TrackNode();
        inicioA = prev;
        for (int i = 1; i <= N; i++) {
            for (int k = 0; k < 20; k++) {
                TrackNode t = new TrackNode(); prev.setNext(t); t.setPrev(prev); prev = t;
            }
            SidingNode sd1 = new SidingNode(); prev.setNext(sd1); sd1.setPrev(prev); prev = sd1;
            StationNode sn = new StationNode(new Estacao("Estacao" + i));
            prev.setNext(sn); sn.setPrev(prev); prev = sn;
            estacoes.add(sn);
            SidingNode sd2 = new SidingNode(); prev.setNext(sd2); sd2.setPrev(prev); prev = sd2;
        }
        for (int k = 0; k < 20; k++) {
            TrackNode t = new TrackNode(); prev.setNext(t); t.setPrev(prev); prev = t;
        }
        inicioB = prev;
    }

    public Node getInicioA() { return inicioA; }
    public Node getInicioB() { return inicioB; }
    public List<StationNode> getEstacoes() { return estacoes; }
}