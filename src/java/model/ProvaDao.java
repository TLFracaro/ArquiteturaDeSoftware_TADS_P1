package model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

public class ProvaDao {
     private EntityManager manager;
    
    private void conectar(){
        manager = Persistence.createEntityManagerFactory("siteProvaPU").createEntityManager();
    }
    
    public <T> int salvar(T obj) {
        conectar();
        try {
            manager.getTransaction().begin();
            manager.persist(obj);
            manager.getTransaction().commit();
            return 1;
        } catch (RollbackException erro) {
            return 2;
        } catch (Exception erro) {
            return 0;
        }
    }

    public <T> List listar(String queryNomeada, Class<T> classe) {
        conectar();
        try {
            return manager.createNamedQuery(queryNomeada, classe).getResultList();
        } catch (NoResultException erro) {
            return null;
        }
    }

    public <T>int excluir(Object pk, Class<T> classe) {
        conectar();
        try {
            T obj = manager.find(classe, pk);
            if (obj == null) {
                return 2;
            } else { 
                manager.getTransaction().begin();
                manager.remove(obj);
                manager.getTransaction().commit();
                return 1;
            }
        } catch (Exception erro) {
            return 0;
        }
    }
}
