package org.example.sgc.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    void insert(T t) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T t) throws SQLException;
    void delete(int id) throws SQLException;
}
