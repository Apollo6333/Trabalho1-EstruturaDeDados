package Tentativa2;

public class PilhaArrayList<T> implements Pilha<T> {
    private Object[] elementos;
    private int tamanho;

    public PilhaArrayList() {
        this.elementos = new Object[10]; // inicializa com capacidade inicial de 10
        this.tamanho = 0;
    }

    @Override
    public void push(T info) {
        if (tamanho == elementos.length) {
            aumentarCapacidade();
        }
        elementos[tamanho++] = info;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T pop() throws PilhaVaziaException {
        if (estaVazia()) {
            throw new PilhaVaziaException("Pilha está vazia");
        }
        T elemento = (T) elementos[--tamanho];
        elementos[tamanho] = null; // limpa a referência para permitir GC
        return elemento;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T peek() throws PilhaVaziaException {
        if (estaVazia()) {
            throw new PilhaVaziaException("Pilha está vazia");
        }
        return (T) elementos[tamanho - 1];
    }

    @Override
    public boolean estaVazia() {
        return tamanho == 0;
    }

    @Override
    public void liberar() {
        for (int i = 0; i < tamanho; i++) {
            elementos[i] = null;
        }
        tamanho = 0;
    }

    private void aumentarCapacidade() {
        Object[] novoArray = new Object[elementos.length * 2];
        System.arraycopy(elementos, 0, novoArray, 0, elementos.length);
        elementos = novoArray;
    }
}
