package manager;

import entities.HttpTaskServer;
import kv.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        new KVServer().start();
        HttpTaskServer.getInstance().start();
    }
}
