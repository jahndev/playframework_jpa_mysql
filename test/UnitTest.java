import controllers.EmployeeController;
import models.Employee;
import models.EmployeeRepository;
import org.junit.Test;
import play.data.FormFactory;
import play.data.format.Formatters;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Content;

import javax.validation.Validator;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.invokeWithContext;

/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * <p>
 * https://www.playframework.com/documentation/latest/JavaTest
 */
public class UnitTest {

    @Test
    public void checkIndex() {
        EmployeeRepository repository = mock(EmployeeRepository.class);
        FormFactory formFactory = mock(FormFactory.class);
        HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
        final EmployeeController controller = new EmployeeController(formFactory, repository, ec);
        final Result result = controller.index();

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void checkTemplate() {
        Content html = views.html.index.render();
        assertThat(html.contentType()).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Add Employee");
    }

    @Test
    public void checkAddEmployee() {
        // Easier to mock out the form factory inputs here
        MessagesApi messagesApi = mock(MessagesApi.class);
        Validator validator = mock(Validator.class);
        FormFactory formFactory = new FormFactory(messagesApi, new Formatters(messagesApi), validator);

        // Don't need to be this involved in setting up the mock, but for demo it works:
        EmployeeRepository repository = mock(EmployeeRepository.class);
        Employee employee = new Employee();
        employee.id = 1L;
        employee.name = "Steve";
        when(repository.add(any())).thenReturn(supplyAsync(() -> employee));

        // Set up the request builder to reflect input
        final Http.RequestBuilder requestBuilder = new Http.RequestBuilder().method("post").bodyJson(Json.toJson(employee));

        // Add in an Http.Context here using invokeWithContext:
        final CompletionStage<Result> stage = invokeWithContext(requestBuilder, () -> {
            HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());

            // Create controller and call method under test:
            final EmployeeController controller = new EmployeeController(formFactory, repository, ec);
            return controller.addEmployee();
        });

        // Test the completed result
        await().atMost(1, SECONDS).until(() ->
            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(result ->
                result.status() == SEE_OTHER, "Should redirect after operation"
            )
        );
    }

}
