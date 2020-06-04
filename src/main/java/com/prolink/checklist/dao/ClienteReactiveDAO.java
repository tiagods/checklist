package com.prolink.checklist.dao;

import com.github.davidmoten.rx.jdbc.ConnectionProvider;
import com.github.davidmoten.rx.jdbc.ConnectionProviderFromUrl;
import com.github.davidmoten.rx.jdbc.Database;

import java.util.List;

public class ClienteReactiveDAO {

    static ConnectionProvider connectionProvider =
            new ConnectionProviderFromUrl(
                    "jdbc:postgresql://localhost:5432/processos", "postgres", "admin");

    public static void main(String[] args) {
        List<ClienteVO> list = Database.from(connectionProvider)
                .select("select COD,EMPRESA, STATUS, CNPJ from CLIENTE")
                .autoMap(ClienteVO.class)
                .toList()
                .toBlocking()
                .single();
        list.forEach(c->System.out.println(c.nome()));
    }
}
