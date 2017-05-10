package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAEmployeeRepository.class)
public interface EmployeeRepository {

    CompletionStage<Employee> add(Employee employee);
	
    CompletionStage<Stream<Employee>> list();
}
