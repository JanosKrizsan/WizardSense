package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Supplier;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoMem implements GenericQueriesDao<Supplier> {

    private List<Supplier> data = new ArrayList<>();
    private static SupplierDaoMem instance = null;

    private SupplierDaoMem() {
    }

    public static SupplierDaoMem getInstance() {
        if (instance == null) {
            instance = new SupplierDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        supplier.setId(data.size() + 1);
        data.add(supplier);
    }

    @Override
    public Supplier find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Supplier> getAll() {
        return data;
    }

    @Override
    public void removeAll() {
        data.clear();
    }
}
