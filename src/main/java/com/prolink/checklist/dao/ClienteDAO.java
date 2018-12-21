package com.prolink.checklist.dao;

import com.prolink.checklist.model.Indexador;
import com.prolink.factory.Conexao;
import com.prolink.checklist.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;

public class ClienteDAO{
	private Conexao conexao = new Conexao();

	public Set<Indexador> findBy(Indexador indexador){
        Set<Indexador> list = new HashSet<>();
        Connection con = null;
        try {
            String sql = "select distinct ("+indexador.getValor()+") from CLIENTE order by "+indexador.getValor();
            con = conexao.getCon();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Indexador filtro = new Indexador(indexador.getIndex(),rs.getString(1));
                list.add(filtro);
            }
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(con!=null) con.close();
            }catch (SQLException e){
            }
        }
    }
    public List<Cliente> listarPor(Indexador indexador, List<Indexador> values){
        Connection con = null;
        try {
            con = conexao.getCon();
            String sql = "select COD,EMPRESA, STATUS, CNPJ from CLIENTE where "+indexador.getValor()+" in (";

            StringBuilder builder = new StringBuilder();
            for(int i = 0; i<values.size(); i++){
                builder.append(" ?");
                if(i != values.size() -1) builder.append(",");
            }
            builder.append(") order by COD");
            sql+=builder.toString();
            PreparedStatement ps = con.prepareStatement(sql);
            for(int i = 1; i <=values.size(); i++)
                ps.setString(i, values.get(i - 1).getValor());

            return pesquisa(ps);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(con!=null) con.close();
            }catch (SQLException e){
            }
        }
    }
    List<Cliente> pesquisa(PreparedStatement ps) throws SQLException{
        List<Cliente> clientes = new ArrayList<>();
        Set<Cliente> clienteSet = new HashSet<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
                Cliente c = new Cliente();
                c.setId(rs.getInt(1));
                c.setNome(novoNome(rs.getString(2)).toUpperCase());
                c.setStatus(rs.getString(3));
                String cnpj = rs.getString(4);
                c.setCnpj(cnpj==null?"":cnpj);
                clienteSet.add(c);
            }
        clientes.addAll(clienteSet);
        Collections.sort(clientes,Comparator.comparingInt(Cliente::getId));
        return clientes;
    }
    public List<Cliente> listar(){
        Connection con = null;
        try {
            con = conexao.getCon();
            String sql = "select COD,EMPRESA, STATUS, CNPJ from CLIENTE order by COD";
            PreparedStatement ps = con.prepareStatement(sql);
            return pesquisa(ps);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }finally {try {if(con!=null) con.close();}catch (SQLException e){}}
    }
    private String novoNome(String nome){
        String novo= Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return novo;
    }
}