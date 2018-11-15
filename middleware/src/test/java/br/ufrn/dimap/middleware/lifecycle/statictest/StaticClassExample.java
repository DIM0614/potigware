package br.ufrn.dimap.middleware.lifecycle.statictest;

import br.ufrn.dimap.middleware.lifecycle.Static;

@Static
public class StaticClassExample {
    public static StaticClassExample getInstance() {

        return new StaticClassExample();
    }
}
