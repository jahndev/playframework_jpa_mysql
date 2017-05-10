package controllers;

import models.Employee;
import models.EmployeeRepository;

import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link play.libs.concurrent.HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class EmployeeController extends Controller {

    private final FormFactory formFactory;
    private final EmployeeRepository employeeRepository;
    private final HttpExecutionContext ec;

    @Inject
    public EmployeeController(FormFactory formFactory, EmployeeRepository employeeRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.employeeRepository = employeeRepository;
        this.ec = ec;
    }
	
	//Index
    public Result index() {
        return ok(views.html.index.render());
    }

	/**
	*	This Method let add a new Employee
	*/
    public CompletionStage<Result> addEmployee() {
        Employee employee = formFactory.form(Employee.class).bindFromRequest().get();
        return employeeRepository.add(employee).thenApplyAsync(p -> {
            return redirect(routes.EmployeeController.index());
        }, ec.current());
    }

	/**
	* this method return the Employee list
	*/
    public CompletionStage<Result> getEmployee() {
        return employeeRepository.list().thenApplyAsync(employeeStream -> {
            return ok(toJson(employeeStream.collect(Collectors.toList())));
        }, ec.current());
    }

}
