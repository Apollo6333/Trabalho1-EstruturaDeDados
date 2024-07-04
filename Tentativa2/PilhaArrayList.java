package Tentativa2;

import java.util.ArrayList;
import java.util.List;

public class PilhaArrayList<T> implements Pilha<T> {
    private List<T> elementos;

    public PilhaArrayList() {
        this.elementos = new ArrayList<>();
    }

    @Override
    public void push(T info) {
        elementos.add(info);
    }

    @Override
    public T pop() throws PilhaVaziaException {
        if (estaVazia()) {
            throw new PilhaVaziaException("Pilha está vazia");
        }
        return elementos.remove(elementos.size() - 1);
    }

    @Override
    public T peek() throws PilhaVaziaException {
        if (estaVazia()) {
            throw new PilhaVaziaException("Pilha está vazia");
        }
        return elementos.get(elementos.size() - 1);
    }

    @Override
    public boolean estaVazia() {
        return elementos.isEmpty();
    }

    @Override
    public void liberar() {
        elementos.clear();
    }
}
