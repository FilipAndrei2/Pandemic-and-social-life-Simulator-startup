package org.filipandrei.pandemic.main;

import org.filipandrei.pandemic.model.configs.Configs;

public class Simulator {
    public static void main(String[] args) {
        System.out.println(Configs.get("db.url"));
    }
}
