package Tentativa2;

public interface Pilha<T> {

    void push(T info);
    T pop() throws PilhaVaziaException;
    T peek() throws PilhaVaziaException;
    boolean estaVazia();
    void liberar();
}
