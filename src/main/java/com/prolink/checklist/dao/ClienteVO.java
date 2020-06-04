package com.prolink.checklist.dao;

import com.github.davidmoten.rx.jdbc.annotations.Column;

public interface ClienteVO {
    @Column("COD")
    int id();
    @Column("EMPRESA")
    String nome();
    @Column("STATUS")
    String status();
    @Column("CNPJ")
    String cnpj();
}
