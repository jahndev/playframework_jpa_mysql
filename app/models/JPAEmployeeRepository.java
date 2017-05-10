package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAEmployeeRepository implements EmployeeRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAEmployeeRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Employee> add(Employee employee) {
        return supplyAsync(() -> wrap(em -> insert(em, employee)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Employee>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Employee insert(EntityManager em, Employee employee) {
        em.persist(employee);
        return employee;
    }

    private Stream<Employee> list(EntityManager em) {
        List<Employee> employee = em.createQuery("select p from Employee p", Employee.class).getResultList();
        return employee.stream();
    }
}
