package br.ufrn.dimap.middleware.utils;

public class Wrapper<T> {
    private final T instance;

    public Wrapper(T service) {
        this.instance = service;
    }

    public T getInstance() {
        return this.instance;
    }
}
